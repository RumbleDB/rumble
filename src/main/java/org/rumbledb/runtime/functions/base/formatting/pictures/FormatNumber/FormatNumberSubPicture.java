package org.rumbledb.runtime.functions.base.formatting.pictures.FormatNumber;

import org.rumbledb.runtime.functions.base.formatting.GroupingPos;

import java.util.List;

public final class FormatNumberSubPicture {

    private final String prefix;
    private final String suffix;
    private final String integerPart;
    private final String fractionalPart;
    private final boolean hasPercent;
    private final boolean hasPerMille;
    private final List<GroupingPos> integerPartGroupingPositions;
    private final Integer repeatingIntegerGroupingInterval;
    private final List<GroupingPos> fractionalPartGroupingPositions;
    private final int minimumIntegerPartSize;
    private final int minimumFractionalPartSize;
    private final int maximumFractionalPartSize;

    public FormatNumberSubPicture(
            String prefix,
            String suffix,
            String integerPart,
            String fractionalPart,
            boolean hasPercent,
            boolean hasPerMille,
            List<GroupingPos> integerPartGroupingPositions,
            Integer repeatingIntegerGroupingInterval,
            List<GroupingPos> fractionalPartGroupingPositions,
            int minimumIntegerPartSize,
            int minimumFractionalPartSize,
            int maximumFractionalPartSize
    ) {
        this.prefix = prefix;
        this.suffix = suffix;
        this.integerPart = integerPart;
        this.fractionalPart = fractionalPart;
        this.hasPercent = hasPercent;
        this.hasPerMille = hasPerMille;
        this.integerPartGroupingPositions = integerPartGroupingPositions;
        this.repeatingIntegerGroupingInterval = repeatingIntegerGroupingInterval;
        this.fractionalPartGroupingPositions = fractionalPartGroupingPositions;
        this.minimumIntegerPartSize = minimumIntegerPartSize;
        this.minimumFractionalPartSize = minimumFractionalPartSize;
        this.maximumFractionalPartSize = maximumFractionalPartSize;
    }

    // Record-style accessors

    public String prefix() {
        return this.prefix;
    }

    public String suffix() {
        return this.suffix;
    }

    public String integerPart() {
        return this.integerPart;
    }

    public String fractionalPart() {
        return this.fractionalPart;
    }

    public boolean hasPercent() {
        return this.hasPercent;
    }

    public boolean hasPerMille() {
        return this.hasPerMille;
    }

    public List<GroupingPos> integerPartGroupingPositions() {
        return this.integerPartGroupingPositions;
    }

    public Integer repeatingIntegerGroupingInterval() {
        return this.repeatingIntegerGroupingInterval;
    }

    public List<GroupingPos> fractionalPartGroupingPositions() {
        return this.fractionalPartGroupingPositions;
    }

    public int minimumIntegerPartSize() {
        return this.minimumIntegerPartSize;
    }

    public int minimumFractionalPartSize() {
        return this.minimumFractionalPartSize;
    }

    public int maximumFractionalPartSize() {
        return this.maximumFractionalPartSize;
    }
}
