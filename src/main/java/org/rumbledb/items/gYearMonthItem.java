package org.rumbledb.items;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.time.*;
import org.rumbledb.api.Item;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.comparison.ComparisonExpression;
import org.rumbledb.runtime.misc.ComparisonIterator;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.ItemType;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class gYearMonthItem implements Item {
    private boolean hasTimeZone;
    private Year year;
    private Month month;
    private ZoneOffset offset;
    private final Pattern gYearMonthRegex = Pattern.compile(
        "-?([1-9][0-9]{3,}|0[0-9]{3})-(0[1-9]|1[0-2])(Z|([+\\-])((0[0-9]|1[0-3]):[0-5][0-9]|14:00))?"
    );


    @SuppressWarnings("unused")
    public gYearMonthItem() {
        super();
    }

    gYearMonthItem(OffsetDateTime dateTime, boolean hasTimeZone) {
        this.year = Year.of(dateTime.getYear());
        this.month = Month.from(dateTime.getMonth());
        if (hasTimeZone) {
            this.offset = dateTime.getOffset();
            this.hasTimeZone = true;
        } else {
            this.offset = null;
            this.hasTimeZone = false;
        }
    }

    gYearMonthItem(String gYearMonthString) {
        getgYearMonthFromString(gYearMonthString);
    }

    private void getgYearMonthFromString(String gYearMonthString) {
        Matcher matcher = this.gYearMonthRegex.matcher(gYearMonthString);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Invalid xs:gYearMonth: \"" + gYearMonthString + "\"");
        }
        this.year = Year.of(Integer.parseInt(matcher.group(1)));
        this.month = Month.of(Integer.parseInt(matcher.group(2)));
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
            return String.format("%d-%02d", this.year.getValue(), this.month.getValue()) + this.offset;
        } else {
            return String.format("%d-%02d", this.year.getValue(), this.month.getValue());
        }
    }

    @Override
    public boolean getEffectiveBooleanValue() {
        return false;
    }

    @Override
    public boolean isGYearMonth() {
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
        getgYearMonthFromString(dateTimeString);
    }

    @Override
    public ItemType getDynamicType() {
        return BuiltinTypesCatalogue.gYearMonthItem;
    }

    @Override
    public boolean isAtomic() {
        return true;
    }

    @Override
    public OffsetDateTime getDateTimeValue() {
        if (this.hasTimeZone) {
            return OffsetDateTime.of(this.year.getValue(), this.month.getValue(), 1, 0, 0, 0, 0, this.offset);
        } else {
            return OffsetDateTime.of(this.year.getValue(), this.month.getValue(), 1, 0, 0, 0, 0, ZoneOffset.UTC);
        }
    }
}
