package org.rumbledb.items;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.time.Duration;
import java.time.Period;
import java.time.format.DateTimeParseException;
import org.rumbledb.api.Item;
import org.rumbledb.exceptions.DurationOverflowOrUnderflow;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.comparison.ComparisonExpression.ComparisonOperator;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.runtime.misc.ComparisonIterator;
import org.rumbledb.types.ItemType;

import java.util.regex.Pattern;

public class DurationItem implements Item {

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
        this.value = value;
        this.isNegative = this.value.toString().contains("-");
    }

    @Override
    public boolean equals(Object otherItem) {
        if (otherItem instanceof Item) {
            long c = ComparisonIterator.compareItems(
                this,
                (Item) otherItem,
                ComparisonOperator.VC_EQ,
                ExceptionMetadata.EMPTY_METADATA
            );
            return c == 0;
        }
        return false;
    }

    public Period getValue() {
        return this.value;
    }

    public Period getPeriodValue() {
        return this.getValue();
    }

    @Override
    public String getStringValue() {
        if (this.isNegative) {
            return '-' + this.getValue().negated().toString();
        }
        return this.getValue().toString();
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
    public int hashCode() {
        return Long.hashCode(this.getValue().toTotalMonths());
    }

    @Override
    public void write(Kryo kryo, Output output) {
        output.writeString(this.getStringValue());
    }

    @Override
    public void read(Kryo kryo, Input input) {
        this.value = getDurationFromString(input.readString(), BuiltinTypesCatalogue.durationItem);
        this.isNegative = this.value.toString().contains("-");
    }

    // private static PeriodFormatter getPeriodFormatter(ItemType durationType) {
    // if (durationType.equals(BuiltinTypesCatalogue.durationItem)) {
    // return ISOPeriodFormat.standard();
    // }
    // if (durationType.equals(BuiltinTypesCatalogue.yearMonthDurationItem)) {
    // return new PeriodFormatterBuilder().appendLiteral("P")
    // .appendYears()
    // .appendSuffix("Y")
    // .appendMonths()
    // .appendSuffix("M")
    // .toFormatter();
    // }

    // if (durationType.equals(BuiltinTypesCatalogue.dayTimeDurationItem)) {
    // return new PeriodFormatterBuilder().appendLiteral("P")
    // .appendDays()
    // .appendSuffix("D")
    // .appendSeparatorIfFieldsAfter("T")
    // .appendHours()
    // .appendSuffix("H")
    // .appendMinutes()
    // .appendSuffix("M")
    // .appendSecondsWithOptionalMillis()
    // .appendSuffix("S")
    // .toFormatter();
    // }
    // throw new IllegalArgumentException();
    // }

    // private static PeriodType getPeriodType(ItemType durationType) {
    // if (durationType.equals(BuiltinTypesCatalogue.durationItem)) {
    // return PeriodType.yearMonthDayTime();
    // }
    // if (durationType.equals(BuiltinTypesCatalogue.yearMonthDurationItem)) {
    // return PeriodType.forFields(
    // new DurationFieldType[] { DurationFieldType.years(), DurationFieldType.months() }
    // );
    // }
    // if (durationType.equals(BuiltinTypesCatalogue.dayTimeDurationItem)) {
    // return PeriodType.dayTime();
    // }
    // throw new IllegalArgumentException();
    // }

    private static boolean checkInvalidDurationFormat(String duration, ItemType durationType) {
        if (durationType.equals(BuiltinTypesCatalogue.durationItem)) {
            return durationPattern.matcher(duration).matches();
        }
        if (durationType.equals(BuiltinTypesCatalogue.yearMonthDurationItem)) {
            return yearMonthDurationPattern.matcher(duration).matches();
        }
        if (durationType.equals(BuiltinTypesCatalogue.dayTimeDurationItem)) {
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
        try {
            Period period = Period.parse(duration);
            return isNegative ? period.negated() : period;
        } catch (DateTimeParseException e) {
            try {
                Period period = Period.ofDays(Math.toIntExact(Duration.parse(duration).toDays())); // very crude way to map from duration to period
                return isNegative ? period.negated() : period;
            } catch (DateTimeParseException e2) {
                throw new DurationOverflowOrUnderflow(
                        "Invalid duration: \"" + duration + "\"",
                        ExceptionMetadata.EMPTY_METADATA
                );
            }
        }
    }

    @Override
    public ItemType getDynamicType() {
        return BuiltinTypesCatalogue.durationItem;
    }
}
