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
package org.rumbledb.runtime.functions.arrays;

import java.math.BigInteger;

import org.rumbledb.api.Item;
import org.rumbledb.exceptions.ArrayIndexOutOfBoundsException;
import org.rumbledb.exceptions.ExceptionMetadata;

public final class ArrayIndexUtils {
    private ArrayIndexUtils() {}

    public static int getZeroBasedPosition(Item selector, int arraySize, ExceptionMetadata metadata) {
        BigInteger position = selector.isInteger()
                ? selector.castToIntegerValue()
                : BigInteger.valueOf(selector.castToIntValue());
        if (position.compareTo(BigInteger.ONE) < 0
                || position.compareTo(BigInteger.valueOf(arraySize)) > 0) {
            throw new ArrayIndexOutOfBoundsException(
                    "Tried to access array index: " + position + ", of array with length: " + arraySize,
                    metadata);
        }
        return position.intValue() - 1;
    }
}
