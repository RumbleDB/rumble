package org.rumbledb.runtime.functions.base.formatting.pictures.FormatNumber;

import org.rumbledb.runtime.functions.base.formatting.GroupingPos;

import java.util.List;

public final class FormatNumberSubPicture {
    private final String rawPictureString;
    private final String prefix;
    private final String suffix;
    private final String integerPart;
    private final String fractionalPart;
    private final String exponentPart;
    private final boolean hasExponent;
    private final boolean hasPercent;
    private final boolean hasPerMille;
    private final List<GroupingPos> integerPartGroupingPositions;
    private final Integer repeatingIntegerGroupingInterval;
    private final List<GroupingPos> fractionalPartGroupingPositions;
    private final int minimumIntegerPartSize;
    private final int minimumFractionalPartSize;
    private final int maximumFractionalPartSize;
    private final int minimumExponentPartSize;
    private final int scalingFactor;

    public FormatNumberSubPicture(
            String rawPictureString,
            String prefix,
            String suffix,
            String integerPart,
            String fractionalPart,
            String exponentPart,
            boolean hasExponent,
            boolean hasPercent,
            boolean hasPerMille,
            List<GroupingPos> integerPartGroupingPositions,
            Integer repeatingIntegerGroupingInterval,
            List<GroupingPos> fractionalPartGroupingPositions,
            int minimumIntegerPartSize,
            int minimumFractionalPartSize,
            int maximumFractionalPartSize,
            int minimumExponentPartSize,
            int scalingFactor
    ) {
        this.rawPictureString = rawPictureString;
        this.prefix = prefix;
        this.suffix = suffix;
        this.integerPart = integerPart;
        this.fractionalPart = fractionalPart;
        this.exponentPart = exponentPart;
        this.hasExponent = hasExponent;
        this.hasPercent = hasPercent;
        this.hasPerMille = hasPerMille;
        this.integerPartGroupingPositions = integerPartGroupingPositions;
        this.repeatingIntegerGroupingInterval = repeatingIntegerGroupingInterval;
        this.fractionalPartGroupingPositions = fractionalPartGroupingPositions;
        this.minimumIntegerPartSize = minimumIntegerPartSize;
        this.minimumFractionalPartSize = minimumFractionalPartSize;
        this.maximumFractionalPartSize = maximumFractionalPartSize;
        this.minimumExponentPartSize = minimumExponentPartSize;
        this.scalingFactor = scalingFactor;
    }

    public String getRawPictureString() {
        return this.rawPictureString;
    }

    public String getPrefix() {
        return this.prefix;
    }

    public String getSuffix() {
        return this.suffix;
    }

    public String getIntegerPart() {
        return this.integerPart;
    }

    public String getFractionalPart() {
        return this.fractionalPart;
    }

    public String getExponentPart() {
        return this.exponentPart;
    }

    public boolean hasExponent() {
        return this.hasExponent;
    }

    public boolean getHasPercent() {
        return this.hasPercent;
    }

    public boolean getHasPerMille() {
        return this.hasPerMille;
    }

    public List<GroupingPos> getIntegerPartGroupingPositions() {
        return this.integerPartGroupingPositions;
    }

    public Integer getRepeatingIntegerGroupingInterval() {
        return this.repeatingIntegerGroupingInterval;
    }

    public List<GroupingPos> getFractionalPartGroupingPositions() {
        return this.fractionalPartGroupingPositions;
    }

    public int getMinimumIntegerPartSize() {
        return this.minimumIntegerPartSize;
    }

    public int getMinimumFractionalPartSize() {
        return this.minimumFractionalPartSize;
    }

    public int getMaximumFractionalPartSize() {
        return this.maximumFractionalPartSize;
    }

    public int getMinimumExponentPartSize() {
        return this.minimumExponentPartSize;
    }

    public int getScalingFactor() {
        return this.scalingFactor;
    }
}
