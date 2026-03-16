package org.rumbledb.runtime.typing;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.*;
import org.rumbledb.items.AnnotatedItem;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.flwor.NativeClauseContext;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.ItemType;
import org.rumbledb.types.SequenceType;
import org.rumbledb.types.WhitespaceFacet;
import org.rumbledb.types.SequenceType.Arity;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;


public class CastIterator extends AtMostOneItemLocalRuntimeIterator {
    private static final long serialVersionUID = 1L;
    private final RuntimeIterator child;
    private final SequenceType sequenceType;

    public CastIterator(
            RuntimeIterator child,
            SequenceType sequenceType,
            RuntimeStaticContext staticContext
    ) {
        super(Collections.singletonList(child), staticContext);
        this.child = child;
        this.sequenceType = sequenceType;
    }

    public Item materializeFirstItemOrNull(
            DynamicContext dynamicContext
    ) {
        if (!this.sequenceType.isResolved()) {
            this.sequenceType.resolve(dynamicContext, getMetadata());
        }
        ItemType targetItemType = this.sequenceType.getItemType();
        // XPath 3.1 cast target must be a generalized atomic type: either an atomic type or
        // a pure union type (union whose members are all atomic). See XPath F&O 3.1 §19.3.5
        // Casting to union types; XPath 3.1 §2.5.4 SequenceType (SingleType uses SimpleTypeName).
        boolean validCastTarget =
            targetItemType.isAtomicItemType()
                || (targetItemType.isUnionType()
                    && targetItemType.getTypes().stream().allMatch(ItemType::isAtomicItemType));
        if (!validCastTarget) {
            throw new UnknownCastTypeException(
                    "The type "
                        + this.sequenceType.getItemType().getIdentifierString()
                        + " is not atomic. Cast can only be used with atomic types.",
                    getMetadata()
            );
        }

        // the target type cannot be xs:NOTATION, xs:anySimpleType, or xs:anyAtomicType
        // TODO: add support for xs:anySimpleType
        if (targetItemType.equals(BuiltinTypesCatalogue.NOTATIONItem)) {
            throw new CastableException("Invalid target type for cast expression: xs:NOTATION", getMetadata());
        }
        if (targetItemType.equals(BuiltinTypesCatalogue.atomicItem)) {
            throw new CastableException("Invalid target type for cast expression: xs:anyAtomicType", getMetadata());
        }

        Item item;
        try {
            item = this.child.materializeAtMostOneItemOrNull(dynamicContext);
            if (item != null && !item.getDynamicType().isResolved()) {
                item.getDynamicType().resolve(dynamicContext, getMetadata());
            }
        } catch (MoreThanOneItemException e) {
            throw new UnexpectedTypeException(
                    " Sequence of more than one item can not be treated as type "
                        + this.sequenceType.toString(),
                    getMetadata()
            );
        }
        if (
            item == null && !this.sequenceType.isEmptySequence() && this.sequenceType.getArity() != Arity.OneOrZero
        ) {
            throw new UnexpectedTypeException(
                    " Empty sequence can not be cast to type with quantifier '1'",
                    getMetadata()
            );
        }
        if (item == null) {
            return null;
        }
        Item result = castItemToType(item, this.sequenceType.getItemType(), getMetadata());
        if (result == null) {
            String message = String.format(
                "\"%s\": this literal is not castable to type %s.",
                item.serialize(),
                this.sequenceType.getItemType()
            );
            throw new CastException(message, getMetadata());
        }
        return result;
    }

