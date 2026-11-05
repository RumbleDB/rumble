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

import java.util.ArrayList;
import java.util.List;

import org.rumbledb.api.Item;
import org.rumbledb.exceptions.ArrayIndexOutOfBoundsException;
import org.rumbledb.exceptions.CastException;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.runtime.typing.CastIterator;
import org.rumbledb.types.BuiltinTypesCatalogue;

public final class ArrayIndexUtils {
    private ArrayIndexUtils() {}

    /** Applies fn:data and array function conversion to lookup keys. */
    public static List<Item> atomizeLookupKeys(List<Item> keys, ExceptionMetadata metadata) {
        List<Item> result = new ArrayList<>();
        for (Item rawKey : keys) {
            for (Item atomized : rawKey.atomizedValue()) {
                Item key = atomized;
                if (key.isUntypedAtomic()) {
                    key = CastIterator.castItemToType(key, BuiltinTypesCatalogue.integerItem, metadata);
                    if (key == null) {
                        throw new CastException("Array lookup key cannot be cast to xs:integer.", metadata);
                    }
                }
                if (!key.isInteger()) {
                    throw new UnexpectedTypeException(
                            "Array lookup key must be an integer; got " + key.getDynamicType(), metadata);
                }
                result.add(key);
            }
        }
        return result;
    }

    public static int getZeroBasedPosition(Item selector, int arraySize, ExceptionMetadata metadata) {
        if (selector.isInteger()) {
            if (selector.isWithinIntRange()) {
                int position = selector.getIntValue();
                if (position >= 1 && position <= arraySize) {
                    return position - 1;
                }
            }
            throw new ArrayIndexOutOfBoundsException(
                    "Tried to access array index: "
                            + selector.getStringValue()
                            + ", of array with length: "
                            + arraySize,
                    metadata);
        }
        int position = selector.castToIntValue();
        if (position < 1 || position > arraySize) {
            throw new ArrayIndexOutOfBoundsException(
                    "Tried to access array index: " + position + ", of array with length: " + arraySize, metadata);
        }
        return position - 1;
    }
}
