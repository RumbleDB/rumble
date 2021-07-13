package org.rumbledb.items;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;
import org.joda.time.format.DateTimeParser;
import org.joda.time.format.ISODateTimeFormat;
import org.rumbledb.api.Item;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.comparison.ComparisonExpression.ComparisonOperator;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.runtime.misc.ComparisonIterator;
import org.rumbledb.types.ItemType;

import java.util.regex.Pattern;

import static org.joda.time.format.ISODateTimeFormat.dateElementParser;

public class DateTimeItem implements Item {

    private static final String yearFrag = "((-)?(([1-9]\\d\\d(\\d)+)|(0\\d\\d\\d)))";
    private static final String monthFrag = "((0[1-9])|(1[0-2]))";
    private static final String dayFrag = "((0[1-9])|([1-2]\\d)|(3[0-1]))";
    private static final String hourFrag = "(([0-1]\\d)|(2[0-3]))";
    private static final String minuteFrag = "([0-5]\\d)";
    private static final String secondFrag = "(([0-5]\\d)(\\.(\\d)+)?)";
    private static final String endOfDayFrag = "(24:00:00(\\.(0)+)?)";
    private static final String timezoneFrag = "(Z|([+\\-])(((0\\d|1[0-3]):" + minuteFrag + ")|(14:00)))";
    private static final String dateFrag = "(" + yearFrag + '-' + monthFrag + '-' + dayFrag + ")";
    private static final String timeFrag = "(("
        + hourFrag
        + ":"
        + minuteFrag
        + ":"
        + secondFrag
        + ")|("
        + endOfDayFrag
        + "))";

    private static final String dateTimeLexicalRep = dateFrag + "T" + timeFrag + "(" + timezoneFrag + ")?";
    private static final String dateTimeStampLexicalRep = dateFrag + "T" + timeFrag + timezoneFrag;
    private static final String dateLexicalRep = "(" + dateFrag + "(" + timezoneFrag + ")?)";
    private static final String timeLexicalRep = "(" + timeFrag + "(" + timezoneFrag + ")?)";

    private static final Pattern dateTimePattern = Pattern.compile(dateTimeLexicalRep);
    private static final Pattern dateTimeStampPattern = Pattern.compile(dateTimeStampLexicalRep);
    private static final Pattern datePattern = Pattern.compile(dateLexicalRep);
    private static final Pattern timePattern = Pattern.compile(timeLexicalRep);


    private static final long serialVersionUID = 1L;
    private DateTime value;
    private boolean hasTimeZone = true;

    public DateTimeItem() {
        super();
    }

    DateTimeItem(DateTime value, boolean hasTimeZone) {
        super();
        this.value = value;
        this.hasTimeZone = hasTimeZone;
    }