    public static Item castItemToType(Item item, ItemType targetType, ExceptionMetadata metadata) {
        Item converted = null;
        // first we try to atomize if item is not atomic
        if (!item.isAtomic()) {
            try {
                List<Item> atomized = item.atomizedValue();
                if (atomized.size() > 1) {
                    throw new UnexpectedTypeException(
                            "Atomization in cast resulted in more than one item.",
                            metadata
                    );
                }
                item = atomized.get(0);
            } catch (CannotAtomizeException e) {
                // need to add metadata, e has no metadata
                RumbleException castE = new CannotAtomizeException(
                        "Atomization in cast failed: \"" + item.serialize() + "\"",
                        metadata
                );
                castE.initCause(e);
                throw castE;
            }
        }

        if (!item.getDynamicType().isStaticallyCastableAs(targetType)) {
            String message = String.format(
                "\"%s\": a value of type %s is not castable to type %s",
                item.serialize(),
                item.getDynamicType(),
                targetType
            );
            throw new UnexpectedTypeException(message, metadata);
        }

        try {
            ItemType itemType = item.getDynamicType();
            // XPath and XQuery F&O 3.1 §19.3.1:
            // "When ST is the same type as TT: this case always succeeds, returning SV unchanged."
            if (itemType.equals(targetType)) {
                // no errors on facet checking can occur in this case
                // so we can return the item unchanged
                return item;
            }

            // XPath and XQuery F&O 3.1 §19.3.2:
            // "When itemType-subtype(ST, TT) is true: This case is described in 19.3.2
            // Casting from derived types to parent types."
            if (itemType.isSubtypeOf(targetType)) {
                // by spec, the source is defined starting from the target (parent) type, applying restrictions via
                // facets.
                // this means that all values of the source type are valid for the target type,
                // so we can return the converted item directly.
                return convertForCastTargetType(item, targetType, metadata);
            }

            // XPath and XQuery F&O 3.1 §19.2: Casting from xs:string and xs:untypedAtomic
            // When the target type is a derived type that is restricted by a pattern facet,
            // the lexical form is first checked against the pattern before further casting is attempted (See 19.3.1
            // Casting to derived types).
            // If the lexical form does not conform to the pattern, a dynamic error [err:FORG0001] is raised.
            if (
                item.isUntypedAtomic()
                    || item.isString()
                    || itemType.getCastingPrimitiveType().equals(BuiltinTypesCatalogue.stringItem)
            ) {
                // check that the item value conforms to the lexical patterns, if converting to a primitive type
                if (targetType.isCastingPrimitive() && !checkLexicalPatterns(item, targetType)) {
                    return null;
                }
                // check that the item value conforms to the pattern facet, if converting to a derived type
                if (!checkPatternFacet(item, targetType)) {
                    return null;
                }
            }

            ItemType sourcePrimitiveType = itemType.getCastingPrimitiveType();
            ItemType targetPrimitiveType = targetType.getCastingPrimitiveType();
            // XPath and XQuery F&O 3.1 §19.3.4: Casting across the type hierarchy
            // - "Otherwise (P(ST) is not the same type as P(TT))" (19.3.4).
            if (!targetType.isCastingPrimitive() && !sourcePrimitiveType.equals(targetPrimitiveType)) {
                // XPath and XQuery F&O 3.1 §19.3.4:
                // 1) cast up to primitive source type
                // 2) cast to primitive target type
                // 3) cast down to final target type

                Item sourcePrimitiveValue = itemType.isCastingPrimitive()
                    ? item
                    : convertForCastTargetType(item, sourcePrimitiveType, metadata);
                if (sourcePrimitiveValue == null) {
                    return null;
                }

                Item targetPrimitiveValue;
                if (targetType.isSubtypeOf(BuiltinTypesCatalogue.NOTATIONItem)) {
                    // F&O §19.3.4 step 2: when TT is derived from xs:NOTATION, assume cast to
                    // xs:NOTATION succeeds for this logical step.
                    targetPrimitiveValue = sourcePrimitiveValue;
                } else {
                    targetPrimitiveValue = convertForCastTargetType(
                        sourcePrimitiveValue,
                        targetPrimitiveType,
                        metadata
                    );
                    if (targetPrimitiveValue == null) {
                        return null;
                    }
                }

                if (!checkValueConformsToTargetFacets(targetPrimitiveValue, targetType)) {
                    return null;
                }
                converted = convertForCastTargetType(targetPrimitiveValue, targetType, metadata);
            } else {
                // This covers both
                // F&O 3.1 §19.1 Casting from primitive types to derived types
                // F&O 3.1 §19.3.3: Casting within a branch of the type hierarchy
                // if ST and TT share the same primitive type, the cast succeeds iff the value
                // conforms to the target type's facets.
                converted = convertForCastTargetType(item, targetType, metadata);
            }

            // If the conversion failed (returned null)
            // or if the value does not conform to the target facet types, throw a CastException
            if (converted == null || !checkValueConformsToTargetFacets(converted, targetType)) {
                return null;
            }

            return converted;

        } catch (
                DatetimeOverflowOrUnderflow
                | DurationOverflowOrUnderflow
                | InvalidLexicalValueException
                | CastException e
        ) {
            throw e;
        } catch (Exception e) {
            String message = String.format(
                "\"%s\": this literal is not castable to type %s. %s",
                item.serialize(),
                targetType,
                e
            );
            throw new CastException(message, metadata);
        }
    }

