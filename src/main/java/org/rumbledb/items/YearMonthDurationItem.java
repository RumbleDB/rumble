package org.rumbledb.items;


import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import org.joda.time.DurationFieldType;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.rumbledb.api.Item;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.expressions.comparison.ComparisonExpression;
import org.rumbledb.types.AtomicItemType;
import org.rumbledb.types.ItemType;


public class YearMonthDurationItem extends DurationItem {

    private static final long serialVersionUID = 1L;
    private Period value;
    public static final PeriodType yearMonthPeriodType = PeriodType.forFields(
        new DurationFieldType[] { DurationFieldType.years(), DurationFieldType.months() }
    );

    public YearMonthDurationItem() {
        super();
    }

    public YearMonthDurationItem(Period value) {
        super();
        this.value = value.normalizedStandard(yearMonthPeriodType);
        this.isNegative = this.value.toString().contains("-");
    }

    @Override
    public Period getValue() {
        return this.value;
    }

    @Override
    public Period getDurationValue() {
        return this.value;
    }

    @Override
    public boolean isYearMonthDuration() {
        return true;
    }

    @Override
    public boolean isTypeOf(ItemType type) {
        return type.equals(AtomicItemType.yearMonthDurationItem) || super.isTypeOf(type);
    }

    @Override
    public void read(Kryo kryo, Input input) {
        this.value = getDurationFromString(input.readString(), AtomicItemType.yearMonthDurationItem).normalizedStandard(
            yearMonthPeriodType
        );
        this.isNegative = this.value.toString().contains("-");
    }

    @Override
    public boolean isCastableAs(ItemType itemType) {
        return itemType.equals(AtomicItemType.yearMonthDurationItem)
            ||
            itemType.equals(AtomicItemType.dayTimeDurationItem)
            ||
            itemType.equals(AtomicItemType.durationItem)
            ||
            itemType.equals(AtomicItemType.stringItem);
    }

    @Override
    public Item castAs(ItemType itemType) {
        if (itemType.equals(AtomicItemType.durationItem)) {
            return ItemFactory.getInstance().createDurationItem(this.getValue());
        }
        if (itemType.equals(AtomicItemType.yearMonthDurationItem)) {
            return this;
        }
        if (itemType.equals(AtomicItemType.dayTimeDurationItem)) {
            return ItemFactory.getInstance().createDayTimeDurationItem(this.getValue());
        }
        if (itemType.equals(AtomicItemType.stringItem)) {
            return ItemFactory.getInstance().createStringItem(this.serialize());
        }
        throw new ClassCastException();
    }

    @Override
    public Item compareItem(
            Item other,
            ComparisonExpression.ComparisonOperator comparisonOperator,
            ExceptionMetadata metadata
    ) {
        if (other.isDuration() && !other.isDayTimeDuration() && !other.isYearMonthDuration()) {
            return other.compareItem(this, comparisonOperator, metadata);
        } else if (!other.isYearMonthDuration() && !other.isNull()) {
            throw new UnexpectedTypeException(
                    "\""
                        + this.getDynamicType().toString()
                        + "\": invalid type: can not compare for equality to type \""
                        + other.getDynamicType().toString()
                        + "\"",
                    metadata
            );
        }
        return super.compareItem(other, comparisonOperator, metadata);
    }

    @Override
    public Item add(Item other) {
        if (other.isDateTime()) {
            return ItemFactory.getInstance()
                .createDateTimeItem(other.getDateTimeValue().plus(this.getValue()), other.hasTimeZone());
        }
        if (other.isDate()) {
            return ItemFactory.getInstance()
                .createDateItem(other.getDateTimeValue().plus(this.getValue()), other.hasTimeZone());
        }
        return ItemFactory.getInstance().createYearMonthDurationItem(this.getValue().plus(other.getDurationValue()));
    }

    @Override
    public Item subtract(Item other) {
        return ItemFactory.getInstance().createYearMonthDurationItem(this.getValue().minus(other.getDurationValue()));
    }

    @Override
    public ItemType getDynamicType() {
        return AtomicItemType.yearMonthDurationItem;
    }
}
