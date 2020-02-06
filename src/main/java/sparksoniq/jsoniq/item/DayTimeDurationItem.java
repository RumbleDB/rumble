package sparksoniq.jsoniq.item;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import org.joda.time.Instant;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.rumbledb.api.Item;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.expressions.operational.base.OperationalExpressionBase;

import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.semantics.types.AtomicTypes;
import sparksoniq.semantics.types.ItemType;
import sparksoniq.semantics.types.ItemTypes;

import java.math.BigDecimal;

public class DayTimeDurationItem extends DurationItem {

    private static final long serialVersionUID = 1L;

    public DayTimeDurationItem() {
        super();
    }

    public DayTimeDurationItem(Period value) {
        super();
        this._value = value.normalizedStandard(PeriodType.dayTime());
        isNegative = this._value.toString().contains("-");
    }

    @Override
    public Period getValue() {
        return this._value;
    }

    @Override
    public Period getDurationValue() {
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
    public void read(Kryo kryo, Input input) {
        this._value = getDurationFromString(input.readString(), AtomicTypes.DayTimeDurationItem).normalizedStandard(
            PeriodType.dayTime()
        );
        this.isNegative = this._value.toString().contains("-");
    }

    @Override
    public boolean isCastableAs(AtomicTypes itemType) {
        return itemType.equals(AtomicTypes.DayTimeDurationItem)
            ||
            itemType.equals(AtomicTypes.YearMonthDurationItem)
            ||
            itemType.equals(AtomicTypes.DurationItem)
            ||
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
        } else if (!other.isDayTimeDuration() && !other.isNull()) {
            throw new UnexpectedTypeException(
                    "\""
                        + ItemTypes.getItemTypeName(this.getClass().getSimpleName())
                        + "\": invalid type: can not compare for equality to type \""
                        + ItemTypes.getItemTypeName(other.getClass().getSimpleName())
                        + "\"",
                    metadata
            );
        }
        return operator.apply(this, other);
    }

    @Override
    public Item add(Item other) {
        if (other.isDateTime())
            return ItemFactory.getInstance()
                .createDateTimeItem(other.getDateTimeValue().plus(this.getValue()), other.hasTimeZone());
        if (other.isDate())
            return ItemFactory.getInstance()
                .createDateItem(other.getDateTimeValue().plus(this.getValue()), other.hasDateTime());
        if (other.isTime())
            return ItemFactory.getInstance()
                .createTimeItem(other.getDateTimeValue().plus(this.getValue()), other.hasDateTime());
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
}
