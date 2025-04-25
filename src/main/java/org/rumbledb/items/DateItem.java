package org.rumbledb.items;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import org.rumbledb.api.Item;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.comparison.ComparisonExpression.ComparisonOperator;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.runtime.misc.ComparisonIterator;
import org.rumbledb.types.ItemType;

public class DateItem implements Item {

    private static final long serialVersionUID = 1L;
    private OffsetDateTime value;
    private boolean hasTimeZone = false;

    @SuppressWarnings("unused")
    public DateItem() {
        super();
    }

    DateItem(OffsetDateTime value, boolean hasTimeZone) {
        super();
        this.value = value;
        this.hasTimeZone = hasTimeZone;
    }

    DateItem(String dateTimeString) {
        getDateFromString(dateTimeString);
    }

    DateItem(int year, int month, int day) {
        super();
        this.value = OffsetDateTime.of(year, month, day, 0, 0, 0, 0, ZoneOffset.UTC);
        this.hasTimeZone = false;
    }

    DateItem(int year, int month, int day, int zoneOffset) {
        super();
        this.value = OffsetDateTime.of(year, month, day, 0, 0, 0, 0, ZoneOffset.ofTotalSeconds(zoneOffset * 60));
        this.hasTimeZone = true;
    }

    private void getDateFromString(String dateString) {
        try {
            if (dateString.contains("Z") || dateString.contains(":")) {
                this.value = LocalDate.parse(dateString, DateTimeFormatter.ISO_OFFSET_DATE)
                    .atStartOfDay()
                    .atOffset(ZoneOffset.of(dateString.substring(10)));
                this.hasTimeZone = true;
            } else {
                this.value = LocalDate.parse(dateString, DateTimeFormatter.ISO_LOCAL_DATE)
                    .atStartOfDay(ZoneOffset.UTC)
                    .toOffsetDateTime();
                this.hasTimeZone = false;
            }
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid xs:date format: " + dateString, e);
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
    public String getStringValue() {
        if (this.hasTimeZone) {
            return this.value.format(DateTimeFormatter.ISO_OFFSET_DATE);
        } else {
            return this.value.format(DateTimeFormatter.ISO_LOCAL_DATE);
        }
    }

    @Override
    public boolean isDate() {
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
        output.writeString(
            this.value.format(!this.hasTimeZone ? DateTimeFormatter.ISO_LOCAL_DATE : DateTimeFormatter.ISO_OFFSET_DATE)
        );
        output.writeBoolean(this.hasTimeZone);
    }

    @Override
    public void read(Kryo kryo, Input input) {
        String dateString = input.readString();
        this.hasTimeZone = input.readBoolean();
        getDateFromString(dateString);
    }

    @Override
    public ItemType getDynamicType() {
        return BuiltinTypesCatalogue.dateItem;
    }

    @Override
    public boolean isAtomic() {
        return true;
    }

    @Override
    public String getSparkSQLType() {
        // TODO: Make enum?
        return "DATE";
    }

    @Override
    public int getMonth() {
        return this.value.getMonth().getValue();
    }

    @Override
    public int getYear() {
        return this.value.getYear();
    }

    @Override
    public int getDay() {
        return this.value.getDayOfMonth();
    }

    @Override
    public int getOffset() {
        ZoneOffset zoneOffset = this.value.getOffset();
        return zoneOffset.getTotalSeconds() / 60;
    }

    @Override
    public boolean hasTimeZone() {
        return this.hasTimeZone;
    }


}
