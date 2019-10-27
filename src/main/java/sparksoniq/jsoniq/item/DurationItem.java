package sparksoniq.jsoniq.item;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.joda.time.DurationFieldType;
import org.joda.time.Instant;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.format.ISOPeriodFormat;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;
import org.rumbledb.api.Item;
import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.exceptions.UnexpectedTypeException;
import sparksoniq.jsoniq.compiler.translator.expr.operational.base.OperationalExpressionBase;
import sparksoniq.jsoniq.runtime.iterator.operational.ComparisonOperationIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.semantics.types.AtomicTypes;
import sparksoniq.semantics.types.ItemType;
import sparksoniq.semantics.types.ItemTypes;

import java.util.regex.Pattern;

public class DurationItem extends AtomicItem {

    private static final String prefix = "(-)?P";
    private static final String duYearFrag = "(\\d)+Y";
    private static final String duMonthFrag = "(\\d)+M";
    private static final String duDayFrag = "(\\d)+D";
    private static final String duHourFrag = "(\\d)+H";
    private static final String duMinuteFrag = "(\\d)+M";
    private static final String duSecondFrag = "(((\\d)+)|(\\.(\\d)+)|((\\d)+\\.(\\d)+))S";

    private static final String duYearMonthFrag = "((" + duYearFrag + "(" + duMonthFrag + ")?)|" + duMonthFrag + ")";
    private static final String duTimeFrag = "T((" + duHourFrag + "(" + duMinuteFrag + ")?" + "(" + duSecondFrag + ")?)|" +
            "(" + duMinuteFrag + "(" + duSecondFrag + ")?)|" + duSecondFrag + ")";
    private static final String duDayTimeFrag = "((" + duDayFrag + "(" + duTimeFrag + ")?)|" + duTimeFrag + ")";
    private static final String durationLiteral = prefix + "((" + duYearMonthFrag + "(" + duDayTimeFrag + ")?)|" + duDayTimeFrag +")";
    private static final String yearMonthDurationLiteral = prefix + duYearMonthFrag;
    private static final String dayTimeDurationLiteral = prefix + duDayTimeFrag;

    private static final long serialVersionUID = 1L;
    private Period _value;

    public DurationItem() {
        super();
    }

    public DurationItem(Period value) {
        super();
        this._value = value.normalizedStandard(PeriodType.yearMonthDayTime());
    }

    public Period getValue() {
        return this._value;
    }

    public Period getDurationValue() {
        return this.getValue();
    }

    @Override
    public boolean isAtomic() {
        return true;
    }

    @Override
    public boolean isDuration() {
        return true;
    }

    @Override
    public boolean getEffectiveBooleanValue() {
        return false;
    }

