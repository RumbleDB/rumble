package org.rumbledb.runtime.functions.util.formatting;

import lombok.Getter;

import java.util.List;

@Getter
public final class NumericPicture {
    private final int zeroDigit;
    private final int mandatoryDigitCount;
    private final int activeDigitCount;
    private final List<GroupingPos> groupingPositions;
    private final boolean repeatingGrouping;
    private final int repeatingGroupingInterval;

    NumericPicture(
            int zeroDigit,
            int mandatoryDigitCount,
            int activeDigitCount,
            List<GroupingPos> groupingPositions,
            boolean repeatingGrouping,
            int repeatingGroupingInterval
    ) {
        this.zeroDigit = zeroDigit;
        this.mandatoryDigitCount = mandatoryDigitCount;
        this.activeDigitCount = activeDigitCount;
        this.groupingPositions = List.copyOf(groupingPositions);
        this.repeatingGrouping = repeatingGrouping;
        this.repeatingGroupingInterval = repeatingGroupingInterval;
    }

}
