package org.rumbledb.items;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.Objects;
import java.util.regex.Pattern;

import org.rumbledb.api.Item;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.comparison.ComparisonExpression;
import org.rumbledb.runtime.misc.ComparisonIterator;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.ItemType;

public class YearMonthDurationItem implements Item {

    private static final long serialVersionUID = 1L;
    private Period value;
    Pattern durationPattern = Pattern.compile(
        "-?P((([0-9]+Y([0-9]+M)?([0-9]+D)?|([0-9]+M)([0-9]+D)?|([0-9]+D))(T(([0-9]+H)([0-9]+M)?([0-9]+(\\.[0-9]+)?S)?|([0-9]+M)([0-9]+(\\.[0-9]+)?S)?|([0-9]+(\\.[0-9]+)?S)))?)|(T(([0-9]+H)([0-9]+M)?([0-9]+(\\.[0-9]+)?S)?| ([0-9]+M)([0-9]+(\\.[0-9]+)?S)?| ([0-9]+(\\.[0-9]+)?S))))"
    );

    @SuppressWarnings("unused")
    public YearMonthDurationItem() {
        super();
    }

    public YearMonthDurationItem(Period value) {
        super();
        this.value = value;
    }

    public YearMonthDurationItem(String value) {
        this.value = normalizeMonthsToYears(Period.parse(value));
    }

    @Override
    public boolean isYearMonthDuration() {
        return true;
    }

    @Override
    public boolean isAtomic() {
        return true;
    }

    @Override
    public boolean isDuration() {
        return true;
    }

    @Override
    public boolean getEffectiveBooleanValue() {
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.value);
    }

    @Override
    public void write(Kryo kryo, Output output) {
        output.writeString(this.getStringValue());
    }

    @Override
    public void read(Kryo kryo, Input input) {
        this.value = normalizeMonthsToYears(Period.parse(input.readString()));
    }

    @Override
    public boolean equals(Object otherItem) {
        if (otherItem instanceof Item) {
            long c = ComparisonIterator.compareItems(
                this,
                (Item) otherItem,
                ComparisonExpression.ComparisonOperator.VC_EQ,
                ExceptionMetadata.EMPTY_METADATA
            );
            return c == 0;
        }
        return false;
    }

    @Override
    public String getStringValue() {
        if (!this.durationPattern.matcher(this.value.toString()).matches()) {
            throw new IllegalArgumentException("Invalid yearMonthDuration: " + this.value.toString());
        }
        return normalizeDuration(normalizeMonthsToYears(this.value));
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
            return "P0M"; // Default value for yearMonthDuration
        }
        StringBuilder sb = new StringBuilder();
        sb.append((period.isNegative()) ? "-P" : "P");
        if (period.getYears() != 0)
            sb.append(Math.abs(period.getYears())).append("Y");
        if (period.getMonths() != 0)
            sb.append(Math.abs(period.getMonths())).append("M");
        if (period.getDays() != 0)
            sb.append(Math.abs(period.getDays())).append("D");
        return sb.toString();
    }

    public static Period normalizeMonthsToYears(Period period) {
        Period normalized = period.normalized();
        if (normalized.getMonths() >= 12) {
            return normalized.minusMonths(normalized.getMonths() - (normalized.getMonths() % 12))
                .plusYears(normalized.getMonths() / 12);
        }
        return normalized;
    }

    @Override
    public int getYear() {
        return this.value.getYears();
    }

    @Override
    public int getMonth() {
        return this.value.getMonths();
    }

    @Override
    public int getDay() {
        return this.value.getDays();
    }
}
