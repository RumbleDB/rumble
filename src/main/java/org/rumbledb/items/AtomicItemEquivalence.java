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
package org.rumbledb.items;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.Arrays;

import org.rumbledb.api.Item;

/**
 * Defines the stable Java equality and hashing contract for atomic items.
 *
 * Query value comparisons are intentionally not used here: they can be context-dependent, can
 * raise dynamic errors, and do not form an equivalence relation. The XDM {@code op:same-key}
 * relation is stable and is already the equality required by maps.
 */
public final class AtomicItemEquivalence {

    private AtomicItemEquivalence() {}

    public static boolean equivalent(Item left, Item right) {
        if (left == right) {
            return true;
        }
        if (left == null || right == null || !left.isAtomic() || !right.isAtomic()) {
            return false;
        }
        if (left.isNull() || right.isNull()) {
            return left.isNull() && right.isNull();
        }
        return MapAtomicSameKey.sameKey(left, right);
    }

    public static int hash(Item item) {
        if (item == null || !item.isAtomic()) {
            return 0;
        }
        if (item.isNull()) {
            return 0x4E554C4C;
        }
        if (item.isString() || item.isAnyURI() || item.isUntypedAtomic()) {
            String value = item.getStringValue();
            return value == null ? 0 : value.hashCode();
        }
        if (item.isNumeric()) {
            return numericHash(item);
        }
        // The Gregorian and duration hashes use the values that ComparisonIterator compares.
        if (item.isTime()) {
            return item.getTimeValue()
                    .atDate(LocalDate.of(1970, 1, 1))
                    .toInstant()
                    .hashCode();
        }
        if (item.isDate()
                || item.isDateTime()
                || item.isGYear()
                || item.isGYearMonth()
                || item.isGMonth()
                || item.isGMonthDay()
                || item.isGDay()) {
            return item.getDateTimeValue().toInstant().hashCode();
        }
        if (item.isDuration()) {
            // Durations of different subtypes are the same key when their months and their
            // day-time components are equal, e.g. xs:yearMonthDuration('P1Y') and xs:duration('P12M').
            Period period = item.getPeriodValue();
            long months = period == null ? 0 : period.toTotalMonths();
            return 31 * Long.hashCode(months)
                    + item.getDayTimeDurationComponent().hashCode();
        }
        if (item.isHexBinary() || item.isBase64Binary()) {
            return Arrays.hashCode(item.getBinaryValue());
        }
        if (item.isBoolean()) {
            return Boolean.hashCode(item.getBooleanValue());
        }
        if (item.isQName()) {
            // Name compares the namespace and the local name, but not the prefix.
            return item.getQNameValue().hashCode();
        }
        if (item.isNotation()) {
            return item.getNotationValue().hashCode();
        }
        // Future atomic types remain correct with a conservative hash.
        return 0x4F;
    }

    private static int numericHash(Item item) {
        if ((item.isFloat() && Float.isNaN(item.getFloatValue()))
                || (item.isDouble() && Double.isNaN(item.getDoubleValue()))) {
            return 0x4E614E;
        }
        if (item.isDouble()) {
            double value = item.getDoubleValue();
            if (value == Double.POSITIVE_INFINITY) {
                return 0x2B494E46;
            }
            if (value == Double.NEGATIVE_INFINITY) {
                return 0x2D494E46;
            }
        }
        if (item.isFloat()) {
            float value = item.getFloatValue();
            if (value == Float.POSITIVE_INFINITY) {
                return 0x2B494E46;
            }
            if (value == Float.NEGATIVE_INFINITY) {
                return 0x2D494E46;
            }
        }
        try {
            BigDecimal value = exactDecimal(item);
            if (value.signum() == 0) {
                return 0;
            }
            // Equal non-zero values have the same unscaled value and scale once trailing zeros are stripped.
            return value.stripTrailingZeros().hashCode();
        } catch (RuntimeException e) {
            return 1;
        }
    }

    private static BigDecimal exactDecimal(Item item) {
        if (item.isDecimal()) {
            return item.getDecimalValue();
        }
        if (item.isInteger()) {
            return new BigDecimal(item.getIntegerValue());
        }
        if (item.isInt()) {
            return BigDecimal.valueOf(item.getIntValue());
        }
        if (item.isDouble()) {
            return new BigDecimal(item.getDoubleValue());
        }
        if (item.isFloat()) {
            return new BigDecimal(item.getFloatValue());
        }
        return item.castToDecimalValue();
    }
}
