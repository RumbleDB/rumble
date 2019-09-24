package sparksoniq.jsoniq.item;

import org.joda.time.Instant;
import org.joda.time.Period;
import org.joda.time.PeriodType;
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

public class DayTimeDurationItem extends DurationItem {

    private static final long serialVersionUID = 1L;
    private Period _value;

    public DayTimeDurationItem() {
        super();
    }

    public DayTimeDurationItem(Period value) {
        super();
        this._value = value;
    }

    @Override
    public Period getValue() {
        return this._value;
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
        return type.getType().equals(ItemTypes.DayTimeDurationItem) || super.isTypeOf(type);
    }

    @Override
    public boolean isCastableAs(AtomicType type) {
        return type.getType().equals(AtomicTypes.DayTimeDurationItem) ||
                type.getType().equals(AtomicTypes.DurationItem) ||
                type.getType().equals(AtomicTypes.StringItem);
    }

    @Override
    public AtomicItem castAs(AtomicItem atomicItem) {
        return atomicItem.createFromDayTimeDuration(this);
    }

    @Override
    public AtomicItem createFromDuration(DurationItem durationItem) {
        return ItemFactory.getInstance().createDayTimeDurationItem(getDurationFromString(durationItem.serialize(), AtomicTypes.DayTimeDurationItem));
    }

    @Override
    public AtomicItem createFromDayTimeDuration(DayTimeDurationItem dayTimeDurationItem) {
        return dayTimeDurationItem;
    }

    @Override
    public AtomicItem createFromString(StringItem stringItem) {
        return ItemFactory.getInstance().createDayTimeDurationItem(getDurationFromString(stringItem.getStringValue(), AtomicTypes.DayTimeDurationItem));
    }

    @Override
    public Item compareItem(Item other, OperationalExpressionBase.Operator operator, IteratorMetadata metadata) {
        if (!other.isDayTimeDuration() && (!other.isDuration() || other.isYearMonthDuration())) {
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
        return ItemFactory.getInstance().createDayTimeDurationItem(this.getValue().plus(((DayTimeDurationItem) other).getValue()));
    }

    @Override
    public Item subtract(Item other, boolean negated) {
        return ItemFactory.getInstance().createDayTimeDurationItem(this.getValue().minus(((DayTimeDurationItem) other).getValue()));
    }

    @Override
    public Item multiply(Item other) {
        BigDecimal otherBd = other.castToDecimalValue();
        if (otherBd.stripTrailingZeros().scale() <= 0) {
            return ItemFactory.getInstance().createDayTimeDurationItem(this.getValue().multipliedBy(otherBd.intValue()));
        }
        long durationInMillis = this.getValue().toStandardDuration().getMillis();
        long duration_result = otherBd.multiply(BigDecimal.valueOf(durationInMillis)).setScale(0, BigDecimal.ROUND_HALF_UP).longValue();
        Period period_time = new Period(duration_result, PeriodType.dayTime());
        int hours = period_time.getHours();
        BigDecimal[] quotientAndRemainder = BigDecimal.valueOf(hours).divideAndRemainder(BigDecimal.valueOf(24));
        Period result = new Period().withDays(quotientAndRemainder[0].intValue()).withHours(quotientAndRemainder[1].intValue())
                .withMinutes(period_time.getMinutes()).withSeconds(period_time.getSeconds()).withMillis(period_time.getMillis());
        return ItemFactory.getInstance().createDayTimeDurationItem(result);
    }

    @Override
    public Item divide(Item other, boolean inverted) {
        BigDecimal otherBd;
        if (other.isDayTimeDuration()) {
            otherBd = BigDecimal.valueOf(((DayTimeDurationItem) other).getValue().toStandardDuration().getMillis());
        } else {
            otherBd = other.castToDecimalValue();
        }

        long durationInMillis = this.getValue().toStandardDuration().getMillis();
        long duration_result = (BigDecimal.valueOf(durationInMillis)).divide(otherBd, 0, BigDecimal.ROUND_HALF_UP).longValue();
        Period period_time = new Period(duration_result, PeriodType.dayTime());
        int hours = period_time.getHours();
        BigDecimal[] quotientAndRemainder = BigDecimal.valueOf(hours).divideAndRemainder(BigDecimal.valueOf(24));
        Period result = new Period(0L, PeriodType.dayTime()).withDays(quotientAndRemainder[0].intValue()).withHours(quotientAndRemainder[1].intValue())
                .withMinutes(period_time.getMinutes()).withSeconds(period_time.getSeconds()).withMillis(period_time.getMillis());
        return ItemFactory.getInstance().createDayTimeDurationItem(result);
    }
}
