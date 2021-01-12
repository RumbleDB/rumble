package org.rumbledb.runtime.typing;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.exceptions.CastException;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.exceptions.MoreThanOneItemException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.items.DurationItem;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.LocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.types.ItemType;
import org.rumbledb.types.SequenceType;
import org.rumbledb.types.SequenceType.Arity;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collections;


public class CastIterator extends LocalRuntimeIterator {
    private static final long serialVersionUID = 1L;
    private final RuntimeIterator child;
    private final SequenceType sequenceType;
    private Item item;

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

    @Override
    public Item next() {
        if (!this.hasNext) {
            throw new IteratorFlowException(RuntimeIterator.FLOW_EXCEPTION_MESSAGE, getMetadata());
        }
        this.hasNext = false;

        if (!canTypeBeCastToType(this.item, this.sequenceType.getItemType(), getMetadata())) {
            String message = String.format(
                "\"%s\": a value of type %s is not castable to type %s",
                this.item.serialize(),
                this.item.getDynamicType(),
                this.sequenceType.getItemType()
            );
            throw new UnexpectedTypeException(message, getMetadata());
        }
        Item result = castItemToType(this.item, this.sequenceType.getItemType(), getMetadata());
        if (result == null) {
            String message = String.format(
                "\"%s\": this literal is not castable to type %s",
                this.item.serialize(),
                this.sequenceType.getItemType()
            );
            throw new CastException(message, getMetadata());
        }
        return result;
    }

    @Override
    public void open(DynamicContext context) {
        super.open(context);
        try {
            this.item = this.child.materializeAtMostOneItemOrNull(this.currentDynamicContextForLocalExecution);
        } catch (MoreThanOneItemException e) {
            throw new UnexpectedTypeException(
                    " Sequence of more than one item can not be treated as type "
                        + this.sequenceType.toString(),
                    getMetadata()
            );
        }
        if (
            this.item == null && !this.sequenceType.isEmptySequence() && this.sequenceType.getArity() != Arity.OneOrZero
        ) {
            throw new UnexpectedTypeException(
                    " Empty sequence can not be cast to type with quantifier '1'",
                    getMetadata()
            );
        }
        if (this.item != null && !this.item.isAtomic()) {
            throw new UnexpectedTypeException(
                    "Only atomics can be cast to atomic types.",
                    getMetadata()
            );
        }
        this.hasNext = this.item != null;
    }

    @Override
    public void close() {
        super.close();
        this.item = null;
        this.hasNext = false;
    }

    @Override
    public void reset(DynamicContext context) {
        super.reset(context);
        try {
            this.item = this.child.materializeAtMostOneItemOrNull(this.currentDynamicContextForLocalExecution);
        } catch (MoreThanOneItemException e) {
            throw new UnexpectedTypeException(
                    " Sequence of more than one item can not be treated as type "
                        + this.sequenceType.toString(),
                    getMetadata()
            );
        }
        if (
            this.item == null && !this.sequenceType.isEmptySequence() && this.sequenceType.getArity() != Arity.OneOrZero
        ) {
            throw new UnexpectedTypeException(
                    " Empty sequence can not be cast to type with quantifier '1'",
                    getMetadata()
            );
        }
        this.hasNext = this.item != null;
    }

