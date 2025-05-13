package org.rumbledb.items;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.text.DecimalFormat;
import java.time.Duration;
import java.time.Period;
import java.util.Objects;
import java.util.regex.Pattern;

import org.rumbledb.api.Item;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.comparison.ComparisonExpression;
import org.rumbledb.runtime.misc.ComparisonIterator;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.ItemType;

public class DayTimeDurationItem implements Item {

    private static final long serialVersionUID = 1L;
    private Duration value;
    Pattern dayTimeDurationRegex = Pattern.compile("^-?P(\\d+D)?(T(\\d+H)?(\\d+M)?(\\d+(\\.\\d+)?S)?)?$");


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
        if (!this.dayTimeDurationRegex.matcher(value).matches()) {
            throw new IllegalArgumentException("Invalid day time duration: " + value);
        }
        this.value = Duration.parse(value);
    }

    @Override
    public boolean isDayTimeDuration() {
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
    public void read(Kryo kryo, Input input) {
        this.value = Duration.parse(input.readString());
    }

    @Override
    public void write(Kryo kryo, Output output) {
        output.writeString(this.getStringValue());
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
    public ItemType getDynamicType() {
        return BuiltinTypesCatalogue.dayTimeDurationItem;
    }

    @Override
    public String getStringValue() {
        return normalizeDuration(this.value);
    }

    @Override
    public long getEpochMilis() {
        return this.value.toMillis();
    }

    @Override
    public Duration getDurationValue() {
        return this.value;
    }

    @Override
    public Period getPeriodValue() {
        if (this.value.isZero()) {
            return Period.ZERO;
        }
        return Period.from(this.value);
    }

    public static String normalizeDuration(Duration duration) {
        if (duration.isZero()) {
            return "PT0S"; // Default value for empty dayTimeDuration
        }
        boolean isNegative = duration.isNegative();
        duration = duration.abs();

        long totalSeconds = duration.getSeconds();
        int nanos = duration.getNano();

        long days = totalSeconds / 86_400;
        long hours = (totalSeconds % 86_400) / 3600;
        long minutes = (totalSeconds % 3600) / 60;
        long seconds = totalSeconds % 60;

        double fractionalSeconds = seconds + nanos / 1_000_000_000.0;

        StringBuilder sb = new StringBuilder();
        if (isNegative)
            sb.append("-");

        sb.append("P");
        if (days > 0)
            sb.append(days).append("D");

        if (hours > 0 || minutes > 0 || fractionalSeconds > 0 || sb.toString().endsWith("P")) {
            sb.append("T");
            if (hours > 0)
                sb.append(hours).append("H");
            if (minutes > 0)
                sb.append(minutes).append("M");

            if (fractionalSeconds > 0) {
                // Format seconds with optional fraction
                DecimalFormat df = new DecimalFormat("0.#########"); // up to nanosecond precision
                sb.append(df.format(fractionalSeconds)).append("S");
            }
        }

        return sb.toString();
    }

    @Override
    public int getMonth() {
        return (int) (this.value.getSeconds() / 86400 / 30);
    }

    @Override
    public int getDay() {
        return (int) ((this.value.getSeconds() + this.value.getNano() / 1_000_000_000.0) / 3600 / 24);
    }

    @Override
    public int getHour() {
        return (int) ((this.value.getSeconds() / 3600) % 24);
    }

    @Override
    public int getMinute() {
        return (int) ((this.value.getSeconds() / 60) % 60);
    }

    @Override
    public double getSecond() {
        return (this.value.getSeconds() % 60 + this.value.getNano() / 1_000_000_000.0);
    }

    @Override
    public int getNanosecond() {
        return this.value.getNano();
    }

    @Override
    public int getYear() {
        return (int) (this.value.getSeconds() / 86400 / 30 / 365);
    }
}
