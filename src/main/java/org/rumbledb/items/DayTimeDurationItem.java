package org.rumbledb.items;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;

import java.time.Duration;
import java.time.Period;

import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.ItemType;

public class DayTimeDurationItem extends DurationItem {

    private static final long serialVersionUID = 1L;
    private Duration value;

    @SuppressWarnings("unused")
    public DayTimeDurationItem() {
        super();
    }

    public DayTimeDurationItem(Duration value) {
        super();
        this.value = value;
    }

    public DayTimeDurationItem(String value) {
        super();
        this.value = Duration.parse(value);
    }

    @Override
    public boolean isDayTimeDuration() {
        return true;
    }

    @Override
    public void read(Kryo kryo, Input input) {
        this.value = Duration.parse(input.readString());
    }

    @Override
    public ItemType getDynamicType() {
        return BuiltinTypesCatalogue.dayTimeDurationItem;
    }

    @Override
    public String getStringValue() {
        return normalizeDuration(Period.ZERO, this.value);
    }

    @Override
    public long getEpochMilis() {
        return this.value.toMillis();
    }

    @Override
    public Duration getDurationValue() {
        return this.value;
    }
}