    /**
     * Converts an already-atomized source item into the requested cast target representation.
     * This method only performs value conversion/materialization. Castability checks and facet validation
     * are handled by the caller.
     *
     * @param item the source atomic item.
     * @param targetType the cast target type representation to materialize.
     * @param metadata exception metadata propagated to conversion errors where applicable.
     * @return the converted item for {@code targetType}, or {@code null} if no conversion applies.
     */
    private static Item convertForCastTargetType(
            Item item,
            ItemType targetType,
            ExceptionMetadata metadata
    ) {
        ItemType sourceType = item.getDynamicType();
        if (sourceType.equals(targetType)) {
            return item;
        }
        if (targetType.equals(BuiltinTypesCatalogue.untypedAtomicItem)) {
            return ItemFactory.getInstance().createUntypedAtomicItem(item.getStringValue());
        }
        if (targetType.isSubtypeOf(BuiltinTypesCatalogue.nullItem)) {
            Item convertedValue = null;
            if (item.isString() && item.getStringValue().trim().equals("null")) {
                convertedValue = ItemFactory.getInstance().createNullItem();
            }
            return finalizeAtomicBranchValue(convertedValue, targetType, BuiltinTypesCatalogue.nullItem);
        }

        if (targetType.isSubtypeOf(BuiltinTypesCatalogue.stringItem)) {
            Item stringValue = ItemFactory.getInstance().createStringItem(item.getStringValue());
            return finalizeAtomicBranchValue(stringValue, targetType, BuiltinTypesCatalogue.stringItem);
        }
        if (targetType.isSubtypeOf(BuiltinTypesCatalogue.booleanItem)) {
            Item convertedValue = null;
            if (item.isString() || item.isUntypedAtomic()) {
                String lexical = item.getStringValue().trim();
                if ("true".equals(lexical) || "1".equals(lexical)) {
                    convertedValue = ItemFactory.getInstance().createBooleanItem(true);
                } else if ("false".equals(lexical) || "0".equals(lexical)) {
                    convertedValue = ItemFactory.getInstance().createBooleanItem(false);
                }
            } else if (item.isInt()) {
                convertedValue = ItemFactory.getInstance().createBooleanItem(item.getIntValue() != 0);
            } else if (item.isInteger()) {
                convertedValue = ItemFactory.getInstance()
                    .createBooleanItem(!item.getIntegerValue().equals(BigInteger.ZERO));
            } else if (item.isDecimal()) {
                convertedValue = ItemFactory.getInstance()
                    .createBooleanItem(item.getDecimalValue().compareTo(BigDecimal.ZERO) != 0);
            } else if (item.isDouble()) {
                double value = item.getDoubleValue();
                boolean booleanValue = !(Double.isNaN(value) || value == 0.0d);
                convertedValue = ItemFactory.getInstance().createBooleanItem(booleanValue);
            } else if (item.isFloat()) {
                float value = item.getFloatValue();
                boolean booleanValue = !(Float.isNaN(value) || value == 0.0f);
                convertedValue = ItemFactory.getInstance().createBooleanItem(booleanValue);
            }
            return finalizeAtomicBranchValue(convertedValue, targetType, BuiltinTypesCatalogue.booleanItem);
        }
        if (targetType.isSubtypeOf(BuiltinTypesCatalogue.doubleItem)) {
            Item convertedValue = null;
            if (item.isString() || item.isUntypedAtomic()) {
                convertedValue = ItemFactory.getInstance().createDoubleItem(item.castToDoubleValue());
            } else if (item.isBoolean()) {
                convertedValue = ItemFactory.getInstance().createDoubleItem(item.getBooleanValue() ? 1 : 0);
            } else if (item.isNumeric()) {
                convertedValue = ItemFactory.getInstance().createDoubleItem(item.castToDoubleValue());
            }
            return finalizeAtomicBranchValue(convertedValue, targetType, BuiltinTypesCatalogue.doubleItem);
        }
        if (targetType.isSubtypeOf(BuiltinTypesCatalogue.floatItem)) {
            Item convertedValue = null;
            if (item.isString() || item.isUntypedAtomic()) {
                convertedValue = ItemFactory.getInstance().createFloatItem(item.castToFloatValue());
            } else if (item.isBoolean()) {
                convertedValue = ItemFactory.getInstance().createFloatItem(item.getBooleanValue() ? 1 : 0);
            } else if (item.isNumeric()) {
                convertedValue = ItemFactory.getInstance().createFloatItem(item.castToFloatValue());
            }
            return finalizeAtomicBranchValue(convertedValue, targetType, BuiltinTypesCatalogue.floatItem);
        }

        // Non-float/double targets do not accept NaN or infinities.
        if (
            (item.isFloat() && (Float.isNaN(item.getFloatValue()) || Float.isInfinite(item.getFloatValue())))
                || (item.isDouble()
                    && (Double.isNaN(item.getDoubleValue()) || Double.isInfinite(item.getDoubleValue())))
        ) {
            throw new InvalidLexicalValueException(
                    "NaN or INF cannot be cast to another type than Float or Double",
                    metadata
            );
        }
        if (targetType.isSubtypeOf(BuiltinTypesCatalogue.intItem)) {
            // Keep IntItem as the concrete storage for the xs:int family, but preserve
            // derived target typing (e.g., xs:byte) for dynamic type checks.
            Item intValue = ItemFactory.getInstance().createIntItem(item.castToIntValue());
            // for subtypes of xs:int, return the annotated item with the target type
            return finalizeAtomicBranchValue(intValue, targetType, BuiltinTypesCatalogue.intItem);
        }
        if (targetType.isSubtypeOf(BuiltinTypesCatalogue.integerItem)) {
            Item convertedValue = null;
            if (item.isString() || item.isUntypedAtomic()) {
                convertedValue = ItemFactory.getInstance().createIntegerItem(item.castToIntegerValue());
            } else if (item.isBoolean()) {
                convertedValue = ItemFactory.getInstance()
                    .createIntegerItem(item.getBooleanValue() ? BigInteger.ONE : BigInteger.ZERO);
            } else if (item.isNumeric()) {
                convertedValue = ItemFactory.getInstance().createIntegerItem(item.castToIntegerValue());
            }
            return finalizeAtomicBranchValue(convertedValue, targetType, BuiltinTypesCatalogue.integerItem);
        }
        if (targetType.isSubtypeOf(BuiltinTypesCatalogue.decimalItem)) {
            Item convertedValue = null;
            if (item.isString() || item.isUntypedAtomic()) {
                convertedValue = ItemFactory.getInstance().createDecimalItem(item.castToDecimalValue());
            } else if (item.isBoolean()) {
                convertedValue = ItemFactory.getInstance()
                    .createDecimalItem(item.getBooleanValue() ? BigDecimal.ONE : BigDecimal.ZERO);
            } else if (item.isNumeric()) {
                convertedValue = ItemFactory.getInstance().createDecimalItem(item.castToDecimalValue());
            }
            return finalizeAtomicBranchValue(convertedValue, targetType, BuiltinTypesCatalogue.decimalItem);
        }
        if (targetType.isSubtypeOf(BuiltinTypesCatalogue.anyURIItem)) {
            Item convertedValue = null;
            if (item.isString() || item.isUntypedAtomic()) {
                convertedValue = ItemFactory.getInstance().createAnyURIItem(item.getStringValue().trim());
            }
            return finalizeAtomicBranchValue(convertedValue, targetType, BuiltinTypesCatalogue.anyURIItem);
        }
        if (targetType.isSubtypeOf(BuiltinTypesCatalogue.base64BinaryItem)) {
            Item convertedValue = null;
            if (item.isString() || item.isUntypedAtomic()) {
                convertedValue = ItemFactory.getInstance().createBase64BinaryItem(item.getStringValue().trim());
            } else if (item.isHexBinary()) {
                convertedValue = ItemFactory.getInstance()
                    .createBase64BinaryItem(Base64.encodeBase64String(item.getBinaryValue()));
            }
            return finalizeAtomicBranchValue(convertedValue, targetType, BuiltinTypesCatalogue.base64BinaryItem);
        }
        if (targetType.isSubtypeOf(BuiltinTypesCatalogue.hexBinaryItem)) {
            Item convertedValue = null;
            if (item.isString() || item.isUntypedAtomic()) {
                convertedValue = ItemFactory.getInstance().createHexBinaryItem(item.getStringValue().trim());
            } else if (item.isBase64Binary()) {
                convertedValue = ItemFactory.getInstance()
                    .createHexBinaryItem(Hex.encodeHexString(item.getBinaryValue()));
            }
            return finalizeAtomicBranchValue(convertedValue, targetType, BuiltinTypesCatalogue.hexBinaryItem);
        }
        if (targetType.isSubtypeOf(BuiltinTypesCatalogue.dateItem)) {
            Item convertedValue = null;
            if (item.isString() || item.isUntypedAtomic()) {
                convertedValue = ItemFactory.getInstance().createDateItem(item.getStringValue().trim());
            } else if (item.isDate() || item.isDateTime()) {
                convertedValue = ItemFactory.getInstance().createDateItem(item.getDateTimeValue(), item.hasTimeZone());
            }
            return finalizeAtomicBranchValue(convertedValue, targetType, BuiltinTypesCatalogue.dateItem);
        }
        if (targetType.isSubtypeOf(BuiltinTypesCatalogue.timeItem)) {
            Item convertedValue = null;
            if (item.isString() || item.isUntypedAtomic()) {
                convertedValue = ItemFactory.getInstance().createTimeItem(item.getStringValue().trim());
            } else if (item.isTime()) {
                convertedValue = ItemFactory.getInstance().createTimeItem(item.getTimeValue(), item.hasTimeZone());
            } else if (item.isDate() || item.isDateTime()) {
                convertedValue = ItemFactory.getInstance()
                    .createTimeItem(item.getDateTimeValue().toOffsetTime(), item.hasTimeZone());
            }
            return finalizeAtomicBranchValue(convertedValue, targetType, BuiltinTypesCatalogue.timeItem);
        }
        if (targetType.isSubtypeOf(BuiltinTypesCatalogue.dateTimeStampItem)) {
            Item convertedValue = null;
            if (item.isString() || item.getDynamicType().equals(BuiltinTypesCatalogue.untypedAtomicItem)) {
                convertedValue = ItemFactory.getInstance()
                    .createDateTimeStampItem(item.getStringValue().trim());
            } else if (item.isDate() || item.isDateTime()) {
                convertedValue = ItemFactory.getInstance()
                    .createDateTimeStampItem(item.getDateTimeValue(), item.hasTimeZone());
            }
            return finalizeAtomicBranchValue(convertedValue, targetType, BuiltinTypesCatalogue.dateTimeStampItem);
        }
        if (targetType.isSubtypeOf(BuiltinTypesCatalogue.dateTimeItem)) {
            Item convertedValue = null;
            if (item.isString() || item.getDynamicType().equals(BuiltinTypesCatalogue.untypedAtomicItem)) {
                convertedValue = ItemFactory.getInstance().createDateTimeItem(item.getStringValue().trim());
            } else if (item.isDate() || item.isDateTime()) {
                convertedValue = ItemFactory.getInstance()
                    .createDateTimeItem(item.getDateTimeValue(), item.hasTimeZone());
            }
            return finalizeAtomicBranchValue(convertedValue, targetType, BuiltinTypesCatalogue.dateTimeItem);
        }
        if (targetType.isSubtypeOf(BuiltinTypesCatalogue.yearMonthDurationItem)) {
            Item convertedValue = null;
            if (item.isString() || item.getDynamicType().equals(BuiltinTypesCatalogue.untypedAtomicItem)) {
                convertedValue = ItemFactory.getInstance()
                    .createYearMonthDurationItem(item.getStringValue().trim());
            } else if (item.isDuration() || item.isDayTimeDuration()) {
                convertedValue = ItemFactory.getInstance()
                    .createYearMonthDurationItem(item.getPeriodValue());
            }
            return finalizeAtomicBranchValue(
                convertedValue,
                targetType,
                BuiltinTypesCatalogue.yearMonthDurationItem
            );
        }
        if (targetType.isSubtypeOf(BuiltinTypesCatalogue.dayTimeDurationItem)) {
            Item convertedValue = null;
            if (item.isString() || item.getDynamicType().equals(BuiltinTypesCatalogue.untypedAtomicItem)) {
                convertedValue = ItemFactory.getInstance()
                    .createDayTimeDurationItem(item.getStringValue().trim());
            } else if (item.isDuration() || item.isYearMonthDuration()) {
                convertedValue = ItemFactory.getInstance()
                    .createDayTimeDurationItem(item.getDurationValue());
            }
            return finalizeAtomicBranchValue(
                convertedValue,
                targetType,
                BuiltinTypesCatalogue.dayTimeDurationItem
            );
        }
        if (targetType.isSubtypeOf(BuiltinTypesCatalogue.durationItem)) {
            Item convertedValue = null;
            if (item.isString() || item.getDynamicType().equals(BuiltinTypesCatalogue.untypedAtomicItem)) {
                convertedValue = ItemFactory.getInstance()
                    .createDurationItem(
                        item.getStringValue().trim()
                    );
            } else if (item.isDayTimeDuration()) {
                convertedValue = ItemFactory.getInstance().createDurationItem(item.getStringValue().trim());
            } else if (item.isYearMonthDuration()) {
                convertedValue = ItemFactory.getInstance().createDurationItem(item.getStringValue().trim());
            }
            return finalizeAtomicBranchValue(convertedValue, targetType, BuiltinTypesCatalogue.durationItem);
        }
        if (targetType.isSubtypeOf(BuiltinTypesCatalogue.gDayItem)) {
            Item convertedValue = null;
            if (item.isString() || item.getDynamicType().equals(BuiltinTypesCatalogue.untypedAtomicItem)) {
                convertedValue = ItemFactory.getInstance().createGDayItem(item.getStringValue().trim());
            } else if (item.isDateTime() || item.isDate()) {
                convertedValue = ItemFactory.getInstance().createGDayItem(item.getDateTimeValue(), item.hasTimeZone());
            }
            return finalizeAtomicBranchValue(convertedValue, targetType, BuiltinTypesCatalogue.gDayItem);
        }
        if (targetType.isSubtypeOf(BuiltinTypesCatalogue.gMonthItem)) {
            Item convertedValue = null;
            if (item.isString() || item.getDynamicType().equals(BuiltinTypesCatalogue.untypedAtomicItem)) {
                convertedValue = ItemFactory.getInstance().createGMonthItem(item.getStringValue().trim());
            } else if (item.isDateTime() || item.isDate()) {
                convertedValue = ItemFactory.getInstance()
                    .createGMonthItem(item.getDateTimeValue(), item.hasTimeZone());
            }
            return finalizeAtomicBranchValue(convertedValue, targetType, BuiltinTypesCatalogue.gMonthItem);
        }
        if (targetType.isSubtypeOf(BuiltinTypesCatalogue.gYearItem)) {
            Item convertedValue = null;
            if (item.isString() || item.getDynamicType().equals(BuiltinTypesCatalogue.untypedAtomicItem)) {
                convertedValue = ItemFactory.getInstance().createGYearItem(item.getStringValue().trim());
            } else if (item.isDateTime() || item.isDate()) {
                convertedValue = ItemFactory.getInstance()
                    .createGYearItem(item.getDateTimeValue(), item.hasTimeZone());
            }
            return finalizeAtomicBranchValue(convertedValue, targetType, BuiltinTypesCatalogue.gYearItem);
        }
        if (targetType.isSubtypeOf(BuiltinTypesCatalogue.gMonthDayItem)) {
            Item convertedValue = null;
            if (item.isString() || item.getDynamicType().equals(BuiltinTypesCatalogue.untypedAtomicItem)) {
                convertedValue = ItemFactory.getInstance().createGMonthDayItem(item.getStringValue().trim());
            } else if (item.isDateTime() || item.isDate()) {
                convertedValue = ItemFactory.getInstance()
                    .createGMonthDayItem(item.getDateTimeValue(), item.hasTimeZone());
            }
            return finalizeAtomicBranchValue(convertedValue, targetType, BuiltinTypesCatalogue.gMonthDayItem);
        }
        if (targetType.isSubtypeOf(BuiltinTypesCatalogue.gYearMonthItem)) {
            Item convertedValue = null;
            if (item.isString() || item.getDynamicType().equals(BuiltinTypesCatalogue.untypedAtomicItem)) {
                convertedValue = ItemFactory.getInstance().createGYearMonthItem(item.getStringValue().trim());
            } else if (item.isDateTime() || item.isDate()) {
                convertedValue = ItemFactory.getInstance()
                    .createGYearMonthItem(item.getDateTimeValue(), item.hasTimeZone());
            }
            return finalizeAtomicBranchValue(
                convertedValue,
                targetType,
                BuiltinTypesCatalogue.gYearMonthItem
            );
        }
        if (targetType.equals(BuiltinTypesCatalogue.numericItem)) {
            Item convertedValue = null;
            if (item.isString() || item.isUntypedAtomic()) {
                convertedValue = ItemFactory.getInstance().createDoubleItem(item.castToDoubleValue());
            } else if (item.isBoolean()) {
                convertedValue = ItemFactory.getInstance().createDoubleItem(item.getBooleanValue() ? 1 : 0);
            }
            return convertedValue;
        }

        // if no branch succeeded, it means no conversion needs to be carried out, so return the item as is,
        // annotated with the target type
        return new AnnotatedItem(item, targetType);
    }

