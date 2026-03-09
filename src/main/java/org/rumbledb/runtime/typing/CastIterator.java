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
        // the target type cannot be xs:NOTATION, xs:anySimpleType, or xs:anyAtomicType
        // TODO: add support for xs:anySimpleType
        if (targetType.equals(BuiltinTypesCatalogue.NOTATIONItem)) {
            throw new CastableException("Invalid target type for cast expression: xs:NOTATION", metadata);
        }
        if (targetType.equals(BuiltinTypesCatalogue.atomicItem)) {
            throw new CastableException("Invalid target type for cast expression: xs:anyAtomicType", metadata);
        }

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
            } catch (FunctionAtomizationException e) {
                // need to add metadata, e has no metadata
                RumbleException castE = new FunctionAtomizationException(
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
            boolean fromStringSource =
                item.isUntypedAtomic()
                    || item.isString()
                    || item.getDynamicType().getPrimitiveType().equals(BuiltinTypesCatalogue.stringItem);

            // XPath and XQuery F&O 3.1 §19.3.1:
            // "When ST is the same type as TT: this case always succeeds, returning SV unchanged."
            if (itemType.equals(targetType)) {
                return item;
            }
            // XPath and XQuery F&O 3.1 §19.3.1:
            // "When itemType-subtype(ST, TT) is true: This case is described in 19.3.2
            // Casting from derived types to parent types."
            if (itemType.isSubtypeOf(targetType)) {
                // TODO: handle the case for union types
                return item;
            }
            ItemType sourcePrimitiveType = itemType.isPrimitive() ? itemType : itemType.getPrimitiveType();
            ItemType targetPrimitiveType = targetType.isPrimitive() ? targetType : targetType.getPrimitiveType();
            // XPath and XQuery F&O 3.1 §19.3.1: Casting to derived types
            // - "When P(ST) is the same type as P(TT)" (19.3.3)
            if (!targetType.isPrimitive() && sourcePrimitiveType.equals(targetPrimitiveType)) {
                // Keep the dedicated conversion paths for duration subtypes and ENTITY-family casts:
                // those require special conversion rules beyond plain facet validation.
                boolean hasSpecialWithinBranchRules =
                    targetType.equals(BuiltinTypesCatalogue.yearMonthDurationItem)
                        || targetType.equals(BuiltinTypesCatalogue.dayTimeDurationItem)
                        || targetType.isSubtypeOf(BuiltinTypesCatalogue.ENTITYItem);
                if (!hasSpecialWithinBranchRules) {
                    // F&O 3.1 §19.3.3:
                    // if ST and TT share the same primitive type, the cast succeeds iff the value
                    // conforms to the target type's facets.
                    if (!checkValueConformsToTargetFacets(item, targetType)) {
                        return null;
                    }
                    return new AnnotatedItem(item, targetType);
                }
            }
            // Remaining successful casts are handled by the target-type conversions below and
            // correspond to §19.3.1 either:
            // - "Otherwise (P(ST) is not the same type as P(TT))" (19.3.4).
            if (!targetType.isPrimitive() && !sourcePrimitiveType.equals(targetPrimitiveType)) {
                // XPath and XQuery F&O 3.1 §19.3.4:
                // 1) cast up to primitive source type
                // 2) cast to primitive target type
                // 3) cast down to final target type
                // with an additional pattern-facet check for string/untypedAtomic sources.
                if (fromStringSource && !checkPatternFacet(item, targetType)) {
                    return null;
                }

                Item sourcePrimitiveValue = itemType.isPrimitive()
                    ? item
                    : castToPrimitiveTypes(item, sourcePrimitiveType, metadata);
                if (sourcePrimitiveValue == null) {
                    return null;
                }

                Item targetPrimitiveValue;
                if (targetType.isSubtypeOf(BuiltinTypesCatalogue.NOTATIONItem)) {
                    // F&O §19.3.4 step 2: when TT is derived from xs:NOTATION, assume cast to
                    // xs:NOTATION succeeds for this logical step.
                    targetPrimitiveValue = sourcePrimitiveValue;
                } else {
                    targetPrimitiveValue = castToPrimitiveTypes(sourcePrimitiveValue, targetPrimitiveType, metadata);
                    if (targetPrimitiveValue == null) {
                        return null;
                    }
                }

                if (!checkValueConformsToTargetFacets(targetPrimitiveValue, targetType)) {
                    return null;
                }
                return new AnnotatedItem(targetPrimitiveValue, targetType);
            }

            if (targetType.equals(BuiltinTypesCatalogue.untypedAtomicItem)) {
                return ItemFactory.getInstance().createUntypedAtomicItem(item.getStringValue());
            }
            if (targetType.isPrimitive()) {
                return castToPrimitiveTypes(item, targetType, metadata);
            }

            boolean directDerivedConversion =
                targetType.equals(BuiltinTypesCatalogue.yearMonthDurationItem)
                    || targetType.equals(BuiltinTypesCatalogue.dayTimeDurationItem);
            ItemType conversionTargetType = directDerivedConversion ? targetType : targetPrimitiveType;
            Item converted = castToPrimitiveTypes(item, conversionTargetType, metadata);
            if (converted == null) {
                return null;
            }

            // xs:numeric is a union type and does not use facet-based annotation.
            if (targetType.equals(BuiltinTypesCatalogue.numericItem)) {
                return converted;
            }

            if (!checkValueConformsToTargetFacets(converted, targetType)) {
                return null;
            }

            if (targetType.isSubtypeOf(BuiltinTypesCatalogue.intItem)) {
                // Keep historical materialization to IntItem for the xs:int family.
                converted = ItemFactory.getInstance().createIntItem(converted.castToIntValue());
            }

            if (
                targetType.equals(BuiltinTypesCatalogue.yearMonthDurationItem)
                    || targetType.equals(BuiltinTypesCatalogue.dayTimeDurationItem)
                    || targetType.equals(BuiltinTypesCatalogue.intItem)
            ) {
                return converted;
            }
            return new AnnotatedItem(converted, targetType);
        } catch (DatetimeOverflowOrUnderflow | DurationOverflowOrUnderflow | InvalidLexicalValueException e) {
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

    private static Item castToPrimitiveTypes(Item item, ItemType targetPrimitiveType, ExceptionMetadata metadata) {
        Item result = null;
        ItemType sourceType = item.getDynamicType();
        if (sourceType.equals(targetPrimitiveType)) {
            return item;
        }
        if (targetPrimitiveType.equals(BuiltinTypesCatalogue.untypedAtomicItem)) {
            return ItemFactory.getInstance().createUntypedAtomicItem(item.getStringValue());
        }
        if (targetPrimitiveType.equals(BuiltinTypesCatalogue.nullItem)) {
            if (item.isString() && item.getStringValue().trim().equals("null")) {
                return ItemFactory.getInstance().createNullItem();
            }
            return null;
        }

        boolean fromStringSource =
            item.isUntypedAtomic()
                || item.isString()
                || sourceType.getPrimitiveType().equals(BuiltinTypesCatalogue.stringItem);
        if (fromStringSource && !checkLexicalPatterns(item, targetPrimitiveType)) {
            return null;
        }

        if (targetPrimitiveType.equals(BuiltinTypesCatalogue.stringItem)) {
            return ItemFactory.getInstance().createStringItem(item.getStringValue());
        }
        if (targetPrimitiveType.equals(BuiltinTypesCatalogue.booleanItem)) {
            if (item.isString() || item.isUntypedAtomic()) {
                String lexical = item.getStringValue().trim();
                if ("true".equals(lexical) || "1".equals(lexical)) {
                    return ItemFactory.getInstance().createBooleanItem(true);
                }
                if ("false".equals(lexical) || "0".equals(lexical)) {
                    return ItemFactory.getInstance().createBooleanItem(false);
                }
                return null;
            }
            if (item.isInt()) {
                return ItemFactory.getInstance().createBooleanItem(item.getIntValue() != 0);
            }
            if (item.isInteger()) {
                return ItemFactory.getInstance()
                    .createBooleanItem(!item.getIntegerValue().equals(BigInteger.ZERO));
            }
            if (item.isDecimal()) {
                return ItemFactory.getInstance()
                    .createBooleanItem(item.getDecimalValue().compareTo(BigDecimal.ZERO) != 0);
            }
            if (item.isDouble()) {
                double value = item.getDoubleValue();
                boolean booleanValue = !(Double.isNaN(value) || value == 0.0d);
                return ItemFactory.getInstance().createBooleanItem(booleanValue);
            }
            if (item.isFloat()) {
                float value = item.getFloatValue();
                boolean booleanValue = !(Float.isNaN(value) || value == 0.0f);
                return ItemFactory.getInstance().createBooleanItem(booleanValue);
            }
            return null;
        }
        if (targetPrimitiveType.equals(BuiltinTypesCatalogue.doubleItem)) {
            if (item.isString() || item.isUntypedAtomic()) {
                return ItemFactory.getInstance().createDoubleItem(item.castToDoubleValue());
            }
            if (item.isBoolean()) {
                return ItemFactory.getInstance().createDoubleItem(item.getBooleanValue() ? 1 : 0);
            }
            if (item.isNumeric()) {
                return ItemFactory.getInstance().createDoubleItem(item.castToDoubleValue());
            }
            return null;
        }
        if (targetPrimitiveType.equals(BuiltinTypesCatalogue.floatItem)) {
            if (item.isString() || item.isUntypedAtomic()) {
                return ItemFactory.getInstance().createFloatItem(item.castToFloatValue());
            }
            if (item.isBoolean()) {
                return ItemFactory.getInstance().createFloatItem(item.getBooleanValue() ? 1 : 0);
            }
            if (item.isNumeric()) {
                return ItemFactory.getInstance().createFloatItem(item.castToFloatValue());
            }
            return null;
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

        if (targetPrimitiveType.equals(BuiltinTypesCatalogue.integerItem)) {
            if (item.isString() || item.isUntypedAtomic()) {
                return ItemFactory.getInstance().createIntegerItem(item.castToIntegerValue());
            }
            if (item.isBoolean()) {
                return ItemFactory.getInstance()
                    .createIntegerItem(item.getBooleanValue() ? BigInteger.ONE : BigInteger.ZERO);
            }
            if (item.isNumeric()) {
                return ItemFactory.getInstance().createIntegerItem(item.castToIntegerValue());
            }
            return null;
        }
        if (targetPrimitiveType.equals(BuiltinTypesCatalogue.decimalItem)) {
            if (item.isString() || item.isUntypedAtomic()) {
                return ItemFactory.getInstance().createDecimalItem(item.castToDecimalValue());
            }
            if (item.isBoolean()) {
                return ItemFactory.getInstance()
                    .createDecimalItem(item.getBooleanValue() ? BigDecimal.ONE : BigDecimal.ZERO);
            }
            if (item.isNumeric()) {
                return ItemFactory.getInstance().createDecimalItem(item.castToDecimalValue());
            }
            return null;
        }
        if (targetPrimitiveType.equals(BuiltinTypesCatalogue.anyURIItem)) {
            if (item.isString() || item.isUntypedAtomic()) {
                return ItemFactory.getInstance().createAnyURIItem(item.getStringValue().trim());
            }
            return null;
        }
        if (targetPrimitiveType.equals(BuiltinTypesCatalogue.base64BinaryItem)) {
            if (item.isString() || item.isUntypedAtomic()) {
                return ItemFactory.getInstance().createBase64BinaryItem(item.getStringValue().trim());
            }
            if (item.isHexBinary()) {
                return ItemFactory.getInstance()
                    .createBase64BinaryItem(Base64.encodeBase64String(item.getBinaryValue()));
            }
            return null;
        }
        if (targetPrimitiveType.equals(BuiltinTypesCatalogue.hexBinaryItem)) {
            if (item.isString() || item.isUntypedAtomic()) {
                return ItemFactory.getInstance().createHexBinaryItem(item.getStringValue().trim());
            }
            if (item.isBase64Binary()) {
                return ItemFactory.getInstance().createHexBinaryItem(Hex.encodeHexString(item.getBinaryValue()));
            }
            return null;
        }
        if (targetPrimitiveType.equals(BuiltinTypesCatalogue.dateItem)) {
            if (item.isString() || item.isUntypedAtomic()) {
                return ItemFactory.getInstance().createDateItem(item.getStringValue().trim());
            }
            if (item.isDate() || item.isDateTime()) {
                return ItemFactory.getInstance().createDateItem(item.getDateTimeValue(), item.hasTimeZone());
            }
            return null;
        }
        if (targetPrimitiveType.equals(BuiltinTypesCatalogue.timeItem)) {
            if (item.isString() || item.isUntypedAtomic()) {
                return ItemFactory.getInstance().createTimeItem(item.getStringValue().trim());
            }
            if (item.isTime()) {
                return ItemFactory.getInstance().createTimeItem(item.getTimeValue(), item.hasTimeZone());
            }
            if (item.isDate() || item.isDateTime()) {
                return ItemFactory.getInstance()
                    .createTimeItem(item.getDateTimeValue().toOffsetTime(), item.hasTimeZone());
            }
            return null;
        }
        if (targetPrimitiveType.equals(BuiltinTypesCatalogue.dateTimeItem)) {
            if (item.isString() || item.getDynamicType().equals(BuiltinTypesCatalogue.untypedAtomicItem)) {
                return ItemFactory.getInstance().createDateTimeItem(item.getStringValue().trim());
            }
            if (item.isDate() || item.isDateTime()) {
                return ItemFactory.getInstance().createDateTimeItem(item.getDateTimeValue(), item.hasTimeZone());
            }
            return null;
        }
        if (targetPrimitiveType.equals(BuiltinTypesCatalogue.dateTimeStampItem)) {
            if (item.isString() || item.getDynamicType().equals(BuiltinTypesCatalogue.untypedAtomicItem)) {
                return ItemFactory.getInstance().createDateTimeStampItem(item.getStringValue().trim());
            }
            if (item.isDate() || item.isDateTime()) {
                return ItemFactory.getInstance()
                    .createDateTimeStampItem(item.getDateTimeValue(), item.hasTimeZone());
            }
            return null;
        }
        if (targetPrimitiveType.equals(BuiltinTypesCatalogue.yearMonthDurationItem)) {
            if (item.isString() || item.getDynamicType().equals(BuiltinTypesCatalogue.untypedAtomicItem)) {
                return ItemFactory.getInstance().createYearMonthDurationItem(item.getStringValue().trim());
            }
            if (item.isDuration() || item.isDayTimeDuration()) {
                return ItemFactory.getInstance().createYearMonthDurationItem(item.getPeriodValue());
            }
            return null;
        }
        if (targetPrimitiveType.equals(BuiltinTypesCatalogue.dayTimeDurationItem)) {
            if (item.isString() || item.getDynamicType().equals(BuiltinTypesCatalogue.untypedAtomicItem)) {
                return ItemFactory.getInstance().createDayTimeDurationItem(item.getStringValue().trim());
            }
            if (item.isDuration() || item.isYearMonthDuration()) {
                return ItemFactory.getInstance().createDayTimeDurationItem(item.getDurationValue());
            }
            return null;
        }
        if (targetPrimitiveType.equals(BuiltinTypesCatalogue.durationItem)) {
            if (item.isString() || item.getDynamicType().equals(BuiltinTypesCatalogue.untypedAtomicItem)) {
                return ItemFactory.getInstance()
                    .createDurationItem(
                        item.getStringValue().trim()
                    );
            }
            if (item.isDayTimeDuration()) {
                return ItemFactory.getInstance().createDurationItem(item.getDurationValue());
            }
            if (item.isYearMonthDuration()) {
                return ItemFactory.getInstance().createDurationItem(item.getDurationValue());
            }
            return null;
        }
        if (targetPrimitiveType.equals(BuiltinTypesCatalogue.gDayItem)) {
            if (item.isString() || item.getDynamicType().equals(BuiltinTypesCatalogue.untypedAtomicItem)) {
                return ItemFactory.getInstance().createGDayItem(item.getStringValue().trim());
            }
            if (item.isDateTime() || item.isDate()) {
                return ItemFactory.getInstance().createGDayItem(item.getDateTimeValue(), item.hasTimeZone());
            }
            return null;
        }
        if (targetPrimitiveType.equals(BuiltinTypesCatalogue.gMonthItem)) {
            if (item.isString() || item.getDynamicType().equals(BuiltinTypesCatalogue.untypedAtomicItem)) {
                return ItemFactory.getInstance().createGMonthItem(item.getStringValue().trim());
            }
            if (item.isDateTime() || item.isDate()) {
                return ItemFactory.getInstance().createGMonthItem(item.getDateTimeValue(), item.hasTimeZone());
            }
            return null;
        }
        if (targetPrimitiveType.equals(BuiltinTypesCatalogue.gYearItem)) {
            if (item.isString() || item.getDynamicType().equals(BuiltinTypesCatalogue.untypedAtomicItem)) {
                return ItemFactory.getInstance().createGYearItem(item.getStringValue().trim());
            }
            if (item.isDateTime() || item.isDate()) {
                return ItemFactory.getInstance().createGYearItem(item.getDateTimeValue(), item.hasTimeZone());
            }
            return null;
        }
        if (targetPrimitiveType.equals(BuiltinTypesCatalogue.gMonthDayItem)) {
            if (item.isString() || item.getDynamicType().equals(BuiltinTypesCatalogue.untypedAtomicItem)) {
                return ItemFactory.getInstance().createGMonthDayItem(item.getStringValue().trim());
            }
            if (item.isDateTime() || item.isDate()) {
                return ItemFactory.getInstance().createGMonthDayItem(item.getDateTimeValue(), item.hasTimeZone());
            }
            return null;
        }
        if (targetPrimitiveType.equals(BuiltinTypesCatalogue.gYearMonthItem)) {
            if (item.isString() || item.getDynamicType().equals(BuiltinTypesCatalogue.untypedAtomicItem)) {
                return ItemFactory.getInstance().createGYearMonthItem(item.getStringValue().trim());
            }
            if (item.isDateTime() || item.isDate()) {
                return ItemFactory.getInstance()
                    .createGYearMonthItem(item.getDateTimeValue(), item.hasTimeZone());
            }
            return null;
        }
        if (targetPrimitiveType.equals(BuiltinTypesCatalogue.numericItem)) {
            if (item.isString() || item.isUntypedAtomic()) {
                return ItemFactory.getInstance().createDoubleItem(item.castToDoubleValue());
            }
            if (item.isBoolean()) {
                return ItemFactory.getInstance().createDoubleItem(item.getBooleanValue() ? 1 : 0);
            }
            return null;
        }
        return result;
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
        String lexical = item.getStringValue();
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
        ItemType primitive = targetType.getPrimitiveType();
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
        if (!checkPatternFacet(item, targetType)) {
            return false;
        }
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
        if (!checkPatternFacet(item, targetType)) {
            return false;
        }
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
        if (!checkPatternFacet(item, targetType)) {
            return false;
        }
        return true;
    }

    public static boolean checkFacetsDouble(Item item, ItemType targetType) {
        if (!checkPatternFacet(item, targetType)) {
            return false;
        }
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
        if (!checkPatternFacet(item, targetType)) {
            return false;
        }
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
        if (!checkPatternFacet(item, targetType)) {
            return false;
        }
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
        if (!checkPatternFacet(item, targetType)) {
            return false;
        }
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
        if (!checkPatternFacet(item, targetType)) {
            return false;
        }
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
        if (!checkPatternFacet(item, targetType)) {
            return false;
        }
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
        if (!checkPatternFacet(item, targetType)) {
            return false;
        }
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
        if (!checkPatternFacet(item, targetType)) {
            return false;
        }
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

