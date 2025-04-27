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

public class DateTimeItem implements Item {

    private static final long serialVersionUID = 1L;
    private ZonedDateTime value;
    private boolean hasTimeZone = true;

    @SuppressWarnings("unused")
    public DateTimeItem() {
        super();
    }

    DateTimeItem(ZonedDateTime value, boolean hasTimeZone) {
        super();
        this.value = value;
        this.hasTimeZone = hasTimeZone;
    }

    public DateTimeItem(String dateTimeString) {
        getDateTimeFromString(dateTimeString);
    }

    private void getDateTimeFromString(String dateTimeString) {
        int yearIncrement = 0;
        int dayIncrement = 0;
        int isMinus = 1;
        try {
            if (dateTimeString.startsWith("-")) {
                dateTimeString = dateTimeString.substring(1);
                isMinus = -1;
            }
            String[] yearAndRest = dateTimeString.split("-", 2);
            String yearOnly = yearAndRest[0];
            String rest = yearAndRest[1];
            if (yearOnly.length() > 4) {
                dateTimeString = "2000-" + rest;
                yearIncrement = Integer.parseInt(yearOnly) - 2000;
            }
            if (dateTimeString.contains("24:00:00")) {
                dateTimeString = dateTimeString.replace("24:00:00", "00:00:00");
                dayIncrement = 1;
            }
            if (
                dateTimeString.contains("Z") || dateTimeString.contains("+") || dateTimeString.matches(".*-\\d\\d:.*")
            ) {
                this.value = ZonedDateTime.parse(dateTimeString, DateTimeFormatter.ISO_DATE_TIME);
                this.hasTimeZone = true;
            } else {
                this.value = LocalDateTime.parse(dateTimeString, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                    .atZone(ZoneOffset.UTC);
                this.hasTimeZone = false;
            }
            this.value = this.value.plusDays(dayIncrement).plusYears(yearIncrement);
            this.value = this.value.withYear(this.value.getYear() * isMinus);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid xs:dateTime format: " + dateTimeString, e);
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
    public String getStringValue() {
        String stringValue;
        if (this.hasTimeZone) {
            stringValue = this.value.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        } else {
            stringValue = this.value.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        }
        if (this.value.toString().startsWith("+")) {
            return stringValue.substring(1);
        }
        return stringValue;
    }

    @Override
    public boolean hasTimeZone() {
        return this.hasTimeZone;
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
        return BuiltinTypesCatalogue.dateTimeItem;
    }

    @Override
    public boolean isAtomic() {
        return true;
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
    @Override
    public long getEpochMilis(){
        return this.value.toInstant().toEpochMilli();
    }
}
