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
package org.rumbledb.runtime.functions.datetime.dateformatting;

final class ParsedTimezonePicture {
    final boolean gmtPrefix;
    final boolean alwaysShowMinutes;
    final String separator;
    final int hourWidth;
    final int minuteWidth;
    final int zeroDigit;
    final boolean zuluForZeroOffset;
    final boolean minutesOptionalIfZero;
    final boolean compactNoSeparator;
    final boolean military;

    final boolean named;
    final String namePresentation;

    private ParsedTimezonePicture(
            boolean gmtPrefix,
            boolean alwaysShowMinutes,
            String separator,
            int hourWidth,
            int minuteWidth,
            int zeroDigit,
            boolean zuluForZeroOffset,
            boolean minutesOptionalIfZero,
            boolean compactNoSeparator,
            boolean military,
            boolean named,
            String namePresentation) {
        this.gmtPrefix = gmtPrefix;
        this.alwaysShowMinutes = alwaysShowMinutes;
        this.separator = separator;
        this.hourWidth = hourWidth;
        this.minuteWidth = minuteWidth;
        this.zeroDigit = zeroDigit;
        this.zuluForZeroOffset = zuluForZeroOffset;
        this.minutesOptionalIfZero = minutesOptionalIfZero;
        this.compactNoSeparator = compactNoSeparator;
        this.military = military;
        this.named = named;
        this.namePresentation = namePresentation;
    }

    static ParsedTimezonePicture military() {
        return new ParsedTimezonePicture(false, false, ":", 2, 2, '0', false, false, false, true, false, null);
    }

    static ParsedTimezonePicture defaultNumeric() {
        return new ParsedTimezonePicture(false, true, ":", 2, 2, '0', false, false, false, false, false, null);
    }

    static ParsedTimezonePicture named(String namePresentation) {
        return new ParsedTimezonePicture(
                false, true, ":", 2, 2, '0', false, false, false, false, true, namePresentation);
    }

    static ParsedTimezonePicture custom(
            boolean gmtPrefix,
            boolean alwaysShowMinutes,
            String sep,
            int hourWidth,
            int minuteWidth,
            int zeroDigit,
            boolean zuluForZeroOffset,
            boolean minutesOptionalIfZero,
            boolean compactNoSeparator) {
        return new ParsedTimezonePicture(
                gmtPrefix,
                alwaysShowMinutes,
                sep,
                hourWidth,
                minuteWidth,
                zeroDigit,
                zuluForZeroOffset,
                minutesOptionalIfZero,
                compactNoSeparator,
                false,
                false,
                null);
    }
}
