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

package org.rumbledb.items;

import org.rumbledb.api.Item;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.comparison.ComparisonExpression.ComparisonOperator;
import org.rumbledb.runtime.misc.ComparisonIterator;

import java.math.BigDecimal;

/**
 * XQuery/XPath {@code op:same-key} for map keys (atomic values).
 *
 * @see <a href="https://www.w3.org/TR/xpath-functions-31/#func-same-key">fn:same-key</a>
 */
public final class MapAtomicSameKey {

    private MapAtomicSameKey() {
    }

    public static boolean sameKey(Item left, Item right) {
        return sameKey(left, right, ExceptionMetadata.EMPTY_METADATA);
    }

    public static boolean sameKey(Item left, Item right, ExceptionMetadata metadata) {
        if (left == null || right == null) {
            return false;
        }
        if (!left.isAtomic() || !right.isAtomic()) {
            return false;
        }
        long cmp = ComparisonIterator.compareItems(left, right, ComparisonOperator.VC_EQ, metadata);
        if (cmp == 0L) {
            return true;
        }
        if (cmp != Long.MIN_VALUE) {
            return false;
        }
        if (left.isNumeric() && right.isNumeric()) {
            try {
                BigDecimal a = left.castToDecimalValue();
                BigDecimal b = right.castToDecimalValue();
                return a.compareTo(b) == 0;
            } catch (Exception e) {
                try {
                    double a = left.castToDoubleValue();
                    double b = right.castToDoubleValue();
                    if (a == 0d && b == 0d) {
                        return true;
                    }
                    return Double.compare(a, b) == 0;
                } catch (Exception e2) {
                    return left.equals(right);
                }
            }
        }
        return left.equals(right);
    }
}
