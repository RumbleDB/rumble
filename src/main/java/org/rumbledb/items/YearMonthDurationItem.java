package org.rumbledb.items;


import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import org.joda.time.DurationFieldType;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.rumbledb.api.Item;
import org.rumbledb.types.ItemType;


public class YearMonthDurationItem extends DurationItem {

    private static final long serialVersionUID = 1L;
    private Period value;
    public static final PeriodType yearMonthPeriodType = PeriodType.forFields(
        new DurationFieldType[] { DurationFieldType.years(), DurationFieldType.months() }
    );

    public YearMonthDurationItem() {
        super();
    }

    public YearMonthDurationItem(Period value) {
        super();
        this.value = value.normalizedStandard(yearMonthPeriodType);
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
    public boolean isYearMonthDuration() {
        return true;
    }

    @Override
    public boolean isTypeOf(ItemType type) {
        return type.equals(ItemType.yearMonthDurationItem) || super.isTypeOf(type);
    }

    @Override
    public void read(Kryo kryo, Input input) {
        this.value = getDurationFromString(input.readString(), ItemType.yearMonthDurationItem).normalizedStandard(
            yearMonthPeriodType
        );
        this.isNegative = this.value.toString().contains("-");
    }

    @Override
    public boolean isCastableAs(ItemType itemType) {
        return itemType.equals(ItemType.yearMonthDurationItem)
            ||
            itemType.equals(ItemType.dayTimeDurationItem)
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
        if (itemType.equals(ItemType.yearMonthDurationItem)) {
            return this;
        }
        if (itemType.equals(ItemType.dayTimeDurationItem)) {
            return ItemFactory.getInstance().createDayTimeDurationItem(this.getValue());
        }
        if (itemType.equals(ItemType.stringItem)) {
            return ItemFactory.getInstance().createStringItem(this.serialize());
        }
        throw new ClassCastException();
    }

    @Override
    public ItemType getDynamicType() {
        return ItemType.yearMonthDurationItem;
    }
}
