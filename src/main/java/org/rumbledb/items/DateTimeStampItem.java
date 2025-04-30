package org.rumbledb.items;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import java.time.OffsetDateTime;

import org.rumbledb.api.Item;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.comparison.ComparisonExpression.ComparisonOperator;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.runtime.misc.ComparisonIterator;
import org.rumbledb.types.ItemType;


public class DateTimeStampItem implements Item {

    private static final long serialVersionUID = 1L;
    private DateTimeItem value;

    @SuppressWarnings("unused")
    public DateTimeStampItem() {
        super();
    }

    DateTimeStampItem(OffsetDateTime value, boolean checkTimezone) {
        super();
        this.value = new DateTimeItem(value, checkTimezone);
    }

    DateTimeStampItem(String dateTimeStampString) {
        this.value = new DateTimeItem(dateTimeStampString);
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
    public OffsetDateTime getDateTimeValue() {
        return this.value.getDateTimeValue();
    }

    @Override
    public String getStringValue() {
        return this.value.getStringValue();
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
        this.value.write(kryo, output);
    }

    @Override
    public void read(Kryo kryo, Input input) {
        this.value.read(kryo, input);
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

