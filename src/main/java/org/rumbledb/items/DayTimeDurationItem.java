package org.rumbledb.items;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.rumbledb.types.AtomicItemType;
import org.rumbledb.types.ItemType;

public class DayTimeDurationItem extends DurationItem {

    private static final long serialVersionUID = 1L;

    public DayTimeDurationItem() {
        super();
    }

    public DayTimeDurationItem(Period value) {
        super();
        this.value = value.normalizedStandard(PeriodType.dayTime());
        this.isNegative = this.value.toString().contains("-");
    }

    @Override
    public Period getValue() {
        return this.value;
    }

    @Override
    public Period getDurationValue() {
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
    public boolean isTypeOf(ItemType type) {
        return type.equals(AtomicItemType.dayTimeDurationItem) || super.isTypeOf(type);
    }

    @Override
    public void read(Kryo kryo, Input input) {
        this.value = getDurationFromString(input.readString(), AtomicItemType.dayTimeDurationItem).normalizedStandard(
            PeriodType.dayTime()
        );
        this.isNegative = this.value.toString().contains("-");
    }

    @Override
    public boolean isCastableAs(ItemType itemType) {
        return itemType.equals(AtomicItemType.dayTimeDurationItem)
            ||
            itemType.equals(AtomicItemType.yearMonthDurationItem)
            ||
            itemType.equals(AtomicItemType.durationItem)
            ||
            itemType.equals(AtomicItemType.stringItem);
    }

    @Override
    public ItemType getDynamicType() {
        return AtomicItemType.dayTimeDurationItem;
    }
}
