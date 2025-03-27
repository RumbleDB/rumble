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


public class DateTimeStampItem implements Item {

    private static final long serialVersionUID = 1L;
    private ZonedDateTime value;

    public DateTimeStampItem() {
        super();
    }

    DateTimeStampItem(ZonedDateTime value, boolean checkTimezone) {
        super();
        if (checkTimezone) {
            this.value = DateTimeItem.parseDateTime(
                this.value.toString(),
                BuiltinTypesCatalogue.dateTimeStampItem
            );
        } else {
            this.value = value;
        }

    }

    DateTimeStampItem(String dateTimeStampString) {
        this.value = DateTimeItem.parseDateTime(dateTimeStampString, BuiltinTypesCatalogue.dateTimeStampItem);
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
        return value + zoneString;
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
    public boolean hasTimeZone() {
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
        output.writeString(this.value.getZone().getId());
    }

    @Override
    public void read(Kryo kryo, Input input) {
        String dateTimeString = input.readString();
        this.value = ZonedDateTime.parse(
            dateTimeString,
            DateTimeFormatter.ISO_INSTANT.withZone(ZoneId.of(dateTimeString))
        );
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

