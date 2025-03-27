package org.rumbledb.items;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import org.rumbledb.api.Item;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.comparison.ComparisonExpression.ComparisonOperator;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.runtime.misc.ComparisonIterator;
import org.rumbledb.types.ItemType;


public class TimeItem implements Item {

    private static final long serialVersionUID = 1L;
    private ZonedDateTime value;
    private boolean hasTimeZone = true;

    public TimeItem() {
        super();
    }

    TimeItem(ZonedDateTime value, boolean hasTimeZone) {
        super();
        this.value = value;
        this.hasTimeZone = hasTimeZone;
    }

    TimeItem(String dateTimeString) {
        this.value = DateTimeItem.parseDateTime(dateTimeString, BuiltinTypesCatalogue.timeItem);
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
    public boolean getEffectiveBooleanValue() {
        return false;
    }

    @Override
    public boolean isTime() {
        return true;
    }

    @Override
    public boolean hasDateTime() {
        return true;
    }

    @Override
    public boolean hasTimeZone() {
        return this.hasTimeZone;
    }

    @Override
    public int hashCode() {
        return this.value.hashCode();
    }

    @Override
    public String getStringValue() {
        String value = this.value.toString();
        String zoneString;
        if (this.value.getOffset().equals(ZoneOffset.UTC)) {
            zoneString = "Z";
        } else {
            zoneString = this.value.getOffset().toString();
        }
        value = value.substring(0, value.length() - zoneString.length());
        if (this.value.getNano() == 0) {
            value = value.substring(0, value.length() - 4);
        }
        int dateTimeSeparatorIndex = value.indexOf("T");
        return value.substring(dateTimeSeparatorIndex + 1) + (this.hasTimeZone ? zoneString : "");
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

    @Override
    public ItemType getDynamicType() {
        return BuiltinTypesCatalogue.timeItem;
    }

    @Override
    public boolean isAtomic() {
        return true;
    }
}
