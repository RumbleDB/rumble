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

import java.math.BigDecimal;
import java.math.BigInteger;

import org.rumbledb.api.Item;
import org.rumbledb.exceptions.OurBadException;

final class FormatNumberTypeResolver {
    private FormatNumberTypeResolver() {}

    // We want to use BigDecimal for an easier formatting experience, but Float or Double can be negative zero, so we
    // pass an additional flag that can be easily checked
    static ResolvedNumber getValue(Item item) {
        if (item.isInteger()) {
            BigInteger value = item.getIntegerValue();
            return ResolvedNumber.fromInteger(new BigDecimal(value), value.signum() < 0);
        }

        if (item.isDecimal()) {
            BigDecimal value = item.getDecimalValue();
            return ResolvedNumber.fromDecimal(value, value.compareTo(BigDecimal.ZERO) < 0);
        }

        if (item.isDouble()) {
            double value = item.getDoubleValue();
            boolean isNegative = value < 0.0d || Double.doubleToRawLongBits(value) == Double.doubleToRawLongBits(-0.0d);

            return ResolvedNumber.fromDouble(value, isNegative);
        }

        if (item.isFloat()) {
            float value = item.getFloatValue();
            boolean isNegative = value < 0.0f || Float.floatToRawIntBits(value) == Float.floatToRawIntBits(-0.0f);

            return ResolvedNumber.fromFloat(value, isNegative);
        }

        throw new OurBadException("Item appears to be not numeric, this should never happen");
    }
}
