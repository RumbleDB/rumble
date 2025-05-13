package org.rumbledb.items;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.time.Month;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import org.rumbledb.api.Item;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.comparison.ComparisonExpression;
import org.rumbledb.runtime.misc.ComparisonIterator;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.ItemType;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class gMonthItem implements Item {

    private static final long serialVersionUID = 1L;
    private boolean hasTimeZone;
    private Month month;
    private ZoneOffset offset;
    private final Pattern gMonthRegex = Pattern.compile(
        "--(0[1-9]|1[0-2])(Z|([+\\-])((0[0-9]|1[0-3]):[0-5][0-9]|14:00))?"
    );

    @SuppressWarnings("unused")
    public gMonthItem() {
        super();
    }

    gMonthItem(OffsetDateTime dateTime, boolean hasTimeZone) {
        this.month = Month.of(dateTime.getMonthValue());
        if (hasTimeZone) {
            this.offset = dateTime.getOffset();
            this.hasTimeZone = true;
        } else {
            this.offset = null;
            this.hasTimeZone = false;
        }
    }

    gMonthItem(String gMonthString) {
        Matcher matcher = this.gMonthRegex.matcher(gMonthString);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Invalid gYear format: " + gMonthString);
        }
        getgMonthFromString(gMonthString);
    }

    private void getgMonthFromString(String gMonthString) {
        Matcher matcher = this.gMonthRegex.matcher(gMonthString);
        this.month = Month.of(Integer.parseInt(matcher.group(1)));
        String tz = matcher.group(2);
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

    public String getStringValue() {
        if (this.hasTimeZone) {
            return String.format("--%02d", this.month.getValue()) + this.offset;
        } else {
            return String.format("--%02d", this.month.getValue());
        }
    }

    @Override
    public boolean isGMonth() {
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
        getgMonthFromString(dateTimeString);
    }

    @Override
    public ItemType getDynamicType() {
        return BuiltinTypesCatalogue.gMonthItem;
    }

    @Override
    public boolean isAtomic() {
        return true;
    }

    @Override
    public OffsetDateTime getDateTimeValue() {
        if (this.hasTimeZone) {
            return OffsetDateTime.of(0, this.month.getValue(), 1, 0, 0, 0, 0, this.offset);
        } else {
            return OffsetDateTime.of(0, this.month.getValue(), 1, 0, 0, 0, 0, ZoneOffset.UTC);
        }
    }
}
