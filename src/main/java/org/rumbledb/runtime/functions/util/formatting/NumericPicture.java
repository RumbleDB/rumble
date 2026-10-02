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
package org.rumbledb.runtime.functions.util.formatting;

import java.util.List;

import lombok.Getter;

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
            int repeatingGroupingInterval) {
        this.zeroDigit = zeroDigit;
        this.mandatoryDigitCount = mandatoryDigitCount;
        this.activeDigitCount = activeDigitCount;
        this.groupingPositions = List.copyOf(groupingPositions);
        this.repeatingGrouping = repeatingGrouping;
        this.repeatingGroupingInterval = repeatingGroupingInterval;
    }
}
