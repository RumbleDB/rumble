package org.rumbledb.runtime.typing;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.exceptions.CastException;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.MoreThanOneItemException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.exceptions.UnknownCastTypeException;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.items.DurationItem;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
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
            ExecutionMode executionMode,
            ExceptionMetadata iteratorMetadata
    ) {
        super(Collections.singletonList(child), executionMode, iteratorMetadata);
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
        try {
            String itemType = item.getDynamicType().toString();

            if (itemType.equals(targetType.toString())) {
                return item;
            }

            if (targetType.equals(BuiltinTypesCatalogue.nullItem)) {
                if (item.isString() && item.getStringValue().trim().equals("null")) {
                    return ItemFactory.getInstance().createNullItem();
                }
            }

            if (targetType.equals(BuiltinTypesCatalogue.stringItem)) {
                return ItemFactory.getInstance().createStringItem(item.serialize());
            }

            if (targetType.equals(BuiltinTypesCatalogue.booleanItem)) {
                if (item.isString()) {
                    return ItemFactory.getInstance()
                        .createBooleanItem(Boolean.parseBoolean(item.getStringValue().trim()));
                }
                if (item.isInt()) {
                    return ItemFactory.getInstance().createBooleanItem(item.getIntValue() != 0);
                }
                if (item.isInteger()) {
                    return ItemFactory.getInstance().createBooleanItem(!item.getIntegerValue().equals(BigInteger.ZERO));
                }
                if (item.isDecimal()) {
                    return ItemFactory.getInstance().createBooleanItem(!item.getDecimalValue().equals(BigDecimal.ZERO));
                }
                if (item.isDouble()) {
                    return ItemFactory.getInstance().createBooleanItem(item.getDoubleValue() != 0);
                }
                if (item.isFloat()) {
                    return ItemFactory.getInstance().createBooleanItem(item.getFloatValue() != 0);
                }
            }

            if (targetType.equals(BuiltinTypesCatalogue.doubleItem)) {
                if (item.isString()) {
                    return ItemFactory.getInstance().createDoubleItem(item.castToDoubleValue());
                }
                if (item.isBoolean()) {
                    return ItemFactory.getInstance().createDoubleItem(item.getBooleanValue() ? 1 : 0);
                }
                if (item.isNumeric()) {
                    return ItemFactory.getInstance().createDoubleItem(item.castToDoubleValue());
                }
            }
            if (targetType.equals(BuiltinTypesCatalogue.floatItem)) {
                if (item.isString()) {
                    return ItemFactory.getInstance().createFloatItem(item.castToFloatValue());
                }
                if (item.isBoolean()) {
                    return ItemFactory.getInstance().createFloatItem(item.getBooleanValue() ? 1 : 0);
                }
                if (item.isNumeric()) {
                    return ItemFactory.getInstance().createFloatItem(item.castToFloatValue());
                }
            }

            if (targetType.equals(BuiltinTypesCatalogue.decimalItem)) {
                if (item.isString()) {
                    return ItemFactory.getInstance().createDecimalItem(item.castToDecimalValue());
                }
                if (item.isBoolean()) {
                    return ItemFactory.getInstance()
                        .createDecimalItem(item.getBooleanValue() ? BigDecimal.ONE : BigDecimal.ZERO);
                }
                if (item.isNumeric()) {
                    return ItemFactory.getInstance().createDecimalItem(item.castToDecimalValue());
                }
            }

            if (targetType.equals(BuiltinTypesCatalogue.integerItem)) {
                if (item.isString()) {
                    return ItemFactory.getInstance().createIntegerItem(item.castToIntegerValue());
                }
                if (item.isBoolean()) {
                    return ItemFactory.getInstance()
                        .createIntegerItem(item.getBooleanValue() ? BigInteger.ONE : BigInteger.ZERO);
                }
                if (item.isNumeric()) {
                    return ItemFactory.getInstance().createIntegerItem(item.castToIntegerValue());
                }
            }

            if (targetType.equals(BuiltinTypesCatalogue.intItem)) {
                if (item.isString()) {
                    return ItemFactory.getInstance().createIntItem(item.castToIntValue());
                }
                if (item.isBoolean()) {
                    return ItemFactory.getInstance()
                        .createIntItem(item.getBooleanValue() ? 1 : 0);
                }
                if (item.isNumeric()) {
                    return ItemFactory.getInstance().createIntItem(item.castToIntValue());
                }
            }

            if (targetType.equals(BuiltinTypesCatalogue.anyURIItem)) {
                if (item.isString()) {
                    return ItemFactory.getInstance().createAnyURIItem(item.getStringValue().trim());
                }
            }

            if (targetType.equals(BuiltinTypesCatalogue.base64BinaryItem)) {
                if (item.isString()) {
                    return ItemFactory.getInstance().createBase64BinaryItem(item.getStringValue().trim());
                }
                if (item.isHexBinary()) {
                    return ItemFactory.getInstance()
                        .createBase64BinaryItem(Base64.encodeBase64String(item.getBinaryValue()));
                }
            }

            if (targetType.equals(BuiltinTypesCatalogue.hexBinaryItem)) {
                if (item.isString()) {
                    return ItemFactory.getInstance().createHexBinaryItem(item.getStringValue().trim());
                }
                if (item.isBase64Binary()) {
                    return ItemFactory.getInstance().createHexBinaryItem(Hex.encodeHexString(item.getBinaryValue()));
                }
            }

            if (targetType.equals(BuiltinTypesCatalogue.dateItem)) {
                if (item.isString()) {
                    return ItemFactory.getInstance().createDateItem(item.getStringValue().trim());
                }
                if (item.isDate()) {
                    return ItemFactory.getInstance().createDateItem(item.getDateTimeValue(), item.hasTimeZone());
                }
                if (item.isDateTime()) {
                    return ItemFactory.getInstance().createDateItem(item.getDateTimeValue(), item.hasTimeZone());
                }
            }
            if (targetType.equals(BuiltinTypesCatalogue.timeItem)) {
                if (item.isString()) {
                    return ItemFactory.getInstance().createTimeItem(item.getStringValue().trim());
                }
                if (item.isTime()) {
                    return ItemFactory.getInstance().createTimeItem(item.getDateTimeValue(), item.hasTimeZone());
                }
                if (item.isDateTime()) {
                    return ItemFactory.getInstance().createTimeItem(item.getDateTimeValue(), item.hasTimeZone());
                }
            }
            if (targetType.equals(BuiltinTypesCatalogue.dateTimeItem)) {
                if (item.isString()) {
                    return ItemFactory.getInstance().createDateTimeItem(item.getStringValue().trim());
                }
                if (item.isDate()) {
                    return ItemFactory.getInstance().createDateTimeItem(item.getDateTimeValue(), item.hasTimeZone());
                }
                if (item.isDateTime()) {
                    return ItemFactory.getInstance().createDateTimeItem(item.getDateTimeValue(), item.hasTimeZone());
                }
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
                }
                if (item.isDuration()) {
                    return ItemFactory.getInstance().createYearMonthDurationItem(item.getDurationValue());
                }
                if (item.isDayTimeDuration()) {
                    return ItemFactory.getInstance().createYearMonthDurationItem(item.getDurationValue());
                }
            }
            if (targetType.equals(BuiltinTypesCatalogue.dayTimeDurationItem)) {
                if (item.isString()) {
                    return ItemFactory.getInstance()
                        .createDayTimeDurationItem(
                            DurationItem.getDurationFromString(
                                item.getStringValue().trim(),
                                BuiltinTypesCatalogue.dayTimeDurationItem
                            )
                        );
                }
                if (item.isDuration()) {
                    return ItemFactory.getInstance().createDayTimeDurationItem(item.getDurationValue());
                }
                if (item.isYearMonthDuration()) {
                    return ItemFactory.getInstance().createDayTimeDurationItem(item.getDurationValue());
                }
            }
            if (targetType.equals(BuiltinTypesCatalogue.durationItem)) {
                if (item.isString()) {
                    return ItemFactory.getInstance()
                        .createDurationItem(
                            DurationItem.getDurationFromString(
                                item.getStringValue().trim(),
                                BuiltinTypesCatalogue.durationItem
                            )
                        );
                }
                if (item.isDayTimeDuration()) {
                    return ItemFactory.getInstance().createDurationItem(item.getDurationValue());
                }
                if (item.isYearMonthDuration()) {
                    return ItemFactory.getInstance().createDurationItem(item.getDurationValue());
                }
            }

            return null;
        } catch (Exception e) {
            return null;
        }
    }
}
