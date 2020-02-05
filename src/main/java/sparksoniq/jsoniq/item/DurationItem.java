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
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.exceptions.UnexpectedTypeException;

import sparksoniq.jsoniq.compiler.translator.expr.operational.base.OperationalExpressionBase;
import org.rumbledb.exceptions.ExceptionMetadata;
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
    private static final String duTimeFrag = "T(("
        + duHourFrag
        + "("
        + duMinuteFrag
        + ")?"
        + "("
        + duSecondFrag
        + ")?)|"
        +
        "("
        + duMinuteFrag
        + "("
        + duSecondFrag
        + ")?)|"
        + duSecondFrag
        + ")";
    private static final String duDayTimeFrag = "((" + duDayFrag + "(" + duTimeFrag + ")?)|" + duTimeFrag + ")";
    private static final String durationLiteral = prefix
        + "(("
        + duYearMonthFrag
        + "("
        + duDayTimeFrag
        + ")?)|"
        + duDayTimeFrag
        + ")";
    private static final String yearMonthDurationLiteral = prefix + duYearMonthFrag;
    private static final String dayTimeDurationLiteral = prefix + duDayTimeFrag;
    private static final Pattern durationPattern = Pattern.compile(durationLiteral);
    private static final Pattern yearMonthDurationPattern = Pattern.compile(yearMonthDurationLiteral);
    private static final Pattern dayTimeDurationPattern = Pattern.compile(dayTimeDurationLiteral);


    private static final long serialVersionUID = 1L;
    protected Period _value;
    boolean isNegative;

    public DurationItem() {
        super();
    }

    public DurationItem(Period value) {
        super();
        this._value = value.normalizedStandard(PeriodType.yearMonthDayTime());
        isNegative = this._value.toString().contains("-");
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
            return this.getDurationValue()
                .toDurationFrom(now)
                .isEqual(otherItem.getDurationValue().toDurationFrom(now));
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(this.getValue().toDurationFrom(Instant.now()).getMillis());
    }

    @Override
    public boolean isTypeOf(ItemType type) {
        return type.getType().equals(ItemTypes.DurationItem) || super.isTypeOf(type);
    }

    @Override
    public boolean isCastableAs(AtomicTypes itemType) {
        return itemType.equals(AtomicTypes.DurationItem)
            ||
            itemType.equals(AtomicTypes.YearMonthDurationItem)
            ||
            itemType.equals(AtomicTypes.DayTimeDurationItem)
            ||
            itemType.equals(AtomicTypes.StringItem);
    }

    @Override
    public Item castAs(AtomicTypes itemType) {
        switch (itemType) {
            case DurationItem:
                return this;
            case YearMonthDurationItem:
                return ItemFactory.getInstance().createYearMonthDurationItem(this.getValue());
            case DayTimeDurationItem:
                return ItemFactory.getInstance().createDayTimeDurationItem(this.getValue());
            case StringItem:
                return ItemFactory.getInstance().createStringItem(this.serialize());
            default:
                throw new ClassCastException();
        }
    }

    @Override
    public String serialize() {
        if (this.isNegative) {
            return '-' + this.getValue().negated().toString();
        }
        return this.getValue().toString();
    }

    @Override
    public void write(Kryo kryo, Output output) {
        output.writeString(this.serialize());
    }

    @Override
    public void read(Kryo kryo, Input input) {
        this._value = getDurationFromString(input.readString(), AtomicTypes.DurationItem).normalizedStandard(
            PeriodType.yearMonthDayTime()
        );
        isNegative = this._value.toString().contains("-");
    }

    private static PeriodFormatter getPeriodFormatter(AtomicTypes durationType) {
        switch (durationType) {
            case DurationItem:
                return ISOPeriodFormat.standard();
            case YearMonthDurationItem:
                return new PeriodFormatterBuilder().appendLiteral("P")
                    .appendYears()
                    .appendSuffix("Y")
                    .appendMonths()
                    .appendSuffix("M")
                    .toFormatter();
            case DayTimeDurationItem:
                return new PeriodFormatterBuilder().appendLiteral("P")
                    .appendDays()
                    .appendSuffix("D")
                    .appendSeparatorIfFieldsAfter("T")
                    .appendHours()
                    .appendSuffix("H")
                    .appendMinutes()
                    .appendSuffix("M")
                    .appendSecondsWithOptionalMillis()
                    .appendSuffix("S")
                    .toFormatter();
            default:
                throw new IllegalArgumentException();
        }
    }

    private static PeriodType getPeriodType(AtomicTypes durationType) {
        switch (durationType) {
            case DurationItem:
                return PeriodType.yearMonthDayTime();
            case YearMonthDurationItem:
                return PeriodType.forFields(
                    new DurationFieldType[] { DurationFieldType.years(), DurationFieldType.months() }
                );
            case DayTimeDurationItem:
                return PeriodType.dayTime();
            default:
                throw new IllegalArgumentException();
        }
    }

    private static boolean checkInvalidDurationFormat(String duration, AtomicTypes durationType) {
        switch (durationType) {
            case DurationItem:
                return durationPattern.matcher(duration).matches();
            case YearMonthDurationItem:
                return yearMonthDurationPattern.matcher(duration).matches();
            case DayTimeDurationItem:
                return dayTimeDurationPattern.matcher(duration).matches();
        }
        return false;
    }

    public static Period getDurationFromString(String duration, AtomicTypes durationType)
            throws UnsupportedOperationException,
                IllegalArgumentException {
        if (durationType == null || !checkInvalidDurationFormat(duration, durationType))
            throw new IllegalArgumentException();
        boolean isNegative = duration.charAt(0) == '-';
        if (isNegative)
            duration = duration.substring(1);
        PeriodFormatter pf = getPeriodFormatter(durationType);
        Period period = Period.parse(duration, pf);
        return isNegative
            ? period.negated().normalizedStandard(getPeriodType(durationType))
            : period.normalizedStandard(getPeriodType(durationType));
    }

    @Override
    public int compareTo(Item other) {
        if (other.isNull())
            return 1;
        Instant now = new Instant();
        if (other.isDuration()) {
            return this.getDurationValue().toDurationFrom(now).compareTo(other.getDurationValue().toDurationFrom(now));
        }
        throw new IteratorFlowException(
                "Cannot compare item of type "
                    + ItemTypes.getItemTypeName(this.getClass().getSimpleName())
                    +
                    " with item of type "
                    + ItemTypes.getItemTypeName(other.getClass().getSimpleName())
        );
    }

    @Override
    public Item compareItem(Item other, OperationalExpressionBase.Operator operator, ExceptionMetadata metadata) {
        if (!other.isDuration() && !other.isNull()) {
            throw new UnexpectedTypeException(
                    "\""
                        + ItemTypes.getItemTypeName(this.getClass().getSimpleName())
                        + "\": invalid type: can not compare for equality to type \""
                        + ItemTypes.getItemTypeName(other.getClass().getSimpleName())
                        + "\"",
                    metadata
            );
        }
        if (other.isNull())
            return operator.apply(this, other);
        switch (operator) {
            case VC_EQ:
            case GC_EQ:
            case VC_NE:
            case GC_NE:
                return operator.apply(this, other);
        }
        throw new UnexpectedTypeException(
                "\""
                    + ItemTypes.getItemTypeName(this.getClass().getSimpleName())
                    + "\": invalid type: can not compare for equality to type \""
                    + ItemTypes.getItemTypeName(other.getClass().getSimpleName())
                    + "\"",
                metadata
        );
    }
}
