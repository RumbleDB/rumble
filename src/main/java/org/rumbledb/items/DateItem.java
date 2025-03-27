package org.rumbledb.items;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import org.rumbledb.api.Item;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.comparison.ComparisonExpression.ComparisonOperator;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.runtime.misc.ComparisonIterator;
import org.rumbledb.types.ItemType;

public class DateItem implements Item {

    private static final long serialVersionUID = 1L;
    private ZonedDateTime value;
    private boolean hasTimeZone = true;

    public DateItem() {
        super();
    }

    DateItem(ZonedDateTime value, boolean hasTimeZone) {
        super();
        this.value = value;
        this.hasTimeZone = hasTimeZone;
    }

    DateItem(String dateTimeString) {
        this.value = DateTimeItem.parseDateTime(dateTimeString, BuiltinTypesCatalogue.dateItem);
        if (
            !dateTimeString.endsWith("Z")
                && this.value.getOffset()
                    .equals(ZoneId.systemDefault().getRules().getOffset(Instant.from(OffsetDateTime.now())))
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

    public ZonedDateTime getValue() {
        return this.value;
    }

    @Override
    public ZonedDateTime getDateTimeValue() {
        return this.value;
    }

    @Override
    public String getStringValue() {
        String value = this.getValue().toString();
        String zone = this.value.getZone().equals(ZoneId.of("UTC")) ? "Z" : this.value.getZone().toString();
        int dateTimeSeparatorIndex = value.indexOf("T");
        return value.substring(0, dateTimeSeparatorIndex) + (this.hasTimeZone ? zone : "");
    }

    @Override
    public boolean isDate() {
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
    public boolean getEffectiveBooleanValue() {
        return false;
    }

    @Override
    public int hashCode() {
        return this.getValue().hashCode();
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
}
