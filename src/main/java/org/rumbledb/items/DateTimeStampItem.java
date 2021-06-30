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

public class DateTimeStampItem implements Item {

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
    private static final String dateTimeStampLexicalRep = dateFrag + "T" + timeFrag + "(" + timezoneFrag + ")";
    private static final String dateLexicalRep = "(" + dateFrag + "(" + timezoneFrag + ")?)";
    private static final String timeLexicalRep = "(" + timeFrag + "(" + timezoneFrag + ")?)";

    private static final Pattern dateTimePattern = Pattern.compile(dateTimeLexicalRep);
    private static final Pattern dateTimeStampPattern = Pattern.compile(dateTimeStampLexicalRep);
    private static final Pattern datePattern = Pattern.compile(dateLexicalRep);
    private static final Pattern timePattern = Pattern.compile(timeLexicalRep);


    private static final long serialVersionUID = 1L;
    private DateTime value;

    public DateTimeStampItem() {
        super();
    }

    DateTimeStampItem(DateTime value) {
        super();
        this.value = value;
    }

    DateTimeStampItem(String dateTimeStampString) {
        this.value = parseDateTime(dateTimeStampString, BuiltinTypesCatalogue.dateTimeStampItem);
        // DateTimeStamp requires the time zone expression at the end of the value
        if (!dateTimeStampString.endsWith("Z") && this.value.getZone() == DateTimeZone.getDefault()) {
            return;
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

    public DateTime getValue() {
        return this.value;
    }

    @Override
    public DateTime getDateTimeValue() {
        return this.getValue();
    }

    @Override
    public String getStringValue() {
        return this.value.toString();
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
        return this.getValue().hashCode();
    }

    @Override
    public String serialize() {
        String value = this.getValue().toString();
        String zoneString = this.getValue().getZone() == DateTimeZone.UTC
            ? "Z"
            : this.getValue().getZone().toString().equals(DateTimeZone.getDefault().toString())
                ? ""
                : value.substring(value.length() - 6);
        value = value.substring(0, value.length() - zoneString.length());
        value = this.getValue().getMillisOfSecond() == 0 ? value.substring(0, value.length() - 4) : value;
        return value + zoneString;
    }

    @Override
    public void write(Kryo kryo, Output output) {
        output.writeLong(this.getDateTimeValue().getMillis(), true);
        output.writeString(this.getDateTimeValue().getZone().getID());
    }

    @Override
    public void read(Kryo kryo, Input input) {
        Long millis = input.readLong(true);
        DateTimeZone zone = DateTimeZone.forID(input.readString());
        this.value = new DateTime(millis, zone);
    }

    private static DateTimeFormatter getDateTimeStampFormatter(ItemType dateTimeStampType) {
        if (dateTimeStampType.equals(BuiltinTypesCatalogue.dateTimeStampItem)) {
            return ISODateTimeFormat.dateTimeParser().withOffsetParsed();
        }
        if (dateTimeStampType.equals(BuiltinTypesCatalogue.dateTimeItem)) {
            return ISODateTimeFormat.dateTimeParser().withOffsetParsed();
        }
        if (dateTimeStampType.equals(BuiltinTypesCatalogue.dateItem)) {
            DateTimeParser dtParser = new DateTimeFormatterBuilder().appendOptional(
                ((new DateTimeFormatterBuilder()).appendTimeZoneOffset("Z", true, 2, 4).toFormatter()).getParser()
            ).toParser();
            return (new DateTimeFormatterBuilder()).append(dateElementParser())
                .appendOptional(dtParser)
                .toFormatter()
                .withOffsetParsed();
        }
        if (dateTimeStampType.equals(BuiltinTypesCatalogue.timeItem)) {
            return ISODateTimeFormat.timeParser().withOffsetParsed();
        }
        throw new IllegalArgumentException();
    }

    private static boolean checkInvalidDateTimeFormat(String dateTimeStamp, ItemType dateTimeStampType) {
        if (dateTimeStampType.equals(BuiltinTypesCatalogue.dateTimeStampItem)) {
            return dateTimeStampPattern.matcher(dateTimeStamp).matches();
        }
        if (dateTimeStampType.equals(BuiltinTypesCatalogue.dateTimeItem)) {
            return dateTimePattern.matcher(dateTimeStamp).matches();
        }
        if (dateTimeStampType.equals(BuiltinTypesCatalogue.dateItem)) {
            return datePattern.matcher(dateTimeStamp).matches();
        }
        if (dateTimeStampType.equals(BuiltinTypesCatalogue.timeItem)) {
            return timePattern.matcher(dateTimeStamp).matches();
        }
        return false;
    }

    private static String fixEndOfDay(String dateTimeStamp) {
        String endOfDay = "24:00:00";
        String startOfDay = "00:00:00";
        if (dateTimeStamp.contains(endOfDay)) {
            if (dateTimeStamp.indexOf(endOfDay) == 0) {
                return startOfDay;
            }
            int indexOfT = dateTimeStamp.indexOf('T');
            if (
                indexOfT < 1
                    || indexOfT != dateTimeStamp.indexOf(endOfDay) - 1
                    || !Character.isDigit(dateTimeStamp.charAt(indexOfT - 1))
            ) {
                throw new IllegalArgumentException();
            }
            int dayValue;
            try {
                dayValue = Character.getNumericValue(dateTimeStamp.charAt(indexOfT - 1));
            } catch (Exception e) {
                throw new IllegalArgumentException();
            }
            return dateTimeStamp.substring(0, indexOfT - 1)
                +
                (dayValue + 1)
                + "T"
                + startOfDay
                +
                dateTimeStamp.substring(indexOfT + endOfDay.length() + 1);
        }
        return dateTimeStamp;
    }

    static DateTime parseDateTime(String dateTimeStamp, ItemType dateTimeStampType) throws IllegalArgumentException {
        if (!checkInvalidDateTimeFormat(dateTimeStamp, dateTimeStampType)) {
            throw new IllegalArgumentException();
        }
        dateTimeStamp = fixEndOfDay(dateTimeStamp);
        return DateTime.parse(dateTimeStamp, getDateTimeStampFormatter(dateTimeStampType));
    }

    @Override
    public ItemType getDynamicType() {
        return BuiltinTypesCatalogue.dateTimeStampItem;
    }

    @Override
    public boolean isAtomic() {
        return true;
    }
}

