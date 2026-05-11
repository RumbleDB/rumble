package org.rumbledb.items;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeParseException;
import java.util.Comparator;
import java.util.Objects;
import java.util.regex.Pattern;

import org.rumbledb.api.Item;
import org.rumbledb.exceptions.DurationOverflowOrUnderflow;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.comparison.ComparisonExpression.ComparisonOperator;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.runtime.misc.ComparisonIterator;
import org.rumbledb.types.ItemType;

public class DurationItem implements Item {

    private static final long serialVersionUID = 1L;
    private Duration durationValue = Duration.ZERO;
    private Period periodValue = Period.ZERO;
    boolean isDuration = false;
    boolean isPeriod = false;
    Pattern durationRegex = Pattern.compile(
        "-?P((([0-9]+Y([0-9]+M)?([0-9]+D)?|([0-9]+M)([0-9]+D)?|([0-9]+D))(T(([0-9]+H)([0-9]+M)?([0-9]+(\\.[0-9]+)?S)?|([0-9]+M)([0-9]+(\\.[0-9]+)?S)?|([0-9]+(\\.[0-9]+)?S)))?)|(T(([0-9]+H)([0-9]+M)?([0-9]+(\\.[0-9]+)?S)?|([0-9]+M)([0-9]+(\\.[0-9]+)?S)?|([0-9]+(\\.[0-9]+)?S))))"
    );

    @SuppressWarnings("unused")
    public DurationItem() {
        super();
    }

    public DurationItem(Duration value) {
        super();
        this.durationValue = value;
    }

    public DurationItem(String value) {
        if (!this.durationRegex.matcher(value).matches()) {
            throw new IllegalArgumentException("Invalid duration: " + value);
        }
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
        if (Objects.isNull(this.durationValue) && Objects.isNull(this.periodValue)) {
            return Duration.ZERO;
        } else if (Objects.isNull(this.periodValue)) {
            return this.durationValue;
        }
        LocalDateTime anchor = LocalDateTime.of(2000, 1, 1, 0, 0);
        LocalDateTime target = anchor.plus(this.periodValue);
        return Duration.between(anchor, target)
            .plus(Objects.isNull(this.durationValue) ? Duration.ofDays(0) : this.durationValue);
    }

    public Period getPeriodValue() {
        return this.periodValue;
    }

    @Override
    public String getStringValue() {
        return normalizeDuration(normalizeMonthsToYears(this.periodValue), this.durationValue);
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
                this.periodValue = normalizeMonthsToYears(Period.parse(periodString));
                this.isPeriod = true;
            }
            if (durationPeriodString.contains("T")) {
                String durationString = "PT" + durationPeriodString.split("T")[1];
                if (durationPeriodString.startsWith("-")) {
                    durationString = "-" + durationString;
                }
                this.durationValue = Duration.parse(durationString);
                this.isDuration = true;
            }
        } catch (DateTimeParseException e) {
            throw new DurationOverflowOrUnderflow(
                    "Invalid xs:duration: \"" + durationPeriodString + "\"",
                    ExceptionMetadata.EMPTY_METADATA
            );
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

    @Override
    public long getEpochMillis() {
        if (Objects.isNull(this.durationValue) && Objects.isNull(this.periodValue)) {
            return 0;
        } else if (Objects.isNull(this.periodValue)) {
            return this.durationValue.toMillis();
        }
        LocalDateTime anchor = LocalDateTime.of(2000, 1, 1, 0, 0);
        LocalDateTime target = anchor.plus(this.periodValue);
        return Duration.between(anchor, target)
            .plus(Objects.isNull(this.durationValue) ? Duration.ofDays(0) : this.durationValue)
            .toMillis();
    }

    public static String normalizeDuration(Period period, Duration duration) {
        if (period.isZero() && duration.isZero()) {
            return duration.toString();
        }
        long seconds = duration.getSeconds();
        long absSeconds = Math.abs(seconds);

        long daysFromDuration = absSeconds / (24 * 3600);
        absSeconds %= 24 * 3600;
        long hours = absSeconds / 3600;
        absSeconds %= 3600;
        long minutes = absSeconds / 60;
        long secs = absSeconds % 60;

        long totalDays = period.getDays() + (seconds >= 0 ? daysFromDuration : -daysFromDuration);
        BigDecimal totalSeconds;
        if (seconds >= 0) {
            totalSeconds = BigDecimal.valueOf(secs).add(BigDecimal.valueOf(duration.getNano(), 9));
        } else {
            totalSeconds = BigDecimal.valueOf(secs).subtract(BigDecimal.valueOf(duration.getNano(), 9));
        }

        StringBuilder sb = new StringBuilder();
        sb.append((seconds < 0 || period.isNegative()) ? "-P" : "P");
        if (period.getYears() != 0)
            sb.append(Math.abs(period.getYears())).append("Y");
        if (period.getMonths() != 0)
            sb.append(Math.abs(period.getMonths())).append("M");
        if (totalDays != 0)
            sb.append(Math.abs(totalDays)).append("D");

        if (hours != 0 || minutes != 0 || totalSeconds.signum() != 0) {
            sb.append("T");
            if (hours != 0)
                sb.append(Math.abs(hours)).append("H");
            if (minutes != 0)
                sb.append(Math.abs(minutes)).append("M");
            if (totalSeconds.signum() != 0)
                sb.append(totalSeconds.abs().stripTrailingZeros().toPlainString()).append("S");
        }
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
        return this.periodValue.getYears();
    }

    @Override
    public int getMonth() {
        return this.periodValue.getMonths();
    }

    @Override
    public int getDay() {
        return this.periodValue.getDays();
    }

    @Override
    public int getHour() {
        return (int) ((this.durationValue.getSeconds() / 3600) % 24);
    }

    @Override
    public int getMinute() {
        return (int) ((this.durationValue.getSeconds() / 60) % 60);
    }

    @Override
    public double getSecond() {
        return (this.durationValue.getSeconds() % 60 + this.durationValue.getNano() / 1_000_000_000.0);
    }

    @Override
    public int getNanosecond() {
        return this.durationValue.getNano();
    }
}
