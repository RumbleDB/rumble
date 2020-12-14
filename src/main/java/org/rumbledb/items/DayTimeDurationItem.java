package org.rumbledb.items;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import org.joda.time.Instant;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.rumbledb.api.Item;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.expressions.comparison.ComparisonExpression;
import org.rumbledb.types.AtomicItemType;
import org.rumbledb.types.ItemType;
import java.math.BigDecimal;

public class DayTimeDurationItem extends DurationItem {

    private static final long serialVersionUID = 1L;

    public DayTimeDurationItem() {
        super();
    }

    public DayTimeDurationItem(Period value) {
        super();
        this.value = value.normalizedStandard(PeriodType.dayTime());
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
    public boolean isAtomic() {
        return true;
    }

    @Override
    public boolean isDayTimeDuration() {
        return true;
    }

    @Override
    public boolean isTypeOf(ItemType type) {
        return type.equals(AtomicItemType.dayTimeDurationItem) || super.isTypeOf(type);
    }

    @Override
    public void read(Kryo kryo, Input input) {
        this.value = getDurationFromString(input.readString(), AtomicItemType.dayTimeDurationItem).normalizedStandard(
            PeriodType.dayTime()
        );
        this.isNegative = this.value.toString().contains("-");
    }

    @Override
    public boolean isCastableAs(ItemType itemType) {
        return itemType.equals(AtomicItemType.dayTimeDurationItem)
            ||
            itemType.equals(AtomicItemType.yearMonthDurationItem)
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
        if (itemType.equals(AtomicItemType.dayTimeDurationItem)) {
            return this;
        }
        if (itemType.equals(AtomicItemType.yearMonthDurationItem)) {
            return ItemFactory.getInstance().createYearMonthDurationItem(this.getValue());
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
        } else if (!other.isDayTimeDuration() && !other.isNull()) {
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
                .createDateItem(other.getDateTimeValue().plus(this.getValue()), other.hasDateTime());
        }
        if (other.isTime()) {
            return ItemFactory.getInstance()
                .createTimeItem(other.getDateTimeValue().plus(this.getValue()), other.hasDateTime());
        }
        return ItemFactory.getInstance().createDayTimeDurationItem(this.getValue().plus(other.getDurationValue()));
    }

    @Override
    public Item subtract(Item other) {
        return ItemFactory.getInstance().createDayTimeDurationItem(this.getValue().minus(other.getDurationValue()));
    }

    @Override
    public Item multiply(Item other) {
        BigDecimal otherAsDecimal = other.castToDecimalValue();
        if (otherAsDecimal.stripTrailingZeros().scale() <= 0) {
            return ItemFactory.getInstance()
                .createDayTimeDurationItem(this.getValue().multipliedBy(otherAsDecimal.intValue()));
        }
        long durationInMillis = this.getValue().toStandardDuration().getMillis();
        long durationResult = otherAsDecimal.multiply(BigDecimal.valueOf(durationInMillis))
            .setScale(16, BigDecimal.ROUND_HALF_UP)
            .longValue();
        return ItemFactory.getInstance().createDayTimeDurationItem(new Period(durationResult, PeriodType.dayTime()));
    }

    @Override
    public Item divide(Item other) {
        if (other.isDayTimeDuration()) {
            Instant now = Instant.now();
            return ItemFactory.getInstance()
                .createDecimalItem(
                    BigDecimal.valueOf(
                        this.getValue().toDurationFrom(now).getMillis()
                            /
                            (double) other.getDurationValue().toDurationFrom(now).getMillis()
                    )
                );
        }
        BigDecimal otherBd = other.castToDecimalValue();
        long durationInMillis = this.getValue().toStandardDuration().getMillis();
        long durationResult = (BigDecimal.valueOf(durationInMillis)).divide(otherBd, 16, BigDecimal.ROUND_HALF_UP)
            .longValue();
        return ItemFactory.getInstance().createDayTimeDurationItem(new Period(durationResult, PeriodType.dayTime()));
    }

    @Override
    public ItemType getDynamicType() {
        return AtomicItemType.dayTimeDurationItem;
    }
}
