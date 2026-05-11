package org.rumbledb.runtime.functions.base.formatting;

import java.util.List;

public final class NumericPicture {
    private final String raw;
    private final int zeroDigit;
    private final int mandatoryDigitCount;
    private final int activeDigitCount;
    private final List<GroupingPos> groupingPositions;
    private final boolean repeatingGrouping;
    private final int repeatingGroupingInterval;

    NumericPicture(
            String raw,
            int zeroDigit,
            int mandatoryDigitCount,
            int activeDigitCount,
            List<GroupingPos> groupingPositions,
            boolean repeatingGrouping,
            int repeatingGroupingInterval
    ) {
        this.raw = raw;
        this.zeroDigit = zeroDigit;
        this.mandatoryDigitCount = mandatoryDigitCount;
        this.activeDigitCount = activeDigitCount;
        this.groupingPositions = List.copyOf(groupingPositions);
        this.repeatingGrouping = repeatingGrouping;
        this.repeatingGroupingInterval = repeatingGroupingInterval;
    }

    public String getRaw() {
        return this.raw;
    }

    public int getZeroDigit() {
        return this.zeroDigit;
    }

    public int getMandatoryDigitCount() {
        return this.mandatoryDigitCount;
    }

    public int getActiveDigitCount() {
        return this.activeDigitCount;
    }

    public List<GroupingPos> getGroupingPositions() {
        return this.groupingPositions;
    }

    public boolean isRepeatingGrouping() {
        return this.repeatingGrouping;
    }

    public int getRepeatingGroupingInterval() {
        return this.repeatingGroupingInterval;
    }
}
