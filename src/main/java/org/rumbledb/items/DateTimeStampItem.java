package org.rumbledb.items;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.rumbledb.api.Item;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.comparison.ComparisonExpression.ComparisonOperator;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.runtime.misc.ComparisonIterator;
import org.rumbledb.types.ItemType;


public class DateTimeStampItem implements Item {

    private static final long serialVersionUID = 1L;
    private DateTime value;

    public DateTimeStampItem() {
        super();
    }

    DateTimeStampItem(DateTime value, boolean checkTimezone) {
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
    public DateTime getDateTimeValue() {
        return this.value;
    }

    @Override
    public String getStringValue() {
        String value = this.value.toString();
        String zoneString = this.value.getZone() == DateTimeZone.UTC
            ? "Z"
            : this.value.getZone().toString().equals(DateTimeZone.getDefault().toString())
                ? ""
                : value.substring(value.length() - 6);
        value = value.substring(0, value.length() - zoneString.length());
        value = this.value.getMillisOfSecond() == 0 ? value.substring(0, value.length() - 4) : value;
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
        output.writeLong(this.value.getMillis(), true);
        output.writeString(this.value.getZone().getID());
    }

    @Override
    public void read(Kryo kryo, Input input) {
        Long millis = input.readLong(true);
        DateTimeZone zone = DateTimeZone.forID(input.readString());
        this.value = new DateTime(millis, zone);
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

