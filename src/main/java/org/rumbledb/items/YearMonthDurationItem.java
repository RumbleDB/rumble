package org.rumbledb.items;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import java.time.Period;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.ItemType;

public class YearMonthDurationItem extends DurationItem {

    private static final long serialVersionUID = 1L;
    private Period value;

    public YearMonthDurationItem() {
        super();
    }

    public YearMonthDurationItem(Period value) {
        super();
        this.value = value.normalized();
        this.isNegative = this.value.getYears() < 0 || this.value.getMonths() < 0;
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
    public boolean isYearMonthDuration() {
        return true;
    }

    @Override
    public void read(Kryo kryo, Input input) {
        this.value = getDurationFromString(input.readString(), BuiltinTypesCatalogue.yearMonthDurationItem);
        this.value = this.value.normalized();
        this.isNegative = this.value.getYears() < 0 || this.value.getMonths() < 0;
    }

    @Override
    public ItemType getDynamicType() {
        return BuiltinTypesCatalogue.yearMonthDurationItem;
    }
}