    private static Item finalizeAtomicBranchValue(Item convertedValue, ItemType targetType, ItemType baseType) {
        if (convertedValue == null) {
            return null;
        }
        if (!targetType.equals(baseType)) {
            return ItemFactory.getInstance().createAnnotatedItem(convertedValue, targetType);
        }
        return convertedValue;
    }

    private static boolean checkValueConformsToTargetFacets(Item value, ItemType targetType) {
        if (targetType.isSubtypeOf(BuiltinTypesCatalogue.stringItem)) {
            return checkFacetsString(value, targetType);
        }
        if (targetType.isSubtypeOf(BuiltinTypesCatalogue.booleanItem)) {
            return checkFacetsBoolean(value, targetType);
        }
        if (targetType.isSubtypeOf(BuiltinTypesCatalogue.doubleItem)) {
            return checkFacetsDouble(value, targetType);
        }
        if (targetType.isSubtypeOf(BuiltinTypesCatalogue.floatItem)) {
            return checkFacetsFloat(value, targetType);
        }
        if (targetType.isSubtypeOf(BuiltinTypesCatalogue.integerItem)) {
            return checkFacetsInteger(value, targetType);
        }
        if (targetType.isSubtypeOf(BuiltinTypesCatalogue.decimalItem)) {
            return checkFacetsDecimal(value, targetType);
        }
        if (targetType.isSubtypeOf(BuiltinTypesCatalogue.anyURIItem)) {
            return checkFacetsAnyURI(value, targetType);
        }
        if (targetType.isSubtypeOf(BuiltinTypesCatalogue.base64BinaryItem)) {
            return checkFacetsBase64Binary(value, targetType);
        }
        if (targetType.isSubtypeOf(BuiltinTypesCatalogue.hexBinaryItem)) {
            return checkFacetsHexBinary(value, targetType);
        }
        if (targetType.equals(BuiltinTypesCatalogue.dateTimeStampItem)) {
            return checkFacetsDateTimeStamp(value, targetType);
        }
        if (targetType.isSubtypeOf(BuiltinTypesCatalogue.dateTimeItem)) {
            return checkFacetsDateTime(value, targetType);
        }
        if (targetType.isSubtypeOf(BuiltinTypesCatalogue.dateItem)) {
            return checkFacetsDate(value, targetType);
        }
        if (targetType.isSubtypeOf(BuiltinTypesCatalogue.timeItem)) {
            return checkFacetsTime(value, targetType);
        }
        if (targetType.isSubtypeOf(BuiltinTypesCatalogue.durationItem)) {
            return checkFacetsDuration(value, targetType);
        }
        if (targetType.isSubtypeOf(BuiltinTypesCatalogue.nullItem)) {
            return checkFacetsNull(value, targetType);
        }
        // For type families without modeled facet checks, accept by default.
        return true;
    }

