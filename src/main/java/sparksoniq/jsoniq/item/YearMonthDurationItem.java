package sparksoniq.jsoniq.item;


import org.joda.time.*;
import org.rumbledb.api.Item;
import sparksoniq.exceptions.UnexpectedTypeException;
import sparksoniq.jsoniq.compiler.translator.expr.operational.base.OperationalExpressionBase;
import sparksoniq.jsoniq.runtime.iterator.operational.ComparisonOperationIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.semantics.types.AtomicType;
import sparksoniq.semantics.types.AtomicTypes;
import sparksoniq.semantics.types.ItemType;
import sparksoniq.semantics.types.ItemTypes;

import java.math.BigDecimal;


public class YearMonthDurationItem extends DurationItem {

    private static final long serialVersionUID = 1L;
    private Period _value;

    public YearMonthDurationItem() {
        super();
    }

    public YearMonthDurationItem(Period value) {
        super();
        this._value = value;
    }

    @Override
    public Period getValue() {
        return this._value;
    }

    @Override
    public boolean isYearMonthDuration() {
        return true;
    }

    @Override
    public boolean isTypeOf(ItemType type) {
        return type.getType().equals(ItemTypes.YearMonthDurationItem) || super.isTypeOf(type);
    }

    @Override
    public boolean isCastableAs(AtomicType type) {
        return type.getType().equals(AtomicTypes.YearMonthDurationItem) ||
                type.getType().equals(AtomicTypes.DurationItem) ||
                type.getType().equals(AtomicTypes.StringItem);
    }

    @Override
    public AtomicItem castAs(AtomicItem atomicItem) {
        return atomicItem.createFromYearMonthDuration(this);
    }

    @Override
    public AtomicItem createFromDuration(DurationItem durationItem) {
        return ItemFactory.getInstance().createYearMonthDurationItem(getDurationFromString(durationItem.serialize(), AtomicTypes.YearMonthDurationItem));
    }

    @Override
    public AtomicItem createFromYearMonthDuration(YearMonthDurationItem yearMonthDurationItem) {
        return yearMonthDurationItem;
    }

    @Override
    public AtomicItem createFromString(StringItem stringItem) {
        return ItemFactory.getInstance().createYearMonthDurationItem(getDurationFromString(stringItem.getStringValue(), AtomicTypes.YearMonthDurationItem));
    }

    @Override
    public Item compareItem(Item other, OperationalExpressionBase.Operator operator, IteratorMetadata metadata) {
        if (!other.isYearMonthDuration() && (!other.isDuration() || other.isDayTimeDuration())) {
            throw new UnexpectedTypeException("\"" + ItemTypes.getItemTypeName(this.getClass().getSimpleName())
                    + "\": invalid type: can not compare for equality to type \""
                    + ItemTypes.getItemTypeName(other.getClass().getSimpleName()) + "\"", metadata);
        } else if (!other.isDayTimeDuration() && !other.isYearMonthDuration() && other.isDuration()) {
            return super.compareItem(other, operator, metadata);
        }
        return operator.apply(this, other);
    }

    @Override
    public Item add(Item other) {
        if (other.isDateTime()) return other.add(this);
        return ItemFactory.getInstance().createYearMonthDurationItem(this.getValue().plus(((YearMonthDurationItem) other).getValue()));
    }

    @Override
    public Item subtract(Item other, boolean negated) {
        return ItemFactory.getInstance().createYearMonthDurationItem(this.getValue().minus(((YearMonthDurationItem) other).getValue()));
    }

    @Override
    public Item multiply(Item other) {
        BigDecimal otherBd = other.castToDecimalValue();
        if (otherBd.stripTrailingZeros().scale() <= 0) {
            return ItemFactory.getInstance().createYearMonthDurationItem(this.getValue().multipliedBy(otherBd.intValue()));
        }
        int years = this.getValue().getYears();
        int months = years * 12 + this.getValue().getMonths();
        BigDecimal[] quotientAndRemainder = (BigDecimal.valueOf(months).multiply(otherBd)
                .setScale(0, BigDecimal.ROUND_HALF_UP)).divideAndRemainder(BigDecimal.valueOf(12));
        Period newPeriod = new Period().withYears(quotientAndRemainder[0].intValue()).withMonths(quotientAndRemainder[1].intValue());
        return ItemFactory.getInstance().createYearMonthDurationItem(newPeriod);
    }

    @Override
    public Item divide(Item other, boolean inverted) {
        BigDecimal otherBd;
        if (other.isYearMonthDuration()) {
            otherBd = BigDecimal.valueOf(((YearMonthDurationItem) other).getValue().toStandardDuration().getMillis());
        } else {
            otherBd = other.castToDecimalValue();
        }
        int years = this.getValue().getYears();
        int months = years * 12 + this.getValue().getMonths();
        BigDecimal[] quotientAndRemainder = (BigDecimal.valueOf(months).divide(otherBd, 0, BigDecimal.ROUND_HALF_UP))
                .divideAndRemainder(BigDecimal.valueOf(12));
        Period newPeriod = new Period(0L, PeriodType.yearMonthDay().withDaysRemoved()).withYears(quotientAndRemainder[0].intValue()).withMonths(quotientAndRemainder[1].intValue());
        return ItemFactory.getInstance().createYearMonthDurationItem(newPeriod);
    }
}
