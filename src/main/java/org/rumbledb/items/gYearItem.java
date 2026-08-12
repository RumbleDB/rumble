package org.rumbledb.items;

import java.io.Serial;
import java.time.OffsetDateTime;
import java.time.Year;
import java.time.ZoneOffset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.rumbledb.api.Item;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.ItemType;

public class gYearItem extends AbstractAtomicItem {

    @Serial
    private static final long serialVersionUID = 1L;

    private boolean hasTimeZone;
    private Year year;
    private ZoneOffset offset;
    private static final Pattern gYearRegex =
            Pattern.compile("-?([1-9][0-9]{3,}|0[0-9]{3})(Z|([+\\-])((0[0-9]|1[0-3]):[0-5][0-9]|14:00))?");

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

    @Override
    public Item copy(boolean mutable) {
        return new gYearItem(this.getDateTimeValue(), this.hasTimeZone);
    }

    private void getgYearFromString(String gYearString) {
        Matcher matcher = gYearRegex.matcher(gYearString);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Invalid xs:gYear: \"" + gYearString + "\"");
        }
        if (gYearString.startsWith("-")) {
            this.year = Year.of(-Integer.parseInt(matcher.group(1)));
        } else {
            this.year = Year.of(Integer.parseInt(matcher.group(1)));
        }
        String tz = matcher.group(2);
        if (tz == null) {
            this.hasTimeZone = false;
        } else {
            this.hasTimeZone = true;
            this.offset = ZoneOffset.of(tz);
        }
    }

    @Override
    public String getStringValue() {
        return String.format(
                "%s%04d%s",
                this.year.getValue() < 0 ? "-" : "",
                Math.abs(this.year.getValue()),
                this.hasTimeZone ? this.offset.toString() : "");
    }

    @Override
    public boolean isGYear() {
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
    public ItemType getDynamicType() {
        return BuiltinTypesCatalogue.gYearItem;
    }

    @Override
    public boolean isAtomic() {
        return true;
    }

    @Override
    public OffsetDateTime getDateTimeValue() {
        return OffsetDateTime.of(
                this.year.getValue(), 1, 1, 0, 0, 0, 0, this.hasTimeZone ? this.offset : ZoneOffset.UTC);
    }
}
