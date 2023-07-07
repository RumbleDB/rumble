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


public class TimeItem implements Item {

    private static final long serialVersionUID = 1L;
    private DateTime value;
    private boolean hasTimeZone = true;

    public TimeItem() {
        super();
    }

    TimeItem(DateTime value, boolean hasTimeZone) {
        super();
        this.value = value;
        this.hasTimeZone = hasTimeZone;
        if (!hasTimeZone) {
            this.value = this.value.withZoneRetainFields(DateTimeZone.UTC);
        }
    }

    TimeItem(String dateTimeString) {
        this.value = DateTimeItem.parseDateTime(dateTimeString, BuiltinTypesCatalogue.timeItem);
        if (doesLexicalValueHaveNoTimeZone(dateTimeString)) {
            this.hasTimeZone = false;
            this.value = this.value.withZoneRetainFields(DateTimeZone.UTC);
        }
    }

    private static boolean doesLexicalValueHaveNoTimeZone(String dateTimeString) {
        return DateTimeItem.TIME_NOTIMEZONE_PATTERN.matcher(dateTimeString).matches();
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
        String originalValue = this.value.toString();
        if (!hasTimeZone()) {
            String value = originalValue.substring(0, originalValue.length() - 1);
            value = this.value.getMillisOfSecond() == 0 ? value.substring(0, value.length() - 4) : value;
            value = value.substring(value.indexOf("T") + 1);;
            return value;
        }
        if (this.value.getZone() == DateTimeZone.UTC) {
            String value = originalValue.substring(0, originalValue.length() - 1);
            value = this.value.getMillisOfSecond() == 0 ? value.substring(0, value.length() - 4) : value;
            value = value.substring(value.indexOf("T") + 1);;
            return value + "Z";
        }
        String zoneString = originalValue.substring(originalValue.length() - 6);
        String value = originalValue.substring(0, originalValue.length() - 6);
        value = this.value.getMillisOfSecond() == 0 ? value.substring(0, value.length() - 4) : value;
        value = value.substring(value.indexOf("T") + 1);;
        return value + zoneString;
    }

    @Override
    public void write(Kryo kryo, Output output) {
        output.writeLong(this.value.getMillis(), true);
        output.writeBoolean(this.hasTimeZone);
        output.writeString(this.value.getZone().getID());
    }

    @Override
    public void read(Kryo kryo, Input input) {
        Long millis = input.readLong(true);
        this.hasTimeZone = input.readBoolean();
        DateTimeZone zone = DateTimeZone.forID(input.readString());
        this.value = new DateTime(millis, zone);
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
