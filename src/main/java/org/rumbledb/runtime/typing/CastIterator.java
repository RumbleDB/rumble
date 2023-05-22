package org.rumbledb.runtime.typing;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang.StringUtils;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.*;
import org.rumbledb.items.AnnotatedItem;
import org.rumbledb.items.DurationItem;
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
        if (item != null && !item.isAtomic()) {
            throw new UnexpectedTypeException(
                    "Only atomics can be cast to atomic types.",
                    getMetadata()
            );
        }
        if (item == null) {
            return null;
        }
        if (!item.getDynamicType().isStaticallyCastableAs(this.sequenceType.getItemType())) {
            String message = String.format(
                "\"%s\": a value of type %s is not castable to type %s",
                item.serialize(),
                item.getDynamicType(),
                this.sequenceType.getItemType()
            );
            throw new UnexpectedTypeException(message, getMetadata());
        }
        Item result = castItemToType(item, this.sequenceType.getItemType(), getMetadata());
        if (result == null) {
            String message = String.format(
                "\"%s\": this literal is not castable to type %s",
                item.serialize(),
                this.sequenceType.getItemType()
            );
            throw new CastException(message, getMetadata());
        }
        return result;
    }

    public static Item castItemToType(Item item, ItemType targetType, ExceptionMetadata metadata) {
        Item result = null;
        try {
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
                result = ItemFactory.getInstance().createStringItem(item.serialize());
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
                } else if (item.isDate()) {
                    result = ItemFactory.getInstance().createDateItem(item.getDateTimeValue(), item.hasTimeZone());
                } else if (item.isDateTime()) {
                    result = ItemFactory.getInstance().createDateItem(item.getDateTimeValue(), item.hasTimeZone());
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
                    result = ItemFactory.getInstance().createTimeItem(item.getDateTimeValue(), item.hasTimeZone());
                } else if (item.isDateTime()) {
                    result = ItemFactory.getInstance().createTimeItem(item.getDateTimeValue(), item.hasTimeZone());
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
                } else if (item.isDate()) {
                    result = ItemFactory.getInstance().createDateTimeItem(item.getDateTimeValue(), item.hasTimeZone());
                } else if (item.isDateTime()) {
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
                } else if (item.isDate()) {
                    result = ItemFactory.getInstance().createDateTimeStampItem(item.getDateTimeValue(), false);
                } else if (item.isDateTime()) {
                    result = ItemFactory.getInstance().createDateTimeStampItem(item.getDateTimeValue(), true);
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
                    return ItemFactory.getInstance()
                        .createYearMonthDurationItem(
                            DurationItem.getDurationFromString(
                                item.getStringValue().trim(),
                                BuiltinTypesCatalogue.yearMonthDurationItem
                            )
                        );
                } else if (item.isDuration()) {
                    result = ItemFactory.getInstance().createYearMonthDurationItem(item.getDurationValue());
                } else if (item.isDayTimeDuration()) {
                    result = ItemFactory.getInstance().createYearMonthDurationItem(item.getDurationValue());
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
                    result = ItemFactory.getInstance()
                        .createDayTimeDurationItem(
                            DurationItem.getDurationFromString(
                                item.getStringValue().trim(),
                                BuiltinTypesCatalogue.dayTimeDurationItem
                            )
                        );
                } else if (item.isDuration()) {
                    result = ItemFactory.getInstance().createDayTimeDurationItem(item.getDurationValue());
                } else if (item.isYearMonthDuration()) {
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
                            DurationItem.getDurationFromString(
                                item.getStringValue().trim(),
                                BuiltinTypesCatalogue.durationItem
                            )
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
                } else {
                    return null;
                }
                if (targetType.equals(BuiltinTypesCatalogue.gYearMonthItem)) {
                    return result;
                }
                return new AnnotatedItem(result, targetType);
            }

            return null;
        } catch (InvalidLexicalValueException i) {
            throw new InvalidLexicalValueException(
                    "NaN or INF cannot be cast to another type than Float or Double",
                    metadata
            );
        } catch (Exception e) {
            return null;
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
        // * TODO: fix this that causes pipeline to fail all tests involving duration
        if (
            (targetType.getMinInclusiveFacet() != null
                && item.getDurationValue()
                    .toStandardDuration()
                    .compareTo(targetType.getMinInclusiveFacet().getDurationValue().toStandardDuration()) < 0)
                || (targetType.getMaxInclusiveFacet() != null
                    && item.getDurationValue()
                        .toStandardDuration()
                        .compareTo(targetType.getMaxInclusiveFacet().getDurationValue().toStandardDuration()) > 0)
                || (targetType.getMinExclusiveFacet() != null
                    &&
                    item.getDurationValue()
                        .toStandardDuration()
                        .compareTo(targetType.getMinExclusiveFacet().getDurationValue().toStandardDuration()) <= 0)
                || (targetType.getMaxExclusiveFacet() != null
                    &&
                    item.getDurationValue()
                        .toStandardDuration()
                        .compareTo(targetType.getMaxExclusiveFacet().getDurationValue().toStandardDuration()) >= 0)
        ) {
            return false;
        }


        return true;
    }

    @Override
    public NativeClauseContext generateNativeQuery(NativeClauseContext nativeClauseContext) {
        NativeClauseContext value = this.children.get(0).generateNativeQuery(nativeClauseContext);
        if (value.equals(NativeClauseContext.NoNativeQuery)) {
            return NativeClauseContext.NoNativeQuery;
        }
        if (this.children.get(0).getStaticType().getArity() != Arity.One) {
            return NativeClauseContext.NoNativeQuery;
        }
        String resultingQuery = "";
        if (this.sequenceType.getItemType().equals(BuiltinTypesCatalogue.booleanItem)) {
            resultingQuery = " (CAST (" + value.getResultingQuery() + " AS " + "BOOLEAN" + ")) ";
            System.err.println("Boolean");
        } else if (this.sequenceType.getItemType().equals(BuiltinTypesCatalogue.byteItem)) {
            resultingQuery = " (CAST (" + value.getResultingQuery() + " AS " + "BYTE" + ")) ";
            System.err.println("Byte");
        } else if (this.sequenceType.getItemType().equals(BuiltinTypesCatalogue.shortItem)) {
            resultingQuery = " (CAST (" + value.getResultingQuery() + " AS " + "SHORT" + ")) ";
            System.err.println("Short");
        } else if (this.sequenceType.getItemType().equals(BuiltinTypesCatalogue.intItem)) {
            resultingQuery = " (CAST (" + value.getResultingQuery() + " AS " + "INT" + ")) ";
            System.err.println("Int");
        } else if (this.sequenceType.getItemType().equals(BuiltinTypesCatalogue.longItem)) {
            resultingQuery = " (CAST (" + value.getResultingQuery() + " AS " + "LONG" + ")) ";
            System.err.println("Long");
        } else if (this.sequenceType.getItemType().equals(BuiltinTypesCatalogue.integerItem)) {
            resultingQuery = " (CAST (" + value.getResultingQuery() + " AS " + "DECIMAL(38,0)" + ")) ";
            System.err.println("Integer");
        } else if (this.sequenceType.getItemType().equals(BuiltinTypesCatalogue.decimalItem)) {
            resultingQuery = " (CAST (" + value.getResultingQuery() + " AS " + "DECIMAL(38,19)" + ")) ";
            System.err.println("Decimal");
        } else if (this.sequenceType.getItemType().equals(BuiltinTypesCatalogue.doubleItem)) {
            resultingQuery = " (CAST (" + value.getResultingQuery() + " AS " + "DOUBLE" + ")) ";
            System.err.println("Double");
        } else if (this.sequenceType.getItemType().equals(BuiltinTypesCatalogue.floatItem)) {
            resultingQuery = " (CAST (" + value.getResultingQuery() + " AS " + "FLOAT" + ")) ";
            System.err.println("Float");
        } else if (this.sequenceType.getItemType().equals(BuiltinTypesCatalogue.stringItem)) {
            resultingQuery = " (CAST (" + value.getResultingQuery() + " AS " + "STRING" + ")) ";
            System.err.println("String");
        } else if (this.sequenceType.getItemType().equals(BuiltinTypesCatalogue.anyURIItem)) {
            resultingQuery = " (CAST (" + value.getResultingQuery() + " AS " + "STRING" + ")) ";
            System.err.println("anyURI");
        } else if (this.sequenceType.getItemType().equals(BuiltinTypesCatalogue.dateItem)) {
            if (getConfiguration().dateWithTimezone()) {
                return NativeClauseContext.NoNativeQuery;
            }
            resultingQuery = " (CAST (" + value.getResultingQuery() + " AS " + "DATE" + ")) ";
            System.err.println("Date");
        } else if (this.sequenceType.getItemType().equals(BuiltinTypesCatalogue.dateTimeStampItem)) {
            resultingQuery = " (CAST (" + value.getResultingQuery() + " AS " + "TIMESTAMP" + ")) ";
            System.err.println("Timestamp");
            /*
             * } else if (this.sequenceType.getItemType().equals(BuiltinTypesCatalogue.hexBinaryItem)) {
             * resultingQuery = " (CAST (" + value.getResultingQuery() + " AS " + "BINARY" + ")) ";
             * System.err.println("HexBinary");
             */
        } else if (this.sequenceType.getItemType().equals(BuiltinTypesCatalogue.nullItem)) {
            resultingQuery = " NULL ";
            System.err.println("Null");
        } else {
            return NativeClauseContext.NoNativeQuery;
        }
        System.err.println("Return");
        return new NativeClauseContext(
                nativeClauseContext,
                resultingQuery
        );
    }
}

