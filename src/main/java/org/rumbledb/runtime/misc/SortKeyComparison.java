/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information regarding
 * copyright ownership. The ASF licenses this file to You under
 * the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may
 * obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and
 * limitations under the License.
 */

package org.rumbledb.runtime.misc;

import org.rumbledb.api.Item;
import org.rumbledb.context.Name;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.exceptions.UnsupportedCollationException;
import org.rumbledb.expressions.comparison.ComparisonExpression.ComparisonOperator;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.functions.sequences.value.DeepEqualFunctionIterator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Lexicographic comparison of sort-key sequences for F&amp;O 3.1 {@code fn:sort} / {@code array:sort}
 * ({@code deep-less-than} and {@code fn:deep-equal} on key sequences with a collation).
 */
public final class SortKeyComparison {

    public static final String FOTS_CASEBLIND_COLLATION =
        "http://www.w3.org/2010/09/qt-fots-catalog/collation/caseblind";

    private SortKeyComparison() {
    }

    public static void checkCollationSupported(String collationUri, ExceptionMetadata metadata) {
        if (collationUri.equals(Name.DEFAULT_COLLATION_NS) || collationUri.equals(FOTS_CASEBLIND_COLLATION)) {
            return;
        }
        throw new UnsupportedCollationException("Wrong collation parameter", metadata);
    }

    public static boolean sortKeysDeepEqual(
            List<Item> a,
            List<Item> b,
            String collationUri,
            RuntimeStaticContext staticContext
    ) {
        if (a.size() != b.size()) {
            return false;
        }
        ExceptionMetadata metadata = staticContext.getMetadata();
        for (int i = 0; i < a.size(); i++) {
            if (!sortKeyItemDeepEqual(a.get(i), b.get(i), collationUri, staticContext, metadata)) {
                return false;
            }
        }
        return true;
    }

    public static boolean sortKeysDeepLessThan(
            List<Item> a,
            List<Item> b,
            String collationUri,
            RuntimeStaticContext staticContext
    ) {
        ExceptionMetadata metadata = staticContext.getMetadata();
        if (a.isEmpty()) {
            return !b.isEmpty();
        }
        if (b.isEmpty()) {
            return false;
        }
        Item headA = a.get(0);
        Item headB = b.get(0);
        if (sortKeyItemDeepEqual(headA, headB, collationUri, staticContext, metadata)) {
            return sortKeysDeepLessThan(tail(a), tail(b), collationUri, staticContext);
        }
        if (isNumericNaN(headA)) {
            return true;
        }
        if (isStringCollationType(headA) && isStringCollationType(headB)) {
            int cmp = compareStringCollationTypes(headA, headB, collationUri, metadata);
            return cmp < 0;
        }
        Item na = normalizeUntypedAtomic(headA);
        Item nb = normalizeUntypedAtomic(headB);
        long cmp = ComparisonIterator.compareItems(na, nb, ComparisonOperator.VC_LT, metadata);
        if (cmp == Long.MIN_VALUE) {
            throw new UnexpectedTypeException(
                    "Sort keys contain values that are not comparable.",
                    metadata
            );
        }
        return cmp < 0;
    }

    private static List<Item> tail(List<Item> seq) {
        if (seq.size() <= 1) {
            return Collections.emptyList();
        }
        return new ArrayList<>(seq.subList(1, seq.size()));
    }

    private static boolean sortKeyItemDeepEqual(
            Item a,
            Item b,
            String collationUri,
            RuntimeStaticContext staticContext,
            ExceptionMetadata metadata
    ) {
        if (isNumericNaN(a) && isNumericNaN(b)) {
            return true;
        }
        if (isStringCollationType(a) && isStringCollationType(b)) {
            return compareStringCollationTypes(a, b, collationUri, metadata) == 0;
        }
        DeepEqualFunctionIterator deepEqual = new DeepEqualFunctionIterator(Collections.emptyList(), staticContext);
        return deepEqual.checkDeepEqual(Collections.singletonList(a), Collections.singletonList(b));
    }

    private static int compareStringCollationTypes(Item a, Item b, String collationUri, ExceptionMetadata metadata) {
        checkCollationSupported(collationUri, metadata);
        String sa = normalizeUntypedAtomic(a).getStringValue();
        String sb = normalizeUntypedAtomic(b).getStringValue();
        if (collationUri.equals(Name.DEFAULT_COLLATION_NS)) {
            return sa.compareTo(sb);
        }
        return String.CASE_INSENSITIVE_ORDER.compare(sa, sb);
    }

    private static Item normalizeUntypedAtomic(Item item) {
        if (item.isUntypedAtomic()) {
            return ItemFactory.getInstance().createStringItem(item.getStringValue());
        }
        return item;
    }

    private static boolean isStringCollationType(Item item) {
        return item.isString() || item.isAnyURI() || item.isUntypedAtomic();
    }

    private static boolean isNumericNaN(Item item) {
        return (item.isDouble() && Double.isNaN(item.getDoubleValue()))
            || (item.isFloat() && Float.isNaN(item.getFloatValue()));
    }
}
