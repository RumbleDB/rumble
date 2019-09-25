package sparksoniq.jsoniq.item;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.joda.time.Instant;
import org.joda.time.Period;
import org.joda.time.format.ISOPeriodFormat;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;
import org.rumbledb.api.Item;
import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.exceptions.UnexpectedTypeException;
import sparksoniq.jsoniq.compiler.translator.expr.operational.base.OperationalExpressionBase;
import sparksoniq.jsoniq.runtime.iterator.operational.ComparisonOperationIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.semantics.types.AtomicType;
import sparksoniq.semantics.types.AtomicTypes;
import sparksoniq.semantics.types.ItemType;
import sparksoniq.semantics.types.ItemTypes;

import java.util.regex.Matcher;
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
        this._value = value;
    }

    public Period getValue() {
        return _value;
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
            DurationItem otherDuration = (DurationItem) otherItem;
            return this.getValue().toDurationFrom(now).isEqual(otherDuration.getValue().toDurationFrom(now));
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
    public boolean isCastableAs(AtomicType type) {
        if (type.getType().equals(AtomicTypes.DurationItem) ||
                type.getType().equals(AtomicTypes.StringItem)) return true;
        try {
            if (type.getType() == AtomicTypes.YearMonthDurationItem) {
                getDurationFromString(this.getValue().toString(), AtomicTypes.YearMonthDurationItem);
            } else if (type.getType() == AtomicTypes.DayTimeDurationItem) {
                getDurationFromString(this.getValue().toString(), AtomicTypes.DayTimeDurationItem);
            }
        } catch (IllegalArgumentException e) {
            return false;
        }
        return true;
    }

    @Override
    public AtomicItem castAs(AtomicItem atomicItem) {
        return atomicItem.createFromDuration(this);
    }

    @Override
    public AtomicItem createFromDuration(DurationItem durationItem) {
        return durationItem;
    }

    @Override
    public AtomicItem createFromYearMonthDuration(YearMonthDurationItem yearMonthDurationItem) {
        return ItemFactory.getInstance().createDurationItem(getDurationFromString(yearMonthDurationItem.serialize(), AtomicTypes.DurationItem));
    }

    @Override
    public AtomicItem createFromDayTimeDuration(DayTimeDurationItem dayTimeDurationItem) {
        return ItemFactory.getInstance().createDurationItem(getDurationFromString(dayTimeDurationItem.serialize(), AtomicTypes.DurationItem));
    }

    @Override
    public AtomicItem createFromString(StringItem stringItem) {
        return ItemFactory.getInstance().createDurationItem(getDurationFromString(stringItem.getStringValue(), AtomicTypes.DurationItem));
    }

    @Override
    public String serialize() {
        return this.getValue().toString();
    }

    @Override
    public void write(Kryo kryo, Output output) {
        kryo.writeObject(output, this.getValue());
    }

    @Override
    public void read(Kryo kryo, Input input) {
        this._value = kryo.readObject(input, Period.class);
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

    private static boolean checkInvalidDurationFormat(String duration, AtomicTypes durationType) {
        switch (durationType) {
            case DurationItem:
                return Pattern.compile(durationLiteral).matcher(duration).matches();
            case YearMonthDurationItem:
                return Pattern.compile(yearMonthDurationLiteral).matcher(duration).matches();
            case DayTimeDurationItem:
                return Pattern.compile(dayTimeDurationLiteral).matcher(duration).matches();
        }
        throw new IllegalArgumentException();
    }

    public static Period getDurationFromString(String duration, AtomicTypes durationType) throws IllegalArgumentException{
        if (durationType == null || !checkInvalidDurationFormat(duration, durationType)) throw new IllegalArgumentException();
        boolean isNegative = false;
        if (duration.charAt(0) == '-') {
            isNegative = true;
            duration = duration.substring(1);
        }
        PeriodFormatter pf = getPeriodFormatter(durationType);
        Period period = Period.parse(duration, pf);
        return isNegative ? period.negated() : period;
    }

    @Override
    public int compareTo(Item other) {
        Instant now = new Instant();
        if (other.isDuration()) {
            DurationItem otherDuration = (DurationItem) other;
            return this.getValue().toDurationFrom(now).compareTo(otherDuration.getValue().toDurationFrom(now));
        }
        throw new IteratorFlowException("Cannot compare item of type " + ItemTypes.getItemTypeName(this.getClass().getSimpleName()) +
                " with item of type " + ItemTypes.getItemTypeName(other.getClass().getSimpleName()));
    }

    @Override
    public Item compareItem(Item other, OperationalExpressionBase.Operator operator, IteratorMetadata metadata) {
        if (!other.isDuration()) {
            throw new UnexpectedTypeException("\"" + ItemTypes.getItemTypeName(this.getClass().getSimpleName())
                    + "\": invalid type: can not compare for equality to type \""
                    + ItemTypes.getItemTypeName(other.getClass().getSimpleName()) + "\"", metadata);
        }
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