    /**
     * Checks the pattern facet (if any) for the given target type against the lexical form of the item.
     * Returns true when either no pattern facet applies or at least one pattern matches; false otherwise.
     *
     * This uses only facet-based patterns (for example on derived types), not the
     * built-in lexical-space patterns of primitive types.
     */
    private static boolean checkPatternFacet(Item item, ItemType targetType) {
        List<String> patterns;
        try {
            patterns = targetType.getPatternFacet();
        } catch (UnsupportedOperationException e) {
            // Target type does not support the pattern facet.
            return true;
        }
        if (patterns == null || patterns.isEmpty()) {
            return true;
        }
        // F&O 3.1 §19.3.3 requires pattern checks against the canonical lexical
        // representation of the source value (or xs:string cast if no canonical form exists).
        String lexical = normalizeLexicalAccordingToWhitespace(
            item.getStringValue(),
            targetType.getCastingPrimitiveType()
        );
        for (String regex : patterns) {
            if (Pattern.matches(regex, lexical)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Normalizes a lexical string according to the whiteSpace facet of the given primitive type.
     */
    private static String normalizeLexicalAccordingToWhitespace(String lexical, ItemType primitiveType) {
        WhitespaceFacet facet = primitiveType.getWhitespaceFacet();
        if (facet == null) {
            return lexical;
        }
        switch (facet) {
            case PRESERVE:
                return lexical;
            case REPLACE:
                return lexical.replaceAll("\\s", " ");
            case COLLAPSE:
                String replaced = lexical.replaceAll("\\s", " ");
                return replaced.trim().replaceAll(" +", " ");
            default:
                return lexical;
        }
    }

    /**
     * Checks the lexical-space patterns (if any) for the given target type against the lexical form of the item.
     * Whitespace normalization is applied according to the whiteSpace facet of the target primitive type.
     * Returns true when either no lexical patterns are modeled or at least one pattern matches; false otherwise.
     */
    private static boolean checkLexicalPatterns(Item item, ItemType targetType) {
        ItemType primitive = targetType.getCastingPrimitiveType();
        java.util.List<String> patterns = primitive.getLexicalSpacePatterns();
        if (patterns == null || patterns.isEmpty()) {
            return true;
        }
        String lexical = normalizeLexicalAccordingToWhitespace(item.getStringValue(), primitive);
        for (String regex : patterns) {
            if (Pattern.matches(regex, lexical)) {
                return true;
            }
        }
        return false;
    }

    public static boolean checkFacetsInteger(Item item, ItemType targetType) {
        BigInteger value = item.castToIntegerValue();
        if (
            (targetType.getMinInclusiveFacet() != null
                && value.compareTo(targetType.getMinInclusiveFacet().castToIntegerValue()) < 0)
                || (targetType.getMaxInclusiveFacet() != null
                    && value.compareTo(targetType.getMaxInclusiveFacet().castToIntegerValue()) > 0)
                || (targetType.getMinExclusiveFacet() != null
                    &&
                    value.compareTo(targetType.getMinExclusiveFacet().castToIntegerValue()) <= 0)
                || (targetType.getMaxExclusiveFacet() != null
                    &&
                    value.compareTo(targetType.getMaxExclusiveFacet().castToIntegerValue()) >= 0)
        ) {
            return false;
        }

        return true;
    }

    public static boolean checkFacetsNull(Item item, ItemType targetType) {
        return true;
    }

    public static boolean checkFacetsString(Item item, ItemType targetType) {
        if (
            (targetType.getLengthFacet() != null && item.getStringValue().length() != targetType.getLengthFacet())
                ||
                (targetType.getMinLengthFacet() != null
                    && item.getStringValue().length() < targetType.getMinLengthFacet())
                ||
                (targetType.getMaxLengthFacet() != null
                    && item.getStringValue().length() > targetType.getMaxLengthFacet())
        ) {
            return false;
        }

        // If no enumeration facet, can directly return true
        /*
         * boolean enumCorrect;
         * if (targetType.getEnumerationFacet() != null) {
         * enumCorrect = false;
         * } else {
         * enumCorrect = true;
         * }
         * for (Item allowedItem : targetType.getEnumerationFacet()) {
         * if (item.getStringValue() == allowedItem.getStringValue()) {
         * enumCorrect = true;
         * break;
         * }
         * }
         * return enumCorrect;
         */
        return true;

    }

    public static boolean checkFacetsBoolean(Item item, ItemType targetType) {
        return true;
    }

    public static boolean checkFacetsDouble(Item item, ItemType targetType) {
        if (
            (targetType.getMinInclusiveFacet() != null
                && item.getDoubleValue() < targetType.getMinInclusiveFacet().getDoubleValue())
                || (targetType.getMaxInclusiveFacet() != null
                    && item.getDoubleValue() > targetType.getMaxInclusiveFacet().getDoubleValue())
                || (targetType.getMinExclusiveFacet() != null
                    &&
                    item.getDoubleValue() <= targetType.getMinExclusiveFacet().getDoubleValue())
                || (targetType.getMaxExclusiveFacet() != null
                    &&
                    item.getDoubleValue() >= targetType.getMaxExclusiveFacet().getDoubleValue())
        ) {
            return false;
        }

        return true;
    }

    public static boolean checkFacetsFloat(Item item, ItemType targetType) {
        if (
            (targetType.getMinInclusiveFacet() != null
                && item.getFloatValue() < targetType.getMinInclusiveFacet().getFloatValue())
                || (targetType.getMaxInclusiveFacet() != null
                    && item.getFloatValue() > targetType.getMaxInclusiveFacet().getFloatValue())
                || (targetType.getMinExclusiveFacet() != null
                    &&
                    item.getFloatValue() <= targetType.getMinExclusiveFacet().getFloatValue())
                || (targetType.getMaxExclusiveFacet() != null
                    &&
                    item.getFloatValue() >= targetType.getMaxExclusiveFacet().getFloatValue())
        ) {
            return false;
        }

        return true;
    }

    public static boolean checkFacetsDecimal(Item item, ItemType targetType) {
        if (
            (targetType.getMinInclusiveFacet() != null
                && item.getDecimalValue().compareTo(targetType.getMinInclusiveFacet().getDecimalValue()) < 0)
                || (targetType.getMaxInclusiveFacet() != null
                    && item.getDecimalValue().compareTo(targetType.getMaxInclusiveFacet().getDecimalValue()) > 0)
                || (targetType.getMinExclusiveFacet() != null
                    &&
                    item.getDecimalValue().compareTo(targetType.getMinExclusiveFacet().getDecimalValue()) <= 0)
                || (targetType.getMaxExclusiveFacet() != null
                    &&
                    item.getDecimalValue().compareTo(targetType.getMaxExclusiveFacet().getDecimalValue()) >= 0)
        ) {
            return false;
        }

        if (
            targetType.getTotalDigitsFacet() != null
                && targetType.getTotalDigitsFacet() != BuiltinTypesCatalogue.decimalItem.getTotalDigitsFacet()
        ) {
            return false;
        }

        if (
            targetType.getFractionDigitsFacet() != null
                && targetType.getFractionDigitsFacet() != BuiltinTypesCatalogue.decimalItem.getFractionDigitsFacet()
        ) {
            return false;
        }

        return true;
    }

    public static boolean checkFacetsAnyURI(Item item, ItemType targetType) {
        return checkFacetsString(item, targetType);
    }

    public static boolean checkFacetsBase64Binary(Item item, ItemType targetType) {
        return checkFacetsString(item, targetType);
    }

    public static boolean checkFacetsHexBinary(Item item, ItemType targetType) {
        return checkFacetsString(item, targetType);
    }

    public static boolean checkDateTimeMinMaxFacets(Item item, ItemType targetType) {
        // TODO: fix this that causes pipeline to fail all tests involving date/time/datetime
        if (
            (targetType.getMinInclusiveFacet() != null
                && item.getDateTimeValue().compareTo(targetType.getMinInclusiveFacet().getDateTimeValue()) < 0)
                || (targetType.getMaxInclusiveFacet() != null
                    && item.getDateTimeValue().compareTo(targetType.getMaxInclusiveFacet().getDateTimeValue()) > 0)
                || (targetType.getMinExclusiveFacet() != null
                    &&
                    item.getDateTimeValue().compareTo(targetType.getMinExclusiveFacet().getDateTimeValue()) <= 0)
                || (targetType.getMaxExclusiveFacet() != null
                    &&
                    item.getDateTimeValue().compareTo(targetType.getMaxExclusiveFacet().getDateTimeValue()) >= 0)
        ) {
            return false;
        }

        return true;
    }

    public static boolean checkFacetsDate(Item item, ItemType targetType) {
        if (!checkDateTimeMinMaxFacets(item, targetType)) {
            return false;
        }

        if (
            targetType.getExplicitTimezoneFacet() != null
                && !targetType.getExplicitTimezoneFacet()
                    .equals(BuiltinTypesCatalogue.dateItem.getExplicitTimezoneFacet())
        ) {
            return false;
        }

        return true;
    }

    public static boolean checkFacetsTime(Item item, ItemType targetType) {
        if (!checkDateTimeMinMaxFacets(item, targetType)) {
            return false;
        }

        if (
            targetType.getExplicitTimezoneFacet() != null
                && !targetType.getExplicitTimezoneFacet()
                    .equals(BuiltinTypesCatalogue.timeItem.getExplicitTimezoneFacet())
        ) {
            return false;
        }

        return true;
    }

    public static boolean checkFacetsDateTime(Item item, ItemType targetType) {
        if (!checkDateTimeMinMaxFacets(item, targetType)) {
            return false;
        }
        if (1 > item.getMonth() || item.getMonth() > 12) {
            return false;
        }
        if (1 > item.getDay() || item.getDay() > 31) {
            return false;
        }
        if (0 > item.getHour() || item.getHour() > 23) {
            return false;
        }
        if (0 > item.getMinute() || item.getMinute() > 59) {
            return false;
        }
        if (0 > item.getSecond() || item.getSecond() >= 60) {
            return false;
        }
        if (-840 > item.getOffset() || item.getOffset() > 840) {
            return false;
        }

        if (
            targetType.getExplicitTimezoneFacet() != null
                && !targetType.getExplicitTimezoneFacet()
                    .equals(BuiltinTypesCatalogue.dateTimeItem.getExplicitTimezoneFacet())
        ) {
            return false;
        }

        return true;
    }

    public static boolean checkFacetsDateTimeStamp(Item item, ItemType targetType) {
        if (!checkDateTimeMinMaxFacets(item, targetType)) {
            return false;
        }
        if (1 > item.getMonth() || item.getMonth() > 12) {
            return false;
        }
        if (1 > item.getDay() || item.getDay() > 31) {
            return false;
        }
        if (0 > item.getHour() || item.getHour() > 23) {
            return false;
        }
        if (0 > item.getMinute() || item.getMinute() > 59) {
            return false;
        }
        if (0 > item.getSecond() || item.getSecond() >= 60) {
            return false;
        }
        if (-840 > item.getOffset() || item.getOffset() > 840) {
            return false;
        }

        if (
            targetType.getExplicitTimezoneFacet() != null
                && !targetType.getExplicitTimezoneFacet()
                    .equals(BuiltinTypesCatalogue.dateTimeStampItem.getExplicitTimezoneFacet())
        ) {
            return false;
        }

        return true;
    }

    public static boolean checkFacetsDuration(Item item, ItemType targetType) {
        if (targetType.equals(BuiltinTypesCatalogue.durationItem)) {
            if (item.getMonth() < 0 && item.getSecond() > 0) {
                return false;
            }
            if (item.getMonth() > 0 && item.getSecond() < 0) {
                return false;
            }
        }
        return true;
        // * TODO: fix this that causes pipeline to fail all tests involving duration
        // Period itemPeriod = item.getPeriodValue();
        // Duration itemDuration = Duration.ofDays(itemPeriod.toTotalMonths() * 30 + itemPeriod.getDays());
        //
        // return (targetType.getMinInclusiveFacet() == null
        // || itemDuration.compareTo(
        // Duration.ofDays(
        // targetType.getMinInclusiveFacet().getPeriodValue().toTotalMonths() * 30
        // + targetType.getMinInclusiveFacet().getPeriodValue().getDays()
        // )
        // ) >= 0)
        // && (targetType.getMaxInclusiveFacet() == null
        // || itemDuration.compareTo(
        // Duration.ofDays(
        // targetType.getMaxInclusiveFacet().getPeriodValue().toTotalMonths() * 30
        // + targetType.getMaxInclusiveFacet().getPeriodValue().getDays()
        // )
        // ) <= 0)
        // && (targetType.getMinExclusiveFacet() == null
        // || itemDuration.compareTo(
        // Duration.ofDays(
        // targetType.getMinExclusiveFacet().getPeriodValue().toTotalMonths() * 30
        // + targetType.getMinExclusiveFacet().getPeriodValue().getDays()
        // )
        // ) > 0)
        // && (targetType.getMaxExclusiveFacet() == null
        // || itemDuration.compareTo(
        // Duration.ofDays(
        // targetType.getMaxExclusiveFacet().getPeriodValue().toTotalMonths() * 30
        // + targetType.getMaxExclusiveFacet().getPeriodValue().getDays()
        // )
        // ) < 0);
        //
    }

    @Override
    public NativeClauseContext generateNativeQuery(NativeClauseContext nativeClauseContext) {
        NativeClauseContext childQuery = this.child.generateNativeQuery(nativeClauseContext);
        if (childQuery == NativeClauseContext.NoNativeQuery) {
            return NativeClauseContext.NoNativeQuery;
        }
        if (this.sequenceType.getItemType().isSubtypeOf(BuiltinTypesCatalogue.floatItem)) {
            return new NativeClauseContext(
                    childQuery,
                    "CAST (" + childQuery.getResultingQuery() + " AS FLOAT)",
                    new SequenceType(BuiltinTypesCatalogue.floatItem, childQuery.getResultingType().getArity())
            );
        }
        if (this.sequenceType.getItemType().isSubtypeOf(BuiltinTypesCatalogue.stringItem)) {
            return new NativeClauseContext(
                    childQuery,
                    "CAST (" + childQuery.getResultingQuery() + " AS STRING)",
                    new SequenceType(BuiltinTypesCatalogue.stringItem, childQuery.getResultingType().getArity())
            );
        }
        if (this.sequenceType.getItemType().isSubtypeOf(BuiltinTypesCatalogue.doubleItem)) {
            return new NativeClauseContext(
                    childQuery,
                    "CAST (" + childQuery.getResultingQuery() + " AS DOUBLE)",
                    new SequenceType(BuiltinTypesCatalogue.doubleItem, childQuery.getResultingType().getArity())
            );
        }
        return NativeClauseContext.NoNativeQuery;
    }
}

