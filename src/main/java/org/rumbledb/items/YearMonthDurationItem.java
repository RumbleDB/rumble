package org.rumbledb.items;


import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import org.joda.time.DurationFieldType;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.rumbledb.types.BuiltinTypesCatalogue;
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
    public void read(Kryo kryo, Input input) {
        this.value = getDurationFromString(input.readString(), BuiltinTypesCatalogue.yearMonthDurationItem)
            .normalizedStandard(
                yearMonthPeriodType
            );
        this.isNegative = this.value.toString().contains("-");
    }

    @Override
    public ItemType getDynamicType() {
        return BuiltinTypesCatalogue.yearMonthDurationItem;
    }


}