    public static Item castItemToType(Item item, ItemType targetType, ExceptionMetadata metadata) {
        try {
            String itemType = item.getDynamicType().toString();

            if (itemType.equals(targetType.toString())) {
                return item;
            }

            if (targetType.equals(ItemType.nullItem)) {
                if (item.isString() && item.getStringValue().equals("null")) {
                    return ItemFactory.getInstance().createNullItem();
                }
            }

            if (targetType.equals(ItemType.stringItem)) {
                return ItemFactory.getInstance().createStringItem(item.serialize());
            }

            if (targetType.equals(ItemType.booleanItem)) {
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

            if (targetType.equals(ItemType.doubleItem)) {
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
            if (targetType.equals(ItemType.floatItem)) {
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

            if (targetType.equals(ItemType.decimalItem)) {
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

            if (targetType.equals(ItemType.integerItem)) {
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

            if (targetType.equals(ItemType.intItem)) {
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

            if (targetType.equals(ItemType.anyURIItem)) {
                if (item.isString()) {
                    return ItemFactory.getInstance().createAnyURIItem(item.getStringValue().trim());
                }
            }

            if (targetType.equals(ItemType.base64BinaryItem)) {
                if (item.isString()) {
                    return ItemFactory.getInstance().createBase64BinaryItem(item.getStringValue().trim());
                }
                if (item.isHexBinary()) {
                    return ItemFactory.getInstance()
                        .createBase64BinaryItem(Base64.encodeBase64String(item.getBinaryValue()));
                }
            }

            if (targetType.equals(ItemType.hexBinaryItem)) {
                if (item.isString()) {
                    return ItemFactory.getInstance().createHexBinaryItem(item.getStringValue().trim());
                }
                if (item.isBase64Binary()) {
                    return ItemFactory.getInstance().createHexBinaryItem(Hex.encodeHexString(item.getBinaryValue()));
                }
            }

            if (targetType.equals(ItemType.dateItem)) {
                if (item.isString()) {
                    return ItemFactory.getInstance().createDateItem(item.getStringValue().trim());
                }
                if (item.isDateTime()) {
                    return ItemFactory.getInstance().createDateItem(item.getDateTimeValue(), item.hasTimeZone());
                }
            }
            if (targetType.equals(ItemType.timeItem)) {
                if (item.isString()) {
                    return ItemFactory.getInstance().createTimeItem(item.getStringValue().trim());
                }
                if (item.isDateTime()) {
                    return ItemFactory.getInstance().createTimeItem(item.getDateTimeValue(), item.hasTimeZone());
                }
            }
            if (targetType.equals(ItemType.dateTimeItem)) {
                if (item.isString()) {
                    return ItemFactory.getInstance().createDateTimeItem(item.getStringValue().trim());
                }
                if (item.isDate()) {
                    return ItemFactory.getInstance().createDateTimeItem(item.getDateTimeValue(), item.hasTimeZone());
                }
            }
            if (targetType.equals(ItemType.yearMonthDurationItem)) {
                if (item.isString()) {
                    return ItemFactory.getInstance()
                        .createYearMonthDurationItem(
                            DurationItem.getDurationFromString(
                                item.getStringValue().trim(),
                                ItemType.yearMonthDurationItem
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
            if (targetType.equals(ItemType.dayTimeDurationItem)) {
                if (item.isString()) {
                    return ItemFactory.getInstance()
                        .createDayTimeDurationItem(
                            DurationItem.getDurationFromString(
                                item.getStringValue().trim(),
                                ItemType.dayTimeDurationItem
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
            if (targetType.equals(ItemType.durationItem)) {
                if (item.isString()) {
                    return ItemFactory.getInstance()
                        .createDurationItem(
                            DurationItem.getDurationFromString(item.getStringValue().trim(), ItemType.durationItem)
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

    public static boolean canTypeBeCastToType(Item item, ItemType targetType, ExceptionMetadata metadata) {
        String itemType = item.getDynamicType().toString();

        if (itemType.equals(targetType.toString())) {
            return true;
        }

        if (targetType.equals(ItemType.nullItem)) {
            if (item.isString()) {
                return true;
            }
        }

        if (targetType.equals(ItemType.stringItem)) {
            return true;
        }

        if (targetType.equals(ItemType.booleanItem)) {
            if (item.isString()) {
                return true;
            }
            if (item.isInt()) {
                return true;
            }
            if (item.isInteger()) {
                return true;
            }
            if (item.isDecimal()) {
                return true;
            }
            if (item.isDouble()) {
                return true;
            }
            if (item.isFloat()) {
                return true;
            }
        }

        if (targetType.equals(ItemType.doubleItem)) {
            if (item.isString()) {
                return true;
            }
            if (item.isBoolean()) {
                return true;
            }
            if (item.isNumeric()) {
                return true;
            }
        }
        if (targetType.equals(ItemType.floatItem)) {
            if (item.isString()) {
                return true;
            }
            if (item.isBoolean()) {
                return true;
            }
            if (item.isNumeric()) {
                return true;
            }
        }

        if (targetType.equals(ItemType.decimalItem)) {
            if (item.isString()) {
                return true;
            }
            if (item.isBoolean()) {
                return true;
            }
            if (item.isNumeric()) {
                return true;
            }
        }

        if (targetType.equals(ItemType.integerItem)) {
            if (item.isString()) {
                return true;
            }
            if (item.isBoolean()) {
                return true;
            }
            if (item.isNumeric()) {
                return true;
            }
        }

        if (targetType.equals(ItemType.intItem)) {
            if (item.isString()) {
            }
            if (item.isBoolean()) {
                return true;
            }
            if (item.isNumeric()) {
                return true;
            }
        }

        if (targetType.equals(ItemType.anyURIItem)) {
            if (item.isString()) {
                return true;
            }
        }

        if (targetType.equals(ItemType.base64BinaryItem)) {
            if (item.isString()) {
                return true;
            }
            if (item.isString()) {
                return true;
            }
            if (item.isHexBinary()) {
                return true;
            }
        }

        if (targetType.equals(ItemType.hexBinaryItem)) {
            if (item.isString()) {
                return true;
            }
            if (item.isBase64Binary()) {
                return true;
            }
        }

        if (targetType.equals(ItemType.dateItem)) {
            if (item.isString()) {
                return true;
            }
            if (item.isDateTime()) {
                return true;
            }
        }
        if (targetType.equals(ItemType.timeItem)) {
            if (item.isString()) {
                return true;
            }
            if (item.isDateTime()) {
                return true;
            }
        }
        if (targetType.equals(ItemType.dateTimeItem)) {
            if (item.isString()) {
                return true;
            }
            if (item.isDate()) {
                return true;
            }
        }
        if (targetType.equals(ItemType.yearMonthDurationItem)) {
            if (item.isString()) {
                return true;
            }
            if (item.isDuration()) {
                return true;
            }
            if (item.isDayTimeDuration()) {
                return true;
            }
        }
        if (targetType.equals(ItemType.dayTimeDurationItem)) {
            if (item.isString()) {
                return true;
            }
            if (item.isDuration()) {
                return true;
            }
            if (item.isYearMonthDuration()) {
                return true;
            }
        }
        if (targetType.equals(ItemType.durationItem)) {
            if (item.isString()) {
                return true;
            }
            if (item.isDayTimeDuration()) {
                return true;
            }
            if (item.isYearMonthDuration()) {
                return true;
            }
        }

        return false;
    }
}