    @Override
    public boolean equals(Object otherObject) {
        if (!(otherObject instanceof Item)) {
            return false;
        }
        Item otherItem = (Item) otherObject;
        Instant now = new Instant();
        if (otherItem.isDuration()) {
            return this.getDurationValue().toDurationFrom(now).isEqual(otherItem.getDurationValue().toDurationFrom(now));
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.getValue().hashCode();
    }

    @Override
    public boolean isTypeOf(ItemType type) {
        return type.getType().equals(ItemTypes.DurationItem) || super.isTypeOf(type);
    }

    @Override
    public boolean isCastableAs(AtomicTypes itemType) {
        if (itemType.equals(AtomicTypes.DurationItem) ||
                itemType.equals(AtomicTypes.StringItem)) return true;
        try {
            if (itemType == AtomicTypes.YearMonthDurationItem) {
                getDurationFromString(this.getValue().toString(), AtomicTypes.YearMonthDurationItem);
            } else if (itemType == AtomicTypes.DayTimeDurationItem) {
                getDurationFromString(this.getValue().toString(), AtomicTypes.DayTimeDurationItem);
            }
        } catch (IllegalArgumentException e) {
            return false;
        }
        return true;
    }

    @Override
    public Item castAs(AtomicTypes itemType) {
        switch (itemType) {
            case DurationItem:
                return this;
            case YearMonthDurationItem:
                return ItemFactory.getInstance().createYearMonthDurationItem(getDurationFromString(this.serialize(), AtomicTypes.YearMonthDurationItem));
            case DayTimeDurationItem:
                return ItemFactory.getInstance().createDayTimeDurationItem(getDurationFromString(this.serialize(), AtomicTypes.DayTimeDurationItem));
            case StringItem:
                return ItemFactory.getInstance().createStringItem(this.serialize());
            default:
                throw new ClassCastException();
        }
    }

    @Override
    public String serialize() {
        return this.getValue().toString();
    }

    @Override
    public void write(Kryo kryo, Output output) {
        kryo.writeObject(output, this.serialize());
    }

    @Override
    public void read(Kryo kryo, Input input) {
        this._value = Period.parse(kryo.readObject(input, String.class), DurationItem.getPeriodFormatter(AtomicTypes.DurationItem));
    }

    private static PeriodFormatter getPeriodFormatter(AtomicTypes durationType) {
        switch (durationType) {
            case DurationItem:
                return ISOPeriodFormat.standard();
            case YearMonthDurationItem:
                return new PeriodFormatterBuilder().
                        appendLiteral("P").appendYears().appendSuffix("Y").appendMonths().appendSuffix("M").toFormatter();
            case DayTimeDurationItem:
                return new PeriodFormatterBuilder().
                        appendLiteral("P").appendDays().appendSuffix("D").appendSeparatorIfFieldsAfter("T").
                        appendHours().appendSuffix("H").appendMinutes().appendSuffix("M")
                        .appendSecondsWithOptionalMillis().appendSuffix("S").toFormatter();
            default:
                throw new IllegalArgumentException();
        }
    }

    private static PeriodType getPeriodType(AtomicTypes durationType) {
        switch (durationType) {
            case DurationItem:
                return PeriodType.yearMonthDayTime();
            case YearMonthDurationItem:
                return PeriodType.forFields(new DurationFieldType[]{DurationFieldType.years(), DurationFieldType.months()});
            case DayTimeDurationItem:
                return PeriodType.dayTime();
            default:
                throw new IllegalArgumentException();
        }
    }

    private static boolean checkInvalidDurationFormat(String duration, AtomicTypes durationType) {
        switch (durationType) {
            case DurationItem:
                return Pattern.compile(durationLiteral).matcher(duration).matches();
            case YearMonthDurationItem:
                return Pattern.compile(yearMonthDurationLiteral).matcher(duration).matches();
            case DayTimeDurationItem:
                return Pattern.compile(dayTimeDurationLiteral).matcher(duration).matches();
        }
        return false;
    }

    public static Period getDurationFromString(String duration, AtomicTypes durationType) throws UnsupportedOperationException, IllegalArgumentException {
        if (durationType == null || !checkInvalidDurationFormat(duration, durationType)) throw new IllegalArgumentException();
        boolean isNegative = false;
        if (duration.charAt(0) == '-') {
            isNegative = true;
            duration = duration.substring(1);
        }
        PeriodFormatter pf = getPeriodFormatter(durationType);
        Period period = Period.parse(duration, pf);
        return isNegative ?
                period.negated().normalizedStandard(getPeriodType(durationType)) :
                period.normalizedStandard(getPeriodType(durationType));
    }

    @Override
    public int compareTo(Item other) {
        if (other.isNull()) return 1;
        Instant now = new Instant();
        if (other.isDuration()) {
            return this.getDurationValue().toDurationFrom(now).compareTo(other.getDurationValue().toDurationFrom(now));
        }
        throw new IteratorFlowException("Cannot compare item of type " + ItemTypes.getItemTypeName(this.getClass().getSimpleName()) +
                " with item of type " + ItemTypes.getItemTypeName(other.getClass().getSimpleName()));
    }

    @Override
    public Item compareItem(Item other, OperationalExpressionBase.Operator operator, IteratorMetadata metadata) {
        if (!other.isDuration() && !other.isNull()) {
            throw new UnexpectedTypeException("\"" + ItemTypes.getItemTypeName(this.getClass().getSimpleName())
                    + "\": invalid type: can not compare for equality to type \""
                    + ItemTypes.getItemTypeName(other.getClass().getSimpleName()) + "\"", metadata);
        }
        if (other.isNull()) return operator.apply(this, other);
        switch (operator) {
            case VC_EQ:
            case GC_EQ:
            case VC_NE:
            case GC_NE:
                return operator.apply(this, other);
        }
        throw new UnexpectedTypeException("\"" + ItemTypes.getItemTypeName(this.getClass().getSimpleName())
                + "\": invalid type: can not compare for equality to type \""
                + ItemTypes.getItemTypeName(other.getClass().getSimpleName()) + "\"", metadata);
    }
}
