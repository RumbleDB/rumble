package org.rumbledb.items;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.rumbledb.api.Item;
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
        return type.equals(ItemType.dayTimeDurationItem) || super.isTypeOf(type);
    }

    @Override
    public void read(Kryo kryo, Input input) {
        this.value = getDurationFromString(input.readString(), ItemType.dayTimeDurationItem).normalizedStandard(
            PeriodType.dayTime()
        );
        this.isNegative = this.value.toString().contains("-");
    }

    @Override
    public boolean isCastableAs(ItemType itemType) {
        return itemType.equals(ItemType.dayTimeDurationItem)
            ||
            itemType.equals(ItemType.yearMonthDurationItem)
            ||
            itemType.equals(ItemType.durationItem)
            ||
            itemType.equals(ItemType.stringItem);
    }

    @Override
    public Item castAs(ItemType itemType) {
        if (itemType.equals(ItemType.durationItem)) {
            return ItemFactory.getInstance().createDurationItem(this.getValue());
        }
        if (itemType.equals(ItemType.dayTimeDurationItem)) {
            return this;
        }
        if (itemType.equals(ItemType.yearMonthDurationItem)) {
            return ItemFactory.getInstance().createYearMonthDurationItem(this.getValue());
        }
        if (itemType.equals(ItemType.stringItem)) {
            return ItemFactory.getInstance().createStringItem(this.serialize());
        }
        throw new ClassCastException();
    }

    @Override
    public Item add(Item other) {
        if (other.isDateTime()) {
            return ItemFactory.getInstance()
                .createDateTimeItem(other.getDateTimeValue().plus(this.getValue()), other.hasTimeZone());
        }
        if (other.isDate()) {
            return ItemFactory.getInstance()
                .createDateItem(other.getDateTimeValue().plus(this.getValue()), other.hasDateTime());
        }
        if (other.isTime()) {
            return ItemFactory.getInstance()
                .createTimeItem(other.getDateTimeValue().plus(this.getValue()), other.hasDateTime());
        }
        return ItemFactory.getInstance().createDayTimeDurationItem(this.getValue().plus(other.getDurationValue()));
    }

    @Override
    public Item subtract(Item other) {
        return ItemFactory.getInstance().createDayTimeDurationItem(this.getValue().minus(other.getDurationValue()));
    }

    @Override
    public ItemType getDynamicType() {
        return ItemType.dayTimeDurationItem;
    }
}
