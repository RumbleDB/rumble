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
package org.rumbledb.runtime.misc;

import org.rumbledb.api.Item;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.RumbleException;
import org.rumbledb.expressions.comparison.ComparisonExpression.ComparisonOperator;
import org.rumbledb.items.ItemFactory;

/**
 * Atom-only {@code fn:deep-equal} behavior shared by {@code op:same-key} (FO 3.1, section 17.1.1)
 * and sequence {@code fn:deep-equal} for non-node, non-array items.
 *
 * @see <a href="https://www.w3.org/TR/xpath-functions-31/#func-deep-equal">fn:deep-equal</a>
 */
public final class AtomicDeepEqual {

    private AtomicDeepEqual() {}

    /**
     * Deep equality for individual items when neither side is an array or node
     * (NaN float/double pairs compare equal, as required by FO 3.1).
     */
    public static boolean deepEqual(Item item1, Item item2) {
        return deepEqual(item1, item2, null, ExceptionMetadata.EMPTY_METADATA);
    }

    /**
     * Deep equality for individual items with collation support for string-participating types.
     *
     * @param item1 the first item
     * @param item2 the second item
     * @param collation the collation URI, or null for default comparison
     * @param metadata exception metadata for error reporting
     * @return true if items are deep-equal, false otherwise
     */
    public static boolean deepEqual(Item item1, Item item2, String collation, ExceptionMetadata metadata) {
        if (!item1.isAtomic() || !item2.isAtomic()) {
            return false;
        }
        if (bothFloatOrDoubleNaN(item1, item2)) {
            return true;
        }
        if (collation != null
                && CollationSupport.isStringCollationType(item1)
                && CollationSupport.isStringCollationType(item2)) {
            return CollationSupport.compareStrings(item1.getStringValue(), item2.getStringValue(), collation, metadata)
                    == 0;
        }
        if (item1.isUntypedAtomic()) {
            item1 = ItemFactory.getInstance().createStringItem(item1.getStringValue());
        }
        if (item2.isUntypedAtomic()) {
            item2 = ItemFactory.getInstance().createStringItem(item2.getStringValue());
        }
        try {
            long comparison = ComparisonIterator.compareItems(
                    item1, item2, ComparisonOperator.VC_EQ, ExceptionMetadata.EMPTY_METADATA);
            // The low-level comparator reports non-comparable types with Long.MIN_VALUE.
            return comparison != Long.MIN_VALUE && comparison == 0;
        } catch (RumbleException e) {
            return false;
        }
    }

    private static boolean bothFloatOrDoubleNaN(Item item1, Item item2) {
        boolean n1 = (item1.isFloat() && Float.isNaN(item1.getFloatValue()))
                || (item1.isDouble() && Double.isNaN(item1.getDoubleValue()));
        boolean n2 = (item2.isFloat() && Float.isNaN(item2.getFloatValue()))
                || (item2.isDouble() && Double.isNaN(item2.getDoubleValue()));
        return n1 && n2;
    }
}
