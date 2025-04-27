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

    private void getDateFromString(String dateString) {
        int yearIncrement = 0;
        int isMinus = 1;
        try {
            if (dateString.startsWith("-")) {
                dateString = dateString.substring(1);
                isMinus = -1;
            }
            String[] yearAndRest = dateString.split("-", 2);
            String yearOnly = yearAndRest[0];
            String rest = yearAndRest[1];
            if (yearOnly.length() > 4) {
                dateString = "2000-" + rest;
                yearIncrement = Integer.parseInt(yearOnly) - 2000;
            }
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
            this.value = this.value.plusYears(yearIncrement);
            this.value = this.value.withYear(this.value.getYear() * isMinus);
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
        String stringValue;
        if (this.hasTimeZone) {
            stringValue = this.value.format(DateTimeFormatter.ISO_OFFSET_DATE);
        } else {
            stringValue = this.value.format(DateTimeFormatter.ISO_LOCAL_DATE);
        }
        if (this.value.toString().startsWith("+")) {
            return stringValue.substring(1);
        }
        return stringValue;
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

    /**
     * Offset is an integer between −840 and 840 inclusive
     * 
     * @return offset in minutes
     */
    @Override
    public int getOffset() {
        ZoneOffset zoneOffset = this.value.getOffset();
        return zoneOffset.getTotalSeconds() / 60;
    }

    @Override
    public boolean hasTimeZone() {
        return this.hasTimeZone;
    }

    @Override
    public ZonedDateTime getDateTimeValue() {
        return ZonedDateTime.of(this.value.toLocalDateTime(), this.value.getOffset());
    }

    @Override
    public OffsetDateTime getDateValue() {
        return this.value;
    }
}
