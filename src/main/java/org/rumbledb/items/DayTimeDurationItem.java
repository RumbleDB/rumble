package org.rumbledb.items;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.text.DecimalFormat;
import java.time.Duration;

import org.rumbledb.api.Item;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.ItemType;

public class DayTimeDurationItem implements Item {

    private static final long serialVersionUID = 1L;
    private Duration value;

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
        this.value = Duration.parse(value);
    }

    @Override
    public boolean isDayTimeDuration() {
        return true;
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

    public static String normalizeDuration(Duration duration) {
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
}
