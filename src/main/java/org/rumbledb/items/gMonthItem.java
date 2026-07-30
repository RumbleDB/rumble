package org.rumbledb.items;

import lombok.NoArgsConstructor;

import java.io.Serial;
import java.time.Month;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import org.rumbledb.api.Item;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.ItemType;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@NoArgsConstructor // For Kryo serialization
public class gMonthItem extends AbstractAtomicItem {

    @Serial
    private static final long serialVersionUID = 1L;
    private boolean hasTimeZone;
    private Month month;
    private ZoneOffset offset;
    private static final Pattern gMonthRegex = Pattern.compile(
        "--(0[1-9]|1[0-2])(Z|([+\\-])((0[0-9]|1[0-3]):[0-5][0-9]|14:00))?"
    );

    gMonthItem(OffsetDateTime dateTime, boolean hasTimeZone) {
        this.month = Month.of(dateTime.getMonthValue());
        if (hasTimeZone) {
            this.offset = dateTime.getOffset();
            this.hasTimeZone = true;
        } else {
            this.offset = null;
            this.hasTimeZone = false;
        }
    }

    gMonthItem(String gMonthString) {

        getgMonthFromString(gMonthString);
    }

    @Override
    public Item copy(boolean mutable) {
        return new gMonthItem(this.getDateTimeValue(), this.hasTimeZone);
    }

    private void getgMonthFromString(String gMonthString) {
        Matcher matcher = gMonthRegex.matcher(gMonthString);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Invalid xs:gMonth: \"" + gMonthString + "\"");
        }
        this.month = Month.of(Integer.parseInt(matcher.group(1)));
        String tz = matcher.group(2);
        if (tz == null) {
            this.hasTimeZone = false;
        } else {
            this.hasTimeZone = true;
            this.offset = ZoneOffset.of(tz);
        }
    }

    @Override
    public boolean getEffectiveBooleanValue() {
        return false;
    }

    @Override
    public String getStringValue() {
        return String.format("--%02d%s", this.month.getValue(), this.hasTimeZone ? this.offset : "");
    }

    @Override
    public boolean isGMonth() {
        return true;
    }

    @Override
    public boolean hasTimeZone() {
        return this.hasTimeZone;
    }



    @Override
    public ItemType getDynamicType() {
        return BuiltinTypesCatalogue.gMonthItem;
    }

    @Override
    public boolean isAtomic() {
        return true;
    }

    @Override
    public OffsetDateTime getDateTimeValue() {
        return OffsetDateTime.of(
            0,
            this.month.getValue(),
            1,
            0,
            0,
            0,
            0,
            this.hasTimeZone ? this.offset : ZoneOffset.UTC
        );
    }
}
