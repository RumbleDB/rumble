package sparksoniq.jsoniq.item;

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
import java.util.logging.Logger;

public class DayTimeDurationItem extends DurationItem {

    private static final long serialVersionUID = 1L;
    private Period _value;
    private static Logger log;

    public DayTimeDurationItem() {
        super();
    }

    public DayTimeDurationItem(Period value) {
        super();
        this._value = value.normalizedStandard(PeriodType.dayTime());
        isNegative = this._value.toString().charAt(1) == '-';
        log = Logger.getLogger(DayTimeDurationItem.class.getName());
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
        log.info("OtherBD " + otherBd);
        long durationInMillis = this.getValue().toStandardDuration().getMillis();
        log.info("durationInMillis " + durationInMillis);
        BigDecimal duration_result_1 = otherBd.multiply(BigDecimal.valueOf(durationInMillis));
        log.info("1 duration result " + duration_result_1);
        BigDecimal duration_result_2 = duration_result_1.setScale(16, BigDecimal.ROUND_HALF_UP);
        log.info("2 duration result " + duration_result_2);
        long duration_result = duration_result_2.longValue();
        log.info("duration result " + duration_result);
        Period period_time = new Period(duration_result, PeriodType.dayTime());
        log.info("period " + period_time.toString());
        int hours = period_time.getHours();
        double hoursInDay = 24.0;
        BigDecimal[] quotientAndRemainder = BigDecimal.valueOf(hours).divideAndRemainder(BigDecimal.valueOf(hoursInDay));
        log.info("DAYS " + quotientAndRemainder[0].intValue());
        log.info("DAYS EXACT " + quotientAndRemainder[0].intValueExact());
        log.info("Hours " + quotientAndRemainder[1].intValue());
        Period result_1 = new Period(0L, PeriodType.dayTime());
        log.info("Period 1 " + result_1);
        Period result = result_1.withDays(quotientAndRemainder[0].intValueExact()).withHours(quotientAndRemainder[1].intValue())
                .withMinutes(period_time.getMinutes()).withSeconds(period_time.getSeconds()).withMillis(period_time.getMillis());
        log.info("RESULT " + result);
        log.info("\n");
        log.info("\n");
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
        log.info("OtherBD " + otherBd);
        long durationInMillis = this.getValue().toStandardDuration().getMillis();
        log.info("durationInMillis " + durationInMillis);
        BigDecimal duration_result_1 = (BigDecimal.valueOf(durationInMillis)).divide(otherBd, 16, BigDecimal.ROUND_HALF_UP);
        log.info("1 duration result " + duration_result_1);
        long duration_result = duration_result_1.longValue();
        log.info("duration result " + duration_result);
        Period period_time = new Period(duration_result, PeriodType.dayTime());
        log.info("period " + period_time.toString());
        int hours = period_time.getHours();
        double hoursInDay = 24.0;
        BigDecimal[] quotientAndRemainder = BigDecimal.valueOf(hours).divideAndRemainder(BigDecimal.valueOf(hoursInDay));
        log.info("DAYS " + quotientAndRemainder[0].intValue());
        log.info("DAYS EXACT " + quotientAndRemainder[0].intValueExact());
        log.info("Hours " + quotientAndRemainder[1].intValue());
        Period result_1 = new Period(0L, PeriodType.dayTime());
        log.info("Period 1 " + result_1);
        Period result = result_1.withDays(quotientAndRemainder[0].intValueExact()).withHours(quotientAndRemainder[1].intValue())
                .withMinutes(period_time.getMinutes()).withSeconds(period_time.getSeconds()).withMillis(period_time.getMillis());
        log.info("RESULT " + result);
        return ItemFactory.getInstance().createDayTimeDurationItem(result);
    }
}
