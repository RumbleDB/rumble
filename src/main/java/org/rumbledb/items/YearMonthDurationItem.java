package org.rumbledb.items;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;

import org.rumbledb.api.Item;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.ItemType;

public class YearMonthDurationItem implements Item {

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
        this.value = Period.parse(value).normalized();
    }

    @Override
    public boolean isYearMonthDuration() {
        return true;
    }

    @Override
    public void write(Kryo kryo, Output output) {
        output.writeString(this.getStringValue());
    }

    @Override
    public void read(Kryo kryo, Input input) {
        this.value = Period.parse(input.readString()).normalized();
    }

    @Override
    public String getStringValue() {
        return normalizeDuration(this.value);
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

    public static String normalizeDuration(Period period) {
        if (period.isZero()) {
            return period.toString();
        }
        StringBuilder sb = new StringBuilder();
        sb.append((period.isNegative()) ? "-P" : "P");
        if (period.getYears() != 0)
            sb.append(Math.abs(period.getYears())).append("Y");
        if (period.getMonths() != 0)
            sb.append(Math.abs(period.getMonths())).append("M");
        return sb.toString();
    }
}
