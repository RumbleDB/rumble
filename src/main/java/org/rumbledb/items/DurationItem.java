package org.rumbledb.items;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.time.Duration;
import java.time.LocalDate;
import java.time.Period;
import java.util.Comparator;
import java.util.Objects;

import org.rumbledb.api.Item;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.comparison.ComparisonExpression.ComparisonOperator;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.runtime.misc.ComparisonIterator;
import org.rumbledb.types.ItemType;

public class DurationItem implements Item {

    private static final long serialVersionUID = 1L;
    private Duration durationValue;
    private Period periodValue;
    boolean isDuration = false;
    boolean isPeriod = false;

    @SuppressWarnings("unused")
    public DurationItem() {
        super();
    }

    public DurationItem(Duration value) {
        super();
        this.durationValue = value;
    }

    public DurationItem(String value) {
        getDurationFromString(value);
    }

    @Override
    public boolean equals(Object otherItem) {
        if (otherItem instanceof Item) {
            long c = ComparisonIterator.compareItems(
                this,
                (Item) otherItem,
                ComparisonOperator.VC_EQ,
                ExceptionMetadata.EMPTY_METADATA
            );
            return c == 0;
        }
        return false;
    }

    public Duration getDurationValue() {
        return this.durationValue;
    }

    public Period getPeriodValue() {
        return this.periodValue;
    }

    @Override
    public String getStringValue() {
        String stringDuration = "";
        String stringPeriod = "";
        if (this.isDuration) {
            stringDuration = this.durationValue.toString();
            if (this.isPeriod) {
                stringDuration = stringDuration.substring(1);
            }
        }
        if (this.isPeriod) {
            stringPeriod = this.periodValue.toString();
        }

        return stringPeriod + stringDuration;
    }

    @Override
    public boolean isAtomic() {
        return true;
    }

    @Override
    public boolean isDuration() {
        return this.isDuration;
    }

    @Override
    public boolean isPeriod() {
        return this.isPeriod;
    }

    @Override
    public boolean getEffectiveBooleanValue() {
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.durationValue, this.periodValue);
    }

    @Override
    public void write(Kryo kryo, Output output) {
        output.writeString(this.getStringValue());
    }

    @Override
    public void read(Kryo kryo, Input input) {
        getDurationFromString(input.readString());
    }

    private void getDurationFromString(String durationPeriodString) {
        try {
            if (!durationPeriodString.contains("PT")) {
                String periodString = durationPeriodString.split("T")[0];
                this.periodValue = Period.parse(periodString);
                this.isPeriod = true;
            }
            if (durationPeriodString.contains("T")) {
                String durationString = "PT" + durationPeriodString.split("T")[1];
                this.durationValue = Duration.parse(durationString);
                this.isDuration = true;
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid xs:duration format: " + durationPeriodString, e);
        }
    }

    @Override
    public ItemType getDynamicType() {
        return BuiltinTypesCatalogue.durationItem;
    }

    public static Comparator<Period> periodComparator = (p1, p2) -> {
        LocalDate base = LocalDate.of(2000, 1, 1);
        return base.plus(p1).compareTo(base.plus(p2));
    };

    public long getSeconds() {
        return this.durationValue.getSeconds();
    }

    public int getMonths() {
        return this.periodValue.getMonths();
    }
}