    DateTimeItem(String dateTimeString) {
        this.value = parseDateTime(dateTimeString, BuiltinTypesCatalogue.dateTimeItem);
        if (!dateTimeString.endsWith("Z") && this.value.getZone() == DateTimeZone.getDefault()) {
            this.hasTimeZone = false;
            this.value = this.value.withZoneRetainFields(DateTimeZone.UTC);
        }
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

    @Override
    public DateTime getDateTimeValue() {
        return this.value;
    }

    @Override
    public String getStringValue() {
        return this.value.toString();
    }

    @Override
    public boolean hasTimeZone() {
        return this.hasTimeZone;
    }

    @Override
    public boolean isDateTime() {
        return true;
    }

    @Override
    public boolean hasDateTime() {
        return true;
    }

    @Override
    public boolean getEffectiveBooleanValue() {
        return false;
    }

    @Override
    public int hashCode() {
        return this.value.hashCode();
    }

    @Override
    public String serialize() {
        String value = this.value.toString();
        String zoneString = this.value.getZone() == DateTimeZone.UTC
            ? "Z"
            : this.value.getZone().toString().equals(DateTimeZone.getDefault().toString())
                ? ""
                : value.substring(value.length() - 6);
        value = value.substring(0, value.length() - zoneString.length());
        value = this.value.getMillisOfSecond() == 0 ? value.substring(0, value.length() - 4) : value;
        return value + (this.hasTimeZone ? zoneString : "");
    }

    @Override
    public void write(Kryo kryo, Output output) {
        output.writeLong(this.value.getMillis(), true);
        output.writeBoolean(this.hasTimeZone);
        output.writeString(this.value.getZone().getID());
    }

    @Override
    public void read(Kryo kryo, Input input) {
        Long millis = input.readLong(true);
        this.hasTimeZone = input.readBoolean();
        DateTimeZone zone = DateTimeZone.forID(input.readString());
        this.value = new DateTime(millis, zone);
    }

    static DateTimeFormatter getDateTimeFormatter(ItemType dateTimeType) {
        if (dateTimeType.equals(BuiltinTypesCatalogue.dateTimeStampItem)) {
            return ISODateTimeFormat.dateTimeParser().withOffsetParsed();
        }
        if (dateTimeType.equals(BuiltinTypesCatalogue.dateTimeItem)) {
            return ISODateTimeFormat.dateTimeParser().withOffsetParsed();
        }
        if (dateTimeType.equals(BuiltinTypesCatalogue.dateItem)) {
            DateTimeParser dtParser = new DateTimeFormatterBuilder().appendOptional(
                ((new DateTimeFormatterBuilder()).appendTimeZoneOffset("Z", true, 2, 4).toFormatter()).getParser()
            ).toParser();
            return (new DateTimeFormatterBuilder()).append(dateElementParser())
                .appendOptional(dtParser)
                .toFormatter()
                .withOffsetParsed();
        }
        if (dateTimeType.equals(BuiltinTypesCatalogue.timeItem)) {
            return ISODateTimeFormat.timeParser().withOffsetParsed();
        }
        throw new IllegalArgumentException();
    }

    static boolean checkInvalidDateTimeFormat(String dateTime, ItemType dateTimeType) {
        if (dateTimeType.equals(BuiltinTypesCatalogue.dateTimeStampItem)) {
            return dateTimeStampPattern.matcher(dateTime).matches();
        }
        if (dateTimeType.equals(BuiltinTypesCatalogue.dateTimeItem)) {
            return dateTimePattern.matcher(dateTime).matches();
        }
        if (dateTimeType.equals(BuiltinTypesCatalogue.dateItem)) {
            return datePattern.matcher(dateTime).matches();
        }
        if (dateTimeType.equals(BuiltinTypesCatalogue.timeItem)) {
            return timePattern.matcher(dateTime).matches();
        }
        return false;
    }

    private static String fixEndOfDay(String dateTime) {
        String endOfDay = "24:00:00";
        String startOfDay = "00:00:00";
        if (dateTime.contains(endOfDay)) {
            if (dateTime.indexOf(endOfDay) == 0) {
                return startOfDay;
            }
            int indexOfT = dateTime.indexOf('T');
            if (
                indexOfT < 1
                    || indexOfT != dateTime.indexOf(endOfDay) - 1
                    || !Character.isDigit(dateTime.charAt(indexOfT - 1))
            ) {
                throw new IllegalArgumentException();
            }
            int dayValue;
            try {
                dayValue = Character.getNumericValue(dateTime.charAt(indexOfT - 1));
            } catch (Exception e) {
                throw new IllegalArgumentException();
            }
            return dateTime.substring(0, indexOfT - 1)
                +
                (dayValue + 1)
                + "T"
                + startOfDay
                +
                dateTime.substring(indexOfT + endOfDay.length() + 1);
        }
        return dateTime;
    }

    static DateTime parseDateTime(String dateTime, ItemType dateTimeType) throws IllegalArgumentException {
        if (!checkInvalidDateTimeFormat(dateTime, dateTimeType)) {
            throw new IllegalArgumentException();
        }
        dateTime = fixEndOfDay(dateTime);
        return DateTime.parse(dateTime, getDateTimeFormatter(dateTimeType));
    }

    @Override
    public ItemType getDynamicType() {
        return BuiltinTypesCatalogue.dateTimeItem;
    }

    @Override
    public boolean isAtomic() {
        return true;
    }
}
