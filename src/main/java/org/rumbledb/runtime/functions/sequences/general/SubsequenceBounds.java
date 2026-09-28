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
package org.rumbledb.runtime.functions.sequences.general;

import java.math.BigInteger;

/** The XPath position predicate, converted to a zero-based slice of a known size. */
final class SubsequenceBounds {
    private final double start;
    private final double end;

    SubsequenceBounds(double start, Double length) {
        this.start = round(start);
        // Addition must remain double arithmetic, including infinity and NaN rules.
        this.end = length == null ? Double.POSITIVE_INFINITY : this.start + round(length);
    }

    Slice slice(BigInteger size) {
        if (Double.isNaN(this.start) || Double.isNaN(this.end) || this.end <= this.start) {
            return new Slice(BigInteger.ZERO, BigInteger.ZERO);
        }
        BigInteger offset = lowerBound(this.start, size);
        return new Slice(offset, lowerBound(this.end, size).subtract(offset));
    }

    private static double round(double value) {
        // Every finite double at this magnitude is already an integer.
        if (!Double.isFinite(value) || Math.abs(value) >= 0x1.0p52) {
            return value;
        }
        double floor = Math.floor(value);
        return value - floor >= 0.5 ? floor + 1 : floor;
    }

    private static BigInteger lowerBound(double boundary, BigInteger size) {
        if (boundary <= 1) {
            return BigInteger.ZERO;
        }
        if (boundary == Double.POSITIVE_INFINITY) {
            return size;
        }
        if (boundary <= 0x1.0p53) {
            return BigInteger.valueOf((long) boundary - 1).min(size);
        }
        // XPath promotes integer positions to double for this comparison. Above 2^53,
        // rounding a position can change which side of the boundary it falls on.
        BigInteger low = BigInteger.ZERO;
        BigInteger high = size;
        while (low.compareTo(high) < 0) {
            BigInteger middle = low.add(high).shiftRight(1);
            if (middle.add(BigInteger.ONE).doubleValue() < boundary) {
                low = middle.add(BigInteger.ONE);
            } else {
                high = middle;
            }
        }
        return low;
    }

    record Slice(BigInteger offset, BigInteger length) {
        BigInteger end() {
            return offset.add(length);
        }
    }
}
