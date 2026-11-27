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

import java.util.Iterator;
import java.util.List;

import org.rumbledb.api.Item;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.expressions.comparison.ComparisonExpression.ComparisonOperator;
import org.rumbledb.items.ItemFactory;

/**
 * Lexicographic comparison of sort-key sequences for F&amp;O 3.1 {@code fn:sort} / {@code array:sort}
 * ({@code deep-less-than} and {@code fn:deep-equal} on key sequences with a collation).
 */
public final class SortKeyComparison {

    private SortKeyComparison() {}

    public static int compare(List<Item> a, List<Item> b, String collationUri, ExceptionMetadata metadata) {
        Iterator<Item> left = a.iterator();
        Iterator<Item> right = b.iterator();
        while (left.hasNext() && right.hasNext()) {
            int comparison = compareItems(left.next(), right.next(), collationUri, metadata);
            if (comparison != 0) {
                return comparison;
            }
        }
        return Boolean.compare(left.hasNext(), right.hasNext());
    }

    private static int compareItems(Item a, Item b, String collationUri, ExceptionMetadata metadata) {
        if (a.isNumeric() && b.isNumeric()) {
            boolean aNaN = isNumericNaN(a);
            boolean bNaN = isNumericNaN(b);
            if (aNaN || bNaN) {
                return Boolean.compare(bNaN, aNaN);
            }
        }
        if (CollationSupport.isStringCollationType(a) && CollationSupport.isStringCollationType(b)) {
            return CollationSupport.compareStrings(a.getStringValue(), b.getStringValue(), collationUri, metadata);
        }
        a = normalizeUntypedAtomic(a);
        b = normalizeUntypedAtomic(b);
        if (AtomicDeepEqual.deepEqual(a, b, collationUri, metadata)) {
            return 0;
        }
        long cmp = ComparisonIterator.compareItems(a, b, ComparisonOperator.VC_LT, metadata);
        if (cmp == Long.MIN_VALUE) {
            throw new UnexpectedTypeException("Sort keys contain values that are not comparable.", metadata);
        }
        return Long.signum(cmp);
    }

    private static Item normalizeUntypedAtomic(Item item) {
        if (item.isUntypedAtomic()) {
            return ItemFactory.getInstance().createStringItem(item.getStringValue());
        }
        return item;
    }

    private static boolean isNumericNaN(Item item) {
        return (item.isDouble() && Double.isNaN(item.getDoubleValue()))
                || (item.isFloat() && Float.isNaN(item.getFloatValue()));
    }
}
