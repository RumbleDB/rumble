/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributor acknowledgements are maintained in the CONTRIBUTORS file at the project root.
 */
package org.rumbledb.runtime.functions.util.formatting.pictures.FormatNumber;

import java.util.List;

import lombok.Getter;

import org.rumbledb.runtime.functions.util.formatting.GroupingPos;

public final class FormatNumberSubPicture {
    @Getter
    private final String rawPictureString;

    @Getter
    private final String prefix;

    @Getter
    private final String suffix;

    @Getter
    private final String integerPart;

    @Getter
    private final String fractionalPart;

    @Getter
    private final String exponentPart;

    private final boolean hasExponent;
    private final boolean hasPercent;
    private final boolean hasPerMille;

    @Getter
    private final List<GroupingPos> integerPartGroupingPositions;

    @Getter
    private final Integer repeatingIntegerGroupingInterval;

    @Getter
    private final List<GroupingPos> fractionalPartGroupingPositions;

    @Getter
    private final int minimumIntegerPartSize;

    @Getter
    private final int minimumFractionalPartSize;

    @Getter
    private final int maximumFractionalPartSize;

    @Getter
    private final int minimumExponentPartSize;

    @Getter
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
            int scalingFactor) {
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

    public boolean hasExponent() {
        return this.hasExponent;
    }

    public boolean getHasPercent() {
        return this.hasPercent;
    }

    public boolean getHasPerMille() {
        return this.hasPerMille;
    }
}
