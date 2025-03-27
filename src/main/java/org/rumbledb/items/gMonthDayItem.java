package org.rumbledb.items;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import org.rumbledb.api.Item;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.comparison.ComparisonExpression;
import org.rumbledb.runtime.misc.ComparisonIterator;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.ItemType;

import java.util.regex.Pattern;

public class gMonthDayItem implements Item {

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


    private static final String gMonthDayLexicalRep = "--" + monthFrag + "-" + dayFrag + "(" + timezoneFrag + ")?";
    private static final String dateTimeLexicalRep = dateFrag + "T" + timeFrag + "(" + timezoneFrag + ")?";
    private static final String dateLexicalRep = "(" + dateFrag + "(" + timezoneFrag + ")?)";

    private static final Pattern gMonthDayPattern = Pattern.compile(gMonthDayLexicalRep);
    private static final Pattern dateTimePattern = Pattern.compile(dateTimeLexicalRep);
    private static final Pattern datePattern = Pattern.compile(dateLexicalRep);

    private static final long serialVersionUID = 1L;
    private String value;
    private ZonedDateTime dt;
    private boolean hasTimeZone = true;

    public gMonthDayItem() {
        super();
    }

    gMonthDayItem(String gMonthDayString) {
        this.value = gMonthDayItem.parseGMonthDay(gMonthDayString, BuiltinTypesCatalogue.gMonthDayItem);
        String monthDay = this.value.substring(2, 7);

        String startingDate;
        if (gMonthDayString.length() == 7) {
            this.hasTimeZone = false;
            startingDate = "1972-".concat(monthDay);
        } else {
            String zone = this.value.substring(7);
            startingDate = "1972-".concat(monthDay).concat(zone);
        }
        startingDate = fixEndOfDay(startingDate);
        this.dt = new DateItem(startingDate).getDateTimeValue();
    }



    @Override
    public boolean equals(Object otherItem) {
        if (otherItem instanceof Item) {
            long c = ComparisonIterator.compareItems(
                this,
                (Item) otherItem,
                ComparisonExpression.ComparisonOperator.VC_EQ,
                ExceptionMetadata.EMPTY_METADATA
            );
            return c == 0;
        }
        return false;
    }

    public String getValue() {
        return this.value;
    }

    @Override
    public String getStringValue() {
        String zone = this.dt.getZone().equals(ZoneId.of("UTC")) ? "Z" : this.dt.getZone().toString();
        return this.dt.format(DateTimeFormatter.ISO_LOCAL_DATE) + (this.hasTimeZone ? zone : "");
    }

    @Override
    public ZonedDateTime getDateTimeValue() {
        return this.dt;
    }

    @Override
    public boolean isGMonthDay() {
        return true;
    }

    @Override
    public void write(Kryo kryo, Output output) {
        output.writeString(this.dt.format(DateTimeFormatter.ISO_INSTANT));
        output.writeBoolean(this.hasTimeZone);
        output.writeString(this.dt.getZone().getId());
    }

    @Override
    public void read(Kryo kryo, Input input) {
        String dateTimeString = input.readString();
        this.hasTimeZone = input.readBoolean();
        ZoneId zone = ZoneId.of(input.readString());
        this.dt = ZonedDateTime.parse(dateTimeString, DateTimeFormatter.ISO_INSTANT.withZone(zone));
    }

    private static boolean checkInvalidGMonthDayFormat(String gMonthDay, ItemType gMonthDayType) {
        if (gMonthDayType.equals(BuiltinTypesCatalogue.gMonthDayItem)) {
            return gMonthDayPattern.matcher(gMonthDay).matches();
        }
        if (gMonthDayType.equals(BuiltinTypesCatalogue.dateTimeItem)) {
            return dateTimePattern.matcher(gMonthDay).matches();
        }
        if (gMonthDayType.equals(BuiltinTypesCatalogue.dateItem)) {
            return datePattern.matcher(gMonthDay).matches();
        }
        return false;
    }

    @Override
    public ItemType getDynamicType() {
        return BuiltinTypesCatalogue.gMonthDayItem;
    }

    @Override
    public boolean isAtomic() {
        return true;
    }

    static String parseGMonthDay(String gMonthDay, ItemType gMonthDayType) throws IllegalArgumentException {
        if (!checkInvalidGMonthDayFormat(gMonthDay, gMonthDayType)) {
            throw new IllegalArgumentException();
        }
        return gMonthDay;
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
}
