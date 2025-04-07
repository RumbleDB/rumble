package org.rumbledb.items;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import org.rumbledb.api.Item;
import org.rumbledb.exceptions.DatetimeOverflowOrUnderflow;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.comparison.ComparisonExpression.ComparisonOperator;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.runtime.misc.ComparisonIterator;
import org.rumbledb.types.ItemType;

import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.regex.Pattern;

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
    private ZonedDateTime value;
    private boolean hasTimeZone = true;

    public DateTimeItem() {
        super();
    }

    DateTimeItem(ZonedDateTime value, boolean hasTimeZone) {
        super();
        this.value = value;
        this.hasTimeZone = hasTimeZone;
    }

    public DateTimeItem(String dateTimeString) {
        this.value = parseDateTime(dateTimeString, BuiltinTypesCatalogue.dateTimeItem);

        if (
            !dateTimeString.endsWith("Z")
                && this.value.getOffset().equals(ZoneId.systemDefault().getRules().getOffset(this.value.toInstant()))
        ) {
            this.hasTimeZone = false;
            this.value = this.value.withZoneSameInstant(ZoneId.of("UTC"));
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
    public ZonedDateTime getDateTimeValue() {
        return this.value;
    }

    @Override
    public String getStringValue() {
        String value = this.value.toString();
        String zoneString;
        if (this.value.getOffset().equals(ZoneOffset.UTC)) {
            zoneString = "Z";
        } else if (this.value.getOffset().equals(ZoneId.systemDefault().getRules().getOffset(this.value.toInstant()))) {
            zoneString = "";
        } else {
            zoneString = this.value.getOffset().toString();
        }
        value = value.substring(0, value.length() - zoneString.length());
        if (this.value.getNano() == 0) {
            value = value.substring(0, value.length() - 4);
        }
        return value + (this.hasTimeZone ? zoneString : "");
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
    public void write(Kryo kryo, Output output) {
        output.writeString(this.value.format(DateTimeFormatter.ISO_INSTANT));
        output.writeBoolean(this.hasTimeZone);
        output.writeString(this.value.getZone().getId());
    }

    @Override
    public void read(Kryo kryo, Input input) {
        String dateTimeString = input.readString();
        this.hasTimeZone = input.readBoolean();
        ZoneId zone = ZoneId.of(input.readString());
        this.value = ZonedDateTime.parse(dateTimeString, DateTimeFormatter.ISO_INSTANT.withZone(zone));
    }

    static DateTimeFormatter getDateTimeFormatter(ItemType dateTimeType) {
        if (
            dateTimeType.equals(BuiltinTypesCatalogue.dateTimeStampItem)
                ||
                dateTimeType.equals(BuiltinTypesCatalogue.dateTimeItem)
        ) {
            return DateTimeFormatter.ISO_DATE_TIME; // ISO date-time with offset
        }
        if (dateTimeType.equals(BuiltinTypesCatalogue.dateItem)) {
            DateTimeFormatter timeZoneOffsetFormatter = new DateTimeFormatterBuilder()
                .appendPattern("['Z']")
                .optionalStart()
                .appendOffset("+HH", "+00")
                .optionalEnd()
                .toFormatter()
                .withResolverStyle(ResolverStyle.STRICT);

            return new DateTimeFormatterBuilder()
                .append(DateTimeFormatter.ISO_LOCAL_DATE) // Assuming this returns a DateTimeFormatter
                .optionalStart()
                .append(timeZoneOffsetFormatter)
                .optionalEnd()
                .toFormatter()
                .withResolverStyle(ResolverStyle.STRICT);
        }
        if (dateTimeType.equals(BuiltinTypesCatalogue.timeItem)) {
            return DateTimeFormatter.ISO_TIME; // ISO time with offset
        }
        throw new IllegalArgumentException("Unsupported ItemType: " + dateTimeType);
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

    static ZonedDateTime parseDateTime(String dateTime, ItemType dateTimeType) throws IllegalArgumentException {
        if (!checkInvalidDateTimeFormat(dateTime, dateTimeType)) {
            throw new IllegalArgumentException();
        }
        dateTime = fixEndOfDay(dateTime);
        try {
            if (dateTimeType.equals(BuiltinTypesCatalogue.dateItem)) {
                return LocalDate.parse(dateTime, getDateTimeFormatter(dateTimeType)).atStartOfDay(ZoneId.of("UTC"));
            } else if (dateTimeType.equals(BuiltinTypesCatalogue.timeItem)) {
                try {
                    return LocalDateTime.of(LocalDate.now(), LocalTime.parse(dateTime)).atZone(ZoneId.of("UTC"));
                } catch (DateTimeParseException e) {
                    return OffsetTime.parse(dateTime).atDate(LocalDate.now()).atZoneSameInstant(ZoneId.of("UTC"));
                }
            } else {
                try {
                    return ZonedDateTime.parse(dateTime, getDateTimeFormatter(dateTimeType));
                } catch (DateTimeParseException e) {
                    return LocalDateTime.parse(dateTime).atZone(ZoneId.of("UTC"));
                }
            }
        } catch (IllegalArgumentException e) {
            throw new DatetimeOverflowOrUnderflow(
                    "Invalid datetime: \"" + dateTime + "\"",
                    ExceptionMetadata.EMPTY_METADATA
            );
        }
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
