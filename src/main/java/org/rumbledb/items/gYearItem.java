package org.rumbledb.items;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.rumbledb.api.Item;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.comparison.ComparisonExpression;
import org.rumbledb.runtime.misc.ComparisonIterator;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.ItemType;

import java.util.regex.Pattern;

public class gYearItem implements Item {

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


    private static final String gYearLexicalRep = "(" + "-" + ")?" + yearFrag + "(" + timezoneFrag + ")?";
    private static final String dateTimeLexicalRep = dateFrag + "T" + timeFrag + "(" + timezoneFrag + ")?";
    private static final String dateLexicalRep = "(" + dateFrag + "(" + timezoneFrag + ")?)";

    private static final Pattern gYearPattern = Pattern.compile(gYearLexicalRep);
    private static final Pattern dateTimePattern = Pattern.compile(dateTimeLexicalRep);
    private static final Pattern datePattern = Pattern.compile(dateLexicalRep);

    private static final long serialVersionUID = 1L;
    private String value;
    private DateTime dt;
    private boolean hasTimeZone = true;

    public gYearItem() {
        super();
    }

    gYearItem(String gYearString) {
        this.value = gYearItem.parseGYear(gYearString, BuiltinTypesCatalogue.gYearItem);
        boolean hasDash;
        String year;
        if (gYearString.startsWith("-")) {
            hasDash = true;
            year = this.value.substring(1, 5);
        } else {
            hasDash = false;
            year = this.value.substring(0, 4);
        }

        String startingDate;
        if ((hasDash && gYearString.length() == 5) || (!hasDash && gYearString.length() == 4)) {
            this.hasTimeZone = false;
            startingDate = year.concat("-01-01");
        } else {
            String zone = hasDash ? this.value.substring(5) : this.value.substring(4);
            startingDate = year.concat("-01-01").concat(zone);
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
        return this.serialize();
    }

    @Override
    public DateTime getDateTimeValue() {
        return this.dt;
    }

    @Override
    public boolean isGYear() {
        return true;
    }

    @Override
    public String serialize() {
        String zone = this.getDateTimeValue().getZone() == DateTimeZone.UTC
            ? "Z"
            : this.getDateTimeValue().getZone().toString();
        return (this.value.startsWith("-") ? this.value.substring(0, 5) : this.value.substring(0, 4))
            + (this.hasTimeZone ? zone : "");
    }

    @Override
    public void write(Kryo kryo, Output output) {
        output.writeLong(this.getDateTimeValue().getMillis(), true);
        output.writeString(this.getDateTimeValue().getZone().getID());
    }

    @Override
    public void read(Kryo kryo, Input input) {
        Long millis = input.readLong(true);
        this.hasTimeZone = input.readBoolean();
        DateTimeZone zone = DateTimeZone.forID(input.readString());
        this.dt = new DateTime(millis, zone);
    }

    private static boolean checkInvalidGYearFormat(String gYear, ItemType gYearType) {
        if (gYear.startsWith("0000") || gYear.startsWith("-0000")) {
            return false;
        }
        if (gYearType.equals(BuiltinTypesCatalogue.gYearItem)) {
            return gYearPattern.matcher(gYear).matches();
        }
        if (gYearType.equals(BuiltinTypesCatalogue.dateTimeItem)) {
            return dateTimePattern.matcher(gYear).matches();
        }
        if (gYearType.equals(BuiltinTypesCatalogue.dateItem)) {
            return datePattern.matcher(gYear).matches();
        }
        return false;
    }

    @Override
    public ItemType getDynamicType() {
        return BuiltinTypesCatalogue.gYearItem;
    }

    @Override
    public boolean isAtomic() {
        return true;
    }

    static String parseGYear(String gYear, ItemType gYearType) throws IllegalArgumentException {
        if (!checkInvalidGYearFormat(gYear, gYearType)) {
            throw new IllegalArgumentException();
        }
        return gYear;
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
