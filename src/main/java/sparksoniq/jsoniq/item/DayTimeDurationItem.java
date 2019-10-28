package sparksoniq.jsoniq.item;

import org.joda.time.DurationFieldType;
import org.joda.time.Instant;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.rumbledb.api.Item;
import sparksoniq.exceptions.UnexpectedTypeException;
import sparksoniq.jsoniq.compiler.translator.expr.operational.base.OperationalExpressionBase;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
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
        this._value = value.normalizedStandard(PeriodType.dayTime());
        isNegative = this._value.toString().charAt(1) == '-';
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
    public boolean isCastableAs(AtomicTypes itemType) {
        return itemType.equals(AtomicTypes.DayTimeDurationItem) ||
                itemType.equals(AtomicTypes.YearMonthDurationItem) ||
                itemType.equals(AtomicTypes.DurationItem) ||
                itemType.equals(AtomicTypes.StringItem);
    }

    @Override
    public Item castAs(AtomicTypes itemType) {
        switch (itemType) {
            case DurationItem:
                return ItemFactory.getInstance().createDurationItem(this.getValue());
            case DayTimeDurationItem:
                return this;
            case YearMonthDurationItem:
                return ItemFactory.getInstance().createYearMonthDurationItem(this.getValue());
            case StringItem:
                return ItemFactory.getInstance().createStringItem(this.serialize());
            default:
                throw new ClassCastException();
        }
    }

    @Override
    public Item compareItem(Item other, OperationalExpressionBase.Operator operator, IteratorMetadata metadata) {
        if (other.isDuration() && !other.isDayTimeDuration() && !other.isYearMonthDuration()) {
            return other.compareItem(this, operator, metadata);
        }
        else if (!other.isDayTimeDuration() && !other.isNull()) {
            throw new UnexpectedTypeException("\"" + ItemTypes.getItemTypeName(this.getClass().getSimpleName())
                    + "\": invalid type: can not compare for equality to type \""
                    + ItemTypes.getItemTypeName(other.getClass().getSimpleName()) + "\"", metadata);
        }
        return operator.apply(this, other);
    }

    @Override
    public Item add(Item other) {
        return ItemFactory.getInstance().createDayTimeDurationItem(this.getValue().plus(other.getDurationValue()));
    }

    @Override
    public Item subtract(Item other) {
        return ItemFactory.getInstance().createDayTimeDurationItem(this.getValue().minus(other.getDurationValue()));
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
    public Item divide(Item other) {
        if (other.isDayTimeDuration()) {
            Instant now  = Instant.now();
            return ItemFactory.getInstance().createDecimalItem(BigDecimal.valueOf(this.getValue().toDurationFrom(now).getMillis() /
                            (double) other.getDurationValue().toDurationFrom(now).getMillis()));
        }
        BigDecimal otherBd = other.castToDecimalValue();
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
