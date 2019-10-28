package sparksoniq.jsoniq.item;


import org.joda.time.*;
import org.rumbledb.api.Item;
import sparksoniq.exceptions.UnexpectedTypeException;
import sparksoniq.jsoniq.compiler.translator.expr.operational.base.OperationalExpressionBase;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.semantics.types.AtomicTypes;
import sparksoniq.semantics.types.ItemType;
import sparksoniq.semantics.types.ItemTypes;

import java.math.BigDecimal;
import java.math.RoundingMode;


public class YearMonthDurationItem extends DurationItem {

    private static final long serialVersionUID = 1L;
    private Period _value;

    public YearMonthDurationItem() {
        super();
    }

    public YearMonthDurationItem(Period value) {
        super();
        this._value = value.normalizedStandard(PeriodType.forFields(new DurationFieldType[]{DurationFieldType.years(), DurationFieldType.months()}));
        isNegative = this._value.toString().charAt(1) == '-';
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
    public boolean isCastableAs(AtomicTypes itemType) {
        return itemType.equals(AtomicTypes.YearMonthDurationItem) ||
                itemType.equals(AtomicTypes.DayTimeDurationItem) ||
                itemType.equals(AtomicTypes.DurationItem) ||
                itemType.equals(AtomicTypes.StringItem);
    }

    @Override
    public Item castAs(AtomicTypes itemType) {
        switch (itemType) {
            case DurationItem:
                return ItemFactory.getInstance().createDurationItem(this.getValue());
            case YearMonthDurationItem:
                return this;
            case DayTimeDurationItem:
                return ItemFactory.getInstance().createDayTimeDurationItem(this.getValue());
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
        else if (!other.isYearMonthDuration() && !other.isNull()) {
            throw new UnexpectedTypeException("\"" + ItemTypes.getItemTypeName(this.getClass().getSimpleName())
                    + "\": invalid type: can not compare for equality to type \""
                    + ItemTypes.getItemTypeName(other.getClass().getSimpleName()) + "\"", metadata);
        }
        return operator.apply(this, other);
    }

    @Override
    public Item add(Item other) {
        return ItemFactory.getInstance().createYearMonthDurationItem(this.getValue().plus(other.getDurationValue()));
    }

    @Override
    public Item subtract(Item other) {
        return ItemFactory.getInstance().createYearMonthDurationItem(this.getValue().minus(other.getDurationValue()));
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
    public Item divide(Item other) {
        int months = this.getValue().getYears() * 12 + this.getValue().getMonths();
        if (other.isYearMonthDuration()) {
            int otherMonths = 12 * other.getDurationValue().getYears() + other.getDurationValue().getMonths();
            return ItemFactory.getInstance().createDecimalItem(BigDecimal.valueOf(months).divide(BigDecimal.valueOf(otherMonths), 16, RoundingMode.HALF_UP));
        }
        BigDecimal otherBd = other.castToDecimalValue();
        BigDecimal[] quotientAndRemainder = (BigDecimal.valueOf(months).divide(otherBd, 0, BigDecimal.ROUND_HALF_UP))
                .divideAndRemainder(BigDecimal.valueOf(12));
        Period newPeriod = new Period(0L, PeriodType.yearMonthDay().withDaysRemoved()).withYears(quotientAndRemainder[0].intValue()).withMonths(quotientAndRemainder[1].intValue());
        return ItemFactory.getInstance().createYearMonthDurationItem(newPeriod);
    }
}
