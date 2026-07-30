/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.rumbledb.runtime.misc;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.rumbledb.api.Item;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.comparison.ComparisonExpression.ComparisonOperator;
import org.rumbledb.items.ItemFactory;

/**
 * Atom-only {@code fn:deep-equal} behavior shared by {@code op:same-key} (FO 3.1, section 17.1.1)
 * and sequence {@code fn:deep-equal} for non-node, non-array items.
 *
 * @see <a href="https://www.w3.org/TR/xpath-functions-31/#func-deep-equal">fn:deep-equal</a>
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AtomicDeepEqual {


    /**
     * Deep equality for individual items when neither side is an array or node
     * (NaN float/double pairs compare equal, as required by FO 3.1).
     */
    public static boolean deepEqual(Item item1, Item item2) {
        if (bothFloatOrDoubleNaN(item1, item2)) {
            return true;
        }
        if (item1.isUntypedAtomic()) {
            item1 = ItemFactory.getInstance().createStringItem(item1.getStringValue());
        }
        if (item2.isUntypedAtomic()) {
            item2 = ItemFactory.getInstance().createStringItem(item2.getStringValue());
        }
        long comparison = ComparisonIterator.compareItems(
            item1,
            item2,
            ComparisonOperator.VC_EQ,
            ExceptionMetadata.EMPTY_METADATA
        );
        // The low-level comparator reports non-comparable types with Long.MIN_VALUE.
        return comparison != Long.MIN_VALUE && comparison == 0;
    }

    private static boolean bothFloatOrDoubleNaN(Item item1, Item item2) {
        boolean n1 = (item1.isFloat() && Float.isNaN(item1.getFloatValue()))
            || (item1.isDouble() && Double.isNaN(item1.getDoubleValue()));
        boolean n2 = (item2.isFloat() && Float.isNaN(item2.getFloatValue()))
            || (item2.isDouble() && Double.isNaN(item2.getDoubleValue()));
        return n1 && n2;
    }
}
