package org.rumbledb.items;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import java.time.OffsetDateTime;
import java.time.Year;
import java.time.ZoneOffset;
import org.rumbledb.api.Item;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.comparison.ComparisonExpression;
import org.rumbledb.runtime.misc.ComparisonIterator;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.ItemType;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class gYearItem implements Item {
    private boolean hasTimeZone;
    private Year year;
    private ZoneOffset offset;
    private final Pattern gYearRegex = Pattern.compile(
        "-?([1-9][0-9]{3,}|0[0-9]{3})(Z|([+\\-])((0[0-9]|1[0-3]):[0-5][0-9]|14:00))?"
    );

    @SuppressWarnings("unused")
    public gYearItem() {
        super();
    }

    gYearItem(OffsetDateTime dateTime, boolean hasTimeZone) {
        this.year = Year.of(dateTime.getYear());
        if (hasTimeZone) {
            this.offset = dateTime.getOffset();
            this.hasTimeZone = true;
        } else {
            this.offset = null;
            this.hasTimeZone = false;
        }
    }

    gYearItem(String gYearString) {
        getgYearFromString(gYearString);
    }

    private void getgYearFromString(String gYearString) {
        Matcher matcher = this.gYearRegex.matcher(gYearString);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Invalid gYear format: " + gYearString);
        }
        this.year = Year.of(Integer.parseInt(matcher.group(1)));
        String tz = matcher.group(2);
        if (tz == null) {
            this.hasTimeZone = false;
        } else {
            this.hasTimeZone = true;
            this.offset = ZoneOffset.of(tz);
        }
    }

    @Override
    public boolean equals(Object otherItem) {
        if (otherItem instanceof Item) {
            long c = ComparisonIterator.compareItems(
                this,
                (Item) otherItem,
                ComparisonExpression.ComparisonOperator.VC_EQ,
                ExceptionMetadata.EMPTY_METADATA
            );
            return c == 0;
        }
        return false;
    }

    public String getStringValue() {
        if (this.hasTimeZone) {
            return this.year.toString() + this.offset.toString();
        } else {
            return this.year.toString();
        }
    }

    @Override
    public boolean isGYear() {
        return true;
    }

    @Override
    public void write(Kryo kryo, Output output) {
        output.writeString(this.getStringValue());
        output.writeBoolean(this.hasTimeZone);
    }

    @Override
    public void read(Kryo kryo, Input input) {
        String dateTimeString = input.readString();
        this.hasTimeZone = input.readBoolean();
        getgYearFromString(dateTimeString);
    }

    @Override
    public ItemType getDynamicType() {
        return BuiltinTypesCatalogue.gYearItem;
    }

    @Override
    public boolean isAtomic() {
        return true;
    }
}
