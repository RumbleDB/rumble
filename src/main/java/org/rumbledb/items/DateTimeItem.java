package org.rumbledb.items;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.time.*;
import java.time.format.DateTimeFormatter;

import org.rumbledb.api.Item;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.comparison.ComparisonExpression.ComparisonOperator;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.runtime.misc.ComparisonIterator;
import org.rumbledb.types.ItemType;

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
    private static final String timeFrag = String.format(
        "((%s:%s:%s)|(%s))",
        hourFrag,
        minuteFrag,
        secondFrag,
        endOfDayFrag
    );

    private static final String dateTimeLexicalRep = String.format("%sT%s(%s)?", dateFrag, timeFrag, timezoneFrag);
    private static final String dateTimeStampLexicalRep = String.format("%sT%s%s", dateFrag, timeFrag, timezoneFrag);
    private static final String dateLexicalRep = String.format("(%s(%s)?)", dateFrag, timezoneFrag);
    private static final String timeLexicalRep = String.format("(%s(%s)?)", timeFrag, timezoneFrag);

    private static final Pattern dateTimePattern = Pattern.compile(dateTimeLexicalRep);
    private static final Pattern dateTimeStampPattern = Pattern.compile(dateTimeStampLexicalRep);
    private static final Pattern datePattern = Pattern.compile(dateLexicalRep);
    private static final Pattern timePattern = Pattern.compile(timeLexicalRep);


    private static final long serialVersionUID = 1L;
    private ZonedDateTime value;
    private boolean hasTimeZone = true;

    @SuppressWarnings("unused")
    public DateTimeItem() {
        super();
    }

    DateTimeItem(ZonedDateTime value, boolean hasTimeZone) {
        super();
        this.value = value;
        this.hasTimeZone = hasTimeZone;
    }

    public DateTimeItem(String dateTimeString) {
        getDateTimeFromString(dateTimeString);
    }

    private void getDateTimeFromString(String dateTimeString) {
        try {
            if (
                dateTimeString.contains("Z") || dateTimeString.contains("+") || dateTimeString.matches(".*-\\d\\d:.*")
            ) {
                this.value = ZonedDateTime.parse(dateTimeString, DateTimeFormatter.ISO_DATE_TIME);
                this.hasTimeZone = true;
            } else {
                this.value = LocalDateTime.parse(dateTimeString, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                    .atZone(ZoneOffset.UTC);
                this.hasTimeZone = false;
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid xs:dateTime format: " + dateTimeString, e);
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
        if (this.hasTimeZone) {
            return this.value.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        } else {
            return this.value.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        }
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

    @Override
    public ItemType getDynamicType() {
        return BuiltinTypesCatalogue.dateTimeItem;
    }

    @Override
    public boolean isAtomic() {
        return true;
    }

    @Override
    public int getMonth() {
        return this.value.getMonth().getValue();
    }

    @Override
    public int getYear() {
        return this.value.getYear();
    }

    @Override
    public int getDay() {
        return this.value.getDayOfMonth();
    }

    @Override
    public int getHour() {
        return this.value.getHour();
    }

    @Override
    public int getMinute() {
        return this.value.getMinute();
    }

    @Override
    public int getSecond() {
        return this.value.getSecond();
    }

    @Override
    public int getNanosecond() {
        return this.value.getNano();
    }

    @Override
    public int getOffset() {
        return this.value.getOffset().getTotalSeconds() / 60;
    }
}
