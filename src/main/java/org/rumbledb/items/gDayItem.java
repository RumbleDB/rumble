package org.rumbledb.items;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import org.rumbledb.api.Item;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.comparison.ComparisonExpression;
import org.rumbledb.runtime.misc.ComparisonIterator;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.ItemType;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class gDayItem implements Item {

    private static final long serialVersionUID = 1L;
    private boolean hasTimeZone;
    private int day;
    private ZoneOffset offset;
    private final Pattern gDayRegex = Pattern.compile(
        "---(0[1-9]|[12][0-9]|3[01])(Z|([+\\-])((0[0-9]|1[0-3]):[0-5][0-9]|14:00))?"
    );

    @SuppressWarnings("unused")
    public gDayItem() {
        super();
    }

    gDayItem(OffsetDateTime dateTime, boolean hasTimeZone) {
        this.day = dateTime.getMonthValue();
        if (hasTimeZone) {
            this.offset = dateTime.getOffset();
            this.hasTimeZone = true;
        } else {
            this.offset = null;
            this.hasTimeZone = false;
        }
    }

    gDayItem(String gDayString) {
        getgDayFromString(gDayString);
    }

    private void getgDayFromString(String gDayString) {
        Matcher matcher = this.gDayRegex.matcher(gDayString);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Invalid gDay format: " + gDayString);
        }
        this.day = Integer.parseInt(matcher.group(1));
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
            return "---" + this.day + this.offset;
        } else {
            return "---" + this.day;
        }
    }

    @Override
    public boolean isGDay() {
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
        getgDayFromString(dateTimeString);
    }

    @Override
    public ItemType getDynamicType() {
        return BuiltinTypesCatalogue.gDayItem;
    }

    @Override
    public boolean isAtomic() {
        return true;
    }

    @Override
    public OffsetDateTime getDateTimeValue() {
        if (this.hasTimeZone) {
            return OffsetDateTime.of(0, 1, this.day, 0, 0, 0, 0, this.offset);
        } else {
            return OffsetDateTime.of(0, 1, this.day, 0, 0, 0, 0, ZoneOffset.UTC);

        }
    }
}
