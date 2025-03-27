package org.rumbledb.items;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import java.time.Period;

import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.ItemType;

public class DayTimeDurationItem extends DurationItem {

    private static final long serialVersionUID = 1L;

    public DayTimeDurationItem() {
        super();
    }

    public DayTimeDurationItem(Period value) {
        super();
        this.value = value;
        this.isNegative = this.value.toString().contains("-");
    }

    @Override
    public Period getValue() {
        return this.value;
    }

    @Override
    public Period getPeriodValue() {
        return this.value;
    }

    @Override
    public boolean isAtomic() {
        return true;
    }

    @Override
    public boolean isDayTimeDuration() {
        return true;
    }

    @Override
    public void read(Kryo kryo, Input input) {
        this.value = getDurationFromString(input.readString(), BuiltinTypesCatalogue.dayTimeDurationItem);
        this.isNegative = this.value.toString().contains("-");
    }

    @Override
    public ItemType getDynamicType() {
        return BuiltinTypesCatalogue.dayTimeDurationItem;
    }
}
