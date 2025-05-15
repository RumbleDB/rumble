package org.rumbledb.items;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.time.Month;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import org.rumbledb.api.Item;
import org.rumbledb.exceptions.DatetimeOverflowOrUnderflow;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.comparison.ComparisonExpression;
import org.rumbledb.runtime.misc.ComparisonIterator;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.ItemType;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class gMonthDayItem implements Item {
    private boolean hasTimeZone;
    private final Pattern gMonthDayRegex = Pattern.compile(
        "--(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])(Z|([+\\-])((0[0-9]|1[0-3]):[0-5][0-9]|14:00))?"
    );
    private Month month;
    private int day;
    private ZoneOffset offset;

    @SuppressWarnings("unused")
    public gMonthDayItem() {
        super();
    }

    gMonthDayItem(OffsetDateTime dateTime, boolean hasTimeZone) {
        this.month = Month.of(dateTime.getMonthValue());
        this.day = dateTime.getDayOfMonth();
        if (hasTimeZone) {
            this.offset = dateTime.getOffset();
            this.hasTimeZone = true;
        } else {
            this.offset = null;
            this.hasTimeZone = false;
        }
    }

    gMonthDayItem(String gMonthDayString) {
        getgMonthDayFromString(gMonthDayString);
    }

    private void getgMonthDayFromString(String gMonthDayString) {
        Matcher matcher = this.gMonthDayRegex.matcher(gMonthDayString);
        if (!matcher.matches()) {
            throw new DatetimeOverflowOrUnderflow(
                    "Invalid xs:gMonthDay: \"" + gMonthDayString + "\"",
                    ExceptionMetadata.EMPTY_METADATA
            );
        }
        this.month = Month.of(Integer.parseInt(matcher.group(1)));
        this.day = Integer.parseInt(matcher.group(2));
        String tz = matcher.group(3);
        if (tz == null) {
            this.hasTimeZone = false;
        } else {
            this.hasTimeZone = true;
            this.offset = ZoneOffset.of(tz);
        }
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

    @Override
    public String getStringValue() {
        if (this.hasTimeZone) {
            return String.format("--%02d-%02d", this.month.getValue(), this.day) + this.offset;
        } else {
            return String.format("--%02d-%02d", this.month.getValue(), this.day);
        }
    }

    @Override
    public boolean isGMonthDay() {
        return true;
    }

    @Override
    public void write(Kryo kryo, Output output) {
        output.writeString(this.getStringValue());
        output.writeBoolean(this.hasTimeZone);
    }

    @Override
    public void read(Kryo kryo, Input input) {
        String dateTimeString = input.readString();
        this.hasTimeZone = input.readBoolean();
        getgMonthDayFromString(dateTimeString);
    }

    @Override
    public ItemType getDynamicType() {
        return BuiltinTypesCatalogue.gMonthDayItem;
    }

    @Override
    public boolean getEffectiveBooleanValue() {
        return false;
    }

    @Override
    public boolean isAtomic() {
        return true;
    }

    @Override
    public OffsetDateTime getDateTimeValue() {
        if (this.hasTimeZone) {
            return OffsetDateTime.of(0, this.month.getValue(), this.day, 0, 0, 0, 0, this.offset);
        } else {
            return OffsetDateTime.of(0, this.month.getValue(), this.day, 0, 0, 0, 0, ZoneOffset.UTC);
        }
    }
}
