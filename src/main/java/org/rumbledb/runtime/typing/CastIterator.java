package org.rumbledb.runtime.typing;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;
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
import org.rumbledb.types.SequenceType.Arity;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collections;
import java.util.List;


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
        if (!this.sequenceType.getItemType().isAtomicItemType()) {
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
            Item result = null;
            ItemType itemType = item.getDynamicType();

            if (itemType.isSubtypeOf(targetType)) {
                return item;
            }

            if (targetType.isSubtypeOf(BuiltinTypesCatalogue.nullItem)) {
                if (item.isString() && item.getStringValue().trim().equals("null")) {
                    result = ItemFactory.getInstance().createNullItem();
                } else {
                    return null;
                }
                if (!checkFacetsNull(result, targetType)) {
                    return null;
                }
                if (targetType.equals(BuiltinTypesCatalogue.nullItem)) {
                    return result;
                }
                return new AnnotatedItem(result, targetType);
            }

            if (targetType.isSubtypeOf(BuiltinTypesCatalogue.stringItem)) {
                result = ItemFactory.getInstance().createStringItem(item.getStringValue());
                if (targetType.equals(BuiltinTypesCatalogue.stringItem)) {
                    return result;
                }
                if (!checkFacetsString(result, targetType)) {
                    return null;
                }
                return new AnnotatedItem(result, targetType);
            }

            if (targetType.isSubtypeOf(BuiltinTypesCatalogue.booleanItem)) {
                if (item.isString()) {
                    if (StringUtils.isNumeric(item.getStringValue())) {
                        result = ItemFactory.getInstance().createBooleanItem(item.castToIntValue() != 0);
                    } else {
                        result = ItemFactory.getInstance()
                            .createBooleanItem(Boolean.parseBoolean(item.getStringValue().trim()));
                    }
                } else if (item.isInt()) {
                    result = ItemFactory.getInstance().createBooleanItem(item.getIntValue() != 0);
                } else if (item.isInteger()) {
                    result = ItemFactory.getInstance()
                        .createBooleanItem(!item.getIntegerValue().equals(BigInteger.ZERO));
                } else if (item.isDecimal()) {
                    result = ItemFactory.getInstance()
                        .createBooleanItem(!item.getDecimalValue().equals(BigDecimal.ZERO));
                } else if (item.isDouble()) {
                    result = ItemFactory.getInstance().createBooleanItem(item.getDoubleValue() != 0);
                } else if (item.isFloat()) {
                    result = ItemFactory.getInstance().createBooleanItem(item.getFloatValue() != 0);
                } else {
                    return null;
                }
                if (!checkFacetsBoolean(result, targetType)) {
                    return null;
                }
                if (targetType.equals(BuiltinTypesCatalogue.booleanItem)) {
                    return result;
                }
                return new AnnotatedItem(result, targetType);
            }

            if (targetType.isSubtypeOf(BuiltinTypesCatalogue.doubleItem)) {
                if (item.isString()) {
                    result = ItemFactory.getInstance().createDoubleItem(item.castToDoubleValue());
                } else if (item.isBoolean()) {
                    result = ItemFactory.getInstance().createDoubleItem(item.getBooleanValue() ? 1 : 0);
                } else if (item.isNumeric()) {
                    result = ItemFactory.getInstance().createDoubleItem(item.castToDoubleValue());
                } else {
                    return null;
                }
                if (!checkFacetsDouble(result, targetType)) {
                    return null;
                }
                if (targetType.equals(BuiltinTypesCatalogue.doubleItem)) {
                    return result;
                }
                return new AnnotatedItem(result, targetType);
            }
            if (targetType.isSubtypeOf(BuiltinTypesCatalogue.floatItem)) {
                if (item.isString()) {
                    result = ItemFactory.getInstance().createFloatItem(item.castToFloatValue());
                } else if (item.isBoolean()) {
                    result = ItemFactory.getInstance().createFloatItem(item.getBooleanValue() ? 1 : 0);
                } else if (item.isNumeric()) {
                    result = ItemFactory.getInstance().createFloatItem(item.castToFloatValue());
                } else {
                    return null;
                }
                if (!checkFacetsFloat(result, targetType)) {
                    return null;
                }
                if (targetType.equals(BuiltinTypesCatalogue.floatItem)) {
                    return result;
                }
                return new AnnotatedItem(result, targetType);
            }

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

            if (targetType.isSubtypeOf(BuiltinTypesCatalogue.integerItem)) {
                if (item.isString()) {
                    result = ItemFactory.getInstance().createIntegerItem(item.castToIntegerValue());
                } else if (item.isBoolean()) {
                    result = ItemFactory.getInstance()
                        .createIntegerItem(item.getBooleanValue() ? BigInteger.ONE : BigInteger.ZERO);
                } else if (item.isNumeric()) {
                    result = ItemFactory.getInstance().createIntegerItem(item.castToIntegerValue());
                } else {
                    return null;
                }
                if (!checkFacetsInteger(result, targetType)) {
                    return null;
                }
                if (targetType.equals(BuiltinTypesCatalogue.integerItem)) {
                    return result;
                }
                if (targetType.isSubtypeOf(BuiltinTypesCatalogue.intItem)) {
                    result = ItemFactory.getInstance().createIntItem(item.castToIntValue());
                    if (targetType.equals(BuiltinTypesCatalogue.intItem)) {
                        return result;
                    }
                }
                return new AnnotatedItem(result, targetType);
            }

            if (targetType.isSubtypeOf(BuiltinTypesCatalogue.decimalItem)) {
                if (item.isString()) {
                    result = ItemFactory.getInstance().createDecimalItem(item.castToDecimalValue());
                } else if (item.isBoolean()) {
                    result = ItemFactory.getInstance()
                        .createDecimalItem(item.getBooleanValue() ? BigDecimal.ONE : BigDecimal.ZERO);
                } else if (item.isNumeric()) {
                    result = ItemFactory.getInstance().createDecimalItem(item.castToDecimalValue());
                } else {
                    return null;
                }
                if (!checkFacetsDecimal(result, targetType)) {
                    return null;
                }
                if (targetType.equals(BuiltinTypesCatalogue.decimalItem)) {
                    return result;
                }
                return new AnnotatedItem(result, targetType);
            }

            if (targetType.isSubtypeOf(BuiltinTypesCatalogue.anyURIItem)) {
                if (item.isString()) {
                    result = ItemFactory.getInstance().createAnyURIItem(item.getStringValue().trim());
                } else {
                    return null;
                }
                if (!checkFacetsAnyURI(result, targetType)) {
                    return null;
                }
                if (targetType.equals(BuiltinTypesCatalogue.anyURIItem)) {
                    return result;
                }
                return new AnnotatedItem(result, targetType);
            }

            if (targetType.isSubtypeOf(BuiltinTypesCatalogue.base64BinaryItem)) {
                if (item.isString()) {
                    result = ItemFactory.getInstance().createBase64BinaryItem(item.getStringValue().trim());
                } else if (item.isHexBinary()) {
                    result = ItemFactory.getInstance()
                        .createBase64BinaryItem(Base64.encodeBase64String(item.getBinaryValue()));
                } else {
                    return null;
                }
                if (!checkFacetsBase64Binary(result, targetType)) {
                    return null;
                }
                if (targetType.equals(BuiltinTypesCatalogue.base64BinaryItem)) {
                    return result;
                }
                return new AnnotatedItem(result, targetType);
            }

            if (targetType.isSubtypeOf(BuiltinTypesCatalogue.hexBinaryItem)) {
                if (item.isString()) {
                    result = ItemFactory.getInstance().createHexBinaryItem(item.getStringValue().trim());
                } else if (item.isBase64Binary()) {
                    result = ItemFactory.getInstance().createHexBinaryItem(Hex.encodeHexString(item.getBinaryValue()));
                } else {
                    return null;
                }
                if (!checkFacetsHexBinary(result, targetType)) {
                    return null;
                }
                if (targetType.equals(BuiltinTypesCatalogue.hexBinaryItem)) {
                    return result;
                }
                return new AnnotatedItem(result, targetType);
            }

            if (targetType.equals(BuiltinTypesCatalogue.dateItem)) {
                if (item.isString()) {
                    result = ItemFactory.getInstance().createDateItem(item.getStringValue().trim());
                } else if (item.isDate() || item.isDateTime()) {
                    result = ItemFactory.getInstance()
                        .createDateItem(item.getDateTimeValue(), item.hasTimeZone());
                } else {
                    return null;
                }
                if (!checkFacetsDate(result, targetType)) {
                    return null;
                }
                if (targetType.equals(BuiltinTypesCatalogue.dateItem)) {
                    return result;
                }
                return new AnnotatedItem(result, targetType);
            }
            if (targetType.equals(BuiltinTypesCatalogue.timeItem)) {
                if (item.isString()) {
                    result = ItemFactory.getInstance().createTimeItem(item.getStringValue().trim());
                } else if (item.isTime()) {
                    result = ItemFactory.getInstance().createTimeItem(item.getTimeValue(), item.hasTimeZone());
                } else if (item.isDate() || item.isDateTime()) {
                    result = ItemFactory.getInstance()
                        .createTimeItem(item.getDateTimeValue().toOffsetTime(), item.hasTimeZone());
                } else {
                    return null;
                }
                if (!checkFacetsTime(result, targetType)) {
                    return null;
                }
                if (targetType.equals(BuiltinTypesCatalogue.timeItem)) {
                    return result;
                }
                return new AnnotatedItem(result, targetType);
            }
            if (targetType.equals(BuiltinTypesCatalogue.dateTimeItem)) {
                if (item.isString()) {
                    result = ItemFactory.getInstance().createDateTimeItem(item.getStringValue().trim());
                } else if (item.isDate() || item.isDateTime()) {
                    result = ItemFactory.getInstance().createDateTimeItem(item.getDateTimeValue(), item.hasTimeZone());
                } else {
                    return null;
                }
                if (!checkFacetsDateTime(result, targetType)) {
                    return null;
                }
                if (targetType.equals(BuiltinTypesCatalogue.dateTimeItem)) {
                    return result;
                }
                return new AnnotatedItem(result, targetType);
            }
            if (targetType.equals(BuiltinTypesCatalogue.dateTimeStampItem)) {
                if (item.isString()) {
                    result = ItemFactory.getInstance().createDateTimeStampItem(item.getStringValue().trim());
                } else if (item.isDate() || item.isDateTime()) {
                    result = ItemFactory.getInstance()
                        .createDateTimeStampItem(item.getDateTimeValue(), item.hasTimeZone());
                } else {
                    return null;
                }
                if (!checkFacetsDateTimeStamp(result, targetType)) {
                    return null;
                }
                if (targetType.equals(BuiltinTypesCatalogue.dateTimeStampItem)) {
                    return result;
                }
                return new AnnotatedItem(result, targetType);
            }
            if (targetType.equals(BuiltinTypesCatalogue.yearMonthDurationItem)) {
                if (item.isString()) {
                    return ItemFactory.getInstance().createYearMonthDurationItem(item.getStringValue().trim());
                } else if (item.isDuration() || item.isDayTimeDuration()) {
                    result = ItemFactory.getInstance().createYearMonthDurationItem(item.getPeriodValue());
                } else {
                    return null;
                }
                if (!checkFacetsDuration(result, targetType)) {
                    return null;
                }
                if (targetType.equals(BuiltinTypesCatalogue.yearMonthDurationItem)) {
                    return result;
                }
                return new AnnotatedItem(result, targetType);
            }
            if (targetType.equals(BuiltinTypesCatalogue.dayTimeDurationItem)) {
                if (item.isString()) {
                    result = ItemFactory.getInstance().createDayTimeDurationItem(item.getStringValue().trim());
                } else if (item.isDuration() || item.isYearMonthDuration()) {
                    result = ItemFactory.getInstance().createDayTimeDurationItem(item.getDurationValue());
                } else {
                    return null;
                }
                if (!checkFacetsDuration(result, targetType)) {
                    return null;
                }
                if (targetType.equals(BuiltinTypesCatalogue.dayTimeDurationItem)) {
                    return result;
                }
                return new AnnotatedItem(result, targetType);
            }
            if (targetType.equals(BuiltinTypesCatalogue.durationItem)) {
                if (item.isString()) {
                    result = ItemFactory.getInstance()
                        .createDurationItem(
                            item.getStringValue().trim()
                        );
                } else if (item.isDayTimeDuration()) {
                    result = ItemFactory.getInstance().createDurationItem(item.getDurationValue());
                } else if (item.isYearMonthDuration()) {
                    return ItemFactory.getInstance().createDurationItem(item.getDurationValue());
                } else {
                    return null;
                }
                if (targetType.equals(BuiltinTypesCatalogue.durationItem)) {
                    return result;
                }
                return new AnnotatedItem(result, targetType);
            }

            if (targetType.equals(BuiltinTypesCatalogue.gDayItem)) {
                if (item.isString()) {
                    result = ItemFactory.getInstance().createGDayItem(item.getStringValue().trim());
                } else if (item.isDateTime() || item.isDate()) {
                    result = ItemFactory.getInstance().createGDayItem(item.getDateTimeValue(), item.hasTimeZone());
                } else {
                    return null;
                }
                if (targetType.equals(BuiltinTypesCatalogue.gDayItem)) {
                    return result;
                }
                return new AnnotatedItem(result, targetType);
            }
            if (targetType.equals(BuiltinTypesCatalogue.gMonthItem)) {
                if (item.isString()) {
                    result = ItemFactory.getInstance().createGMonthItem(item.getStringValue().trim());
                } else if (item.isDateTime() || item.isDate()) {
                    result = ItemFactory.getInstance().createGMonthItem(item.getDateTimeValue(), item.hasTimeZone());
                } else {
                    return null;
                }
                if (targetType.equals(BuiltinTypesCatalogue.gMonthItem)) {
                    return result;
                }
                return new AnnotatedItem(result, targetType);
            }
            if (targetType.equals(BuiltinTypesCatalogue.gYearItem)) {
                if (item.isString()) {
                    result = ItemFactory.getInstance().createGYearItem(item.getStringValue().trim());
                } else if (item.isDateTime() || item.isDate()) {
                    result = ItemFactory.getInstance().createGYearItem(item.getDateTimeValue(), item.hasTimeZone());
                } else {
                    return null;
                }
                if (targetType.equals(BuiltinTypesCatalogue.gYearItem)) {
                    return result;
                }
                return new AnnotatedItem(result, targetType);
            }
            if (targetType.equals(BuiltinTypesCatalogue.gMonthDayItem)) {
                if (item.isString()) {
                    result = ItemFactory.getInstance().createGMonthDayItem(item.getStringValue().trim());
                } else if (item.isDateTime() || item.isDate()) {
                    result = ItemFactory.getInstance().createGMonthDayItem(item.getDateTimeValue(), item.hasTimeZone());
                } else {
                    return null;
                }
                if (targetType.equals(BuiltinTypesCatalogue.gMonthDayItem)) {
                    return result;
                }
                return new AnnotatedItem(result, targetType);
            }
            if (targetType.equals(BuiltinTypesCatalogue.gYearMonthItem)) {
                if (item.isString()) {
                    result = ItemFactory.getInstance().createGYearMonthItem(item.getStringValue().trim());
                } else if (item.isDateTime() || item.isDate()) {
                    result = ItemFactory.getInstance()
                        .createGYearMonthItem(item.getDateTimeValue(), item.hasTimeZone());
                } else {
                    return null;
                }
                if (targetType.equals(BuiltinTypesCatalogue.gYearMonthItem)) {
                    return result;
                }
                return new AnnotatedItem(result, targetType);
            }

            if (targetType.equals(BuiltinTypesCatalogue.numericItem)) {
                if (item.isString()) {
                    return ItemFactory.getInstance().createDoubleItem(item.castToDoubleValue());
                }
                if (item.isBoolean()) {
                    return ItemFactory.getInstance().createDoubleItem(item.getBooleanValue() ? 1 : 0);
                }
            }
            return null;
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

    public static boolean checkFacetsInteger(Item item, ItemType targetType) {
        if (
            (targetType.getMinInclusiveFacet() != null
                && item.getIntegerValue().compareTo(targetType.getMinInclusiveFacet().getIntegerValue()) < 0)
                || (targetType.getMaxInclusiveFacet() != null
                    && item.getIntegerValue().compareTo(targetType.getMaxInclusiveFacet().getIntegerValue()) > 0)
                || (targetType.getMinExclusiveFacet() != null
                    &&
                    item.getIntegerValue().compareTo(targetType.getMinExclusiveFacet().getIntegerValue()) <= 0)
                || (targetType.getMaxExclusiveFacet() != null
                    &&
                    item.getIntegerValue().compareTo(targetType.getMaxExclusiveFacet().getIntegerValue()) >= 0)
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

