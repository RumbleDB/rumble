package org.rumbledb.items;

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
import org.rumbledb.types.AtomicItemType;
import org.rumbledb.types.ItemType;

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
    protected Period value;
    boolean isNegative;

    public DurationItem() {
        super();
    }

    public DurationItem(Period value) {
        super();
        this.value = value.normalizedStandard(PeriodType.yearMonthDayTime());
        this.isNegative = this.value.toString().contains("-");
    }

    public Period getValue() {
        return this.value;
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
        return type.equals(AtomicItemType.durationItem) || super.isTypeOf(type);
    }

    @Override
    public boolean isCastableAs(ItemType itemType) {
        return itemType.equals(AtomicItemType.durationItem)
            ||
            itemType.equals(AtomicItemType.yearMonthDurationItem)
            ||
            itemType.equals(AtomicItemType.dayTimeDurationItem)
            ||
            itemType.equals(AtomicItemType.stringItem);
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
        this.value = getDurationFromString(input.readString(), AtomicItemType.durationItem).normalizedStandard(
            PeriodType.yearMonthDayTime()
        );
        this.isNegative = this.value.toString().contains("-");
    }

    private static PeriodFormatter getPeriodFormatter(ItemType durationType) {
        if (durationType.equals(AtomicItemType.durationItem)) {
            return ISOPeriodFormat.standard();
        }
        if (durationType.equals(AtomicItemType.yearMonthDurationItem)) {
            return new PeriodFormatterBuilder().appendLiteral("P")
                .appendYears()
                .appendSuffix("Y")
                .appendMonths()
                .appendSuffix("M")
                .toFormatter();
        }

        if (durationType.equals(AtomicItemType.dayTimeDurationItem)) {
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
        }
        throw new IllegalArgumentException();
    }

    private static PeriodType getPeriodType(ItemType durationType) {
        if (durationType.equals(AtomicItemType.durationItem)) {
            return PeriodType.yearMonthDayTime();
        }
        if (durationType.equals(AtomicItemType.yearMonthDurationItem)) {
            return PeriodType.forFields(
                new DurationFieldType[] { DurationFieldType.years(), DurationFieldType.months() }
            );
        }
        if (durationType.equals(AtomicItemType.dayTimeDurationItem)) {
            return PeriodType.dayTime();
        }
        throw new IllegalArgumentException();
    }

    private static boolean checkInvalidDurationFormat(String duration, ItemType durationType) {
        if (durationType.equals(AtomicItemType.durationItem)) {
            return durationPattern.matcher(duration).matches();
        }
        if (durationType.equals(AtomicItemType.yearMonthDurationItem)) {
            return yearMonthDurationPattern.matcher(duration).matches();
        }
        if (durationType.equals(AtomicItemType.dayTimeDurationItem)) {
            return dayTimeDurationPattern.matcher(duration).matches();
        }
        return false;
    }

    public static Period getDurationFromString(String duration, ItemType durationType)
            throws UnsupportedOperationException,
                IllegalArgumentException {
        if (durationType == null || !checkInvalidDurationFormat(duration, durationType)) {
            throw new IllegalArgumentException();
        }
        boolean isNegative = duration.charAt(0) == '-';
        if (isNegative) {
            duration = duration.substring(1);
        }
        PeriodFormatter pf = getPeriodFormatter(durationType);
        Period period = Period.parse(duration, pf);
        return isNegative
            ? period.negated().normalizedStandard(getPeriodType(durationType))
            : period.normalizedStandard(getPeriodType(durationType));
    }

    @Override
    public int compareTo(Item other) {
        if (other.isNull()) {
            return 1;
        }
        Instant now = new Instant();
        if (other.isDuration()) {
            return this.getDurationValue().toDurationFrom(now).compareTo(other.getDurationValue().toDurationFrom(now));
        }
        throw new IteratorFlowException(
                "Cannot compare item of type "
                    + this.getDynamicType().toString()
                    +
                    " with item of type "
                    + other.getDynamicType().toString()
        );
    }

    @Override
    public ItemType getDynamicType() {
        return AtomicItemType.durationItem;
    }
}
