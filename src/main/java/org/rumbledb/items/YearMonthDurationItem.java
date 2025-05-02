package org.rumbledb.items;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.ItemType;

public class YearMonthDurationItem extends DurationItem {

    private static final long serialVersionUID = 1L;
    private Period value;

    @SuppressWarnings("unused")
    public YearMonthDurationItem() {
        super();
    }

    public YearMonthDurationItem(Period value) {
        super();
        this.value = value;
    }

    public YearMonthDurationItem(String value) {
        this.value = Period.parse(value);
    }

    @Override
    public boolean isYearMonthDuration() {
        return true;
    }

    @Override
    public void read(Kryo kryo, Input input) {
        this.value = Period.parse(input.readString());
    }

    @Override
    public String getStringValue() {
        return normalizeDuration(this.value, Duration.ZERO);
    }

    @Override
    public ItemType getDynamicType() {
        return BuiltinTypesCatalogue.yearMonthDurationItem;
    }

    @Override
    public Duration getDurationValue() {
        LocalDateTime anchor = LocalDateTime.of(2000, 1, 1, 0, 0);
        LocalDateTime target = anchor.plus(this.value);
        return Duration.between(anchor, target);
    }

    public Period getPeriodValue() {
        return this.value;
    }

    @Override
    public long getEpochMilis() {
        LocalDateTime anchor = LocalDateTime.of(2000, 1, 1, 0, 0);
        LocalDateTime target = anchor.plus(this.value);
        return Duration.between(anchor, target).toMillis();
    }
}
