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


public class TimeItem implements Item {

    private static final long serialVersionUID = 1L;
    private OffsetTime value;
    private boolean hasTimeZone = true;

    @SuppressWarnings("unused")
    public TimeItem() {
        super();
    }

    TimeItem(OffsetTime value, boolean hasTimeZone) {
        super();
        this.value = value;
        this.hasTimeZone = hasTimeZone;
    }

    TimeItem(String timeString) {
        getTimeFromString(timeString);
    }

    TimeItem(int hour, int minute, int second) {
        super();
        this.value = OffsetTime.of(hour, minute, second, 0, ZoneOffset.UTC);
        this.hasTimeZone = false;
    }

    TimeItem(int hour, int minute, int second, int zoneOffset) {
        super();
        this.value = OffsetTime.of(hour, minute, second, 0, ZoneOffset.ofTotalSeconds(zoneOffset * 60));
        this.hasTimeZone = true;
    }

    private void getTimeFromString(String timeString) {
        try {
            if (timeString.startsWith("24:00:00")) {
                timeString = timeString.replace("24:00:00", "00:00:00");
            }
            if (timeString.contains("Z") || timeString.contains("+") || timeString.contains("-")) {
                this.value = OffsetTime.parse(timeString, DateTimeFormatter.ISO_OFFSET_TIME);
                this.hasTimeZone = true;
            } else {
                this.value = LocalTime.parse(timeString).atOffset(ZoneOffset.UTC);
                this.hasTimeZone = false;
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid xs:time format: " + timeString, e);
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
        return this.value.format(
            this.hasTimeZone ? DateTimeFormatter.ISO_OFFSET_TIME : DateTimeFormatter.ISO_LOCAL_TIME
        );
    }

    @Override
    public void write(Kryo kryo, Output output) {
        output.writeString(this.value.format(DateTimeFormatter.ISO_OFFSET_TIME));
        output.writeBoolean(this.hasTimeZone);
    }

    @Override
    public void read(Kryo kryo, Input input) {
        this.value = OffsetTime.parse(input.readString());
        this.hasTimeZone = input.readBoolean();
    }

    @Override
    public ItemType getDynamicType() {
        return BuiltinTypesCatalogue.timeItem;
    }

    @Override
    public boolean isAtomic() {
        return true;
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
