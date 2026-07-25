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

package org.rumbledb.runtime.cursor;

import lombok.NonNull;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.InvalidArgumentTypeException;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.UnsupportedCollationException;
import org.rumbledb.items.ItemComparator;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.plan.RuntimePlan;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.ItemType;

/**
 * Local cursor shared by the {@code min()} and {@code max()} aggregate plans.
 */
public final class ExtremumLocalCursor extends AtMostOneLocalCursor<Item> {

    private enum Kind {
        MIN,
        MAX
    }

    private static final String CODEPOINT_COLLATION =
        "http://www.w3.org/2005/xpath-functions/collation/codepoint";

    private final RuntimePlan<Item> childPlan;
    private final RuntimePlan<Item> collationPlan;
    private final DynamicContext context;
    private final ExceptionMetadata metadata;
    private final Kind kind;

    private ExtremumLocalCursor(
            @NonNull RuntimePlan<Item> childPlan,
            RuntimePlan<Item> collationPlan,
            @NonNull DynamicContext context,
            @NonNull ExceptionMetadata metadata,
            @NonNull Kind kind
    ) {
        this.childPlan = childPlan;
        this.collationPlan = collationPlan;
        this.context = context;
        this.metadata = metadata;
        this.kind = kind;
    }

    public static ExtremumLocalCursor min(
            RuntimePlan<Item> childPlan,
            RuntimePlan<Item> collationPlan,
            DynamicContext context,
            ExceptionMetadata metadata
    ) {
        return new ExtremumLocalCursor(childPlan, collationPlan, context, metadata, Kind.MIN);
    }

    public static ExtremumLocalCursor max(
            RuntimePlan<Item> childPlan,
            RuntimePlan<Item> collationPlan,
            DynamicContext context,
            ExceptionMetadata metadata
    ) {
        return new ExtremumLocalCursor(childPlan, collationPlan, context, metadata, Kind.MAX);
    }

    @Override
    protected Item materializeFirstItemOrNull() {
        validateCollation();

        Item selected = null;
        boolean sawNull = false;
        boolean sawFloat = false;
        boolean sawDouble = false;
        boolean sawString = false;
        ItemComparator comparator = new ItemComparator(
                this.kind == Kind.MIN,
                new InvalidArgumentTypeException(
                        functionName() + " expression input error. Input has to be non-null atomics of matching types",
                        this.metadata
                )
        );

        try (LocalCursor<Item> childCursor = this.childPlan.createLocalCursor(this.context)) {
            childCursor.open();
            while (childCursor.hasNext()) {
                Item candidate = childCursor.next();
                if (candidate.isNull()) {
                    if (this.kind == Kind.MIN) {
                        return ItemFactory.getInstance().createNullItem();
                    }
                    sawNull = true;
                    continue;
                }
                if (candidate.isUntypedAtomic()) {
                    candidate = ItemFactory.getInstance().createDoubleItem(candidate.castToDoubleValue());
                }
                ensureSupported(candidate);
                sawDouble |= candidate.isDouble();
                sawFloat |= candidate.isFloat();
                sawString |= candidate.isString();

                if (selected == null) {
                    selected = candidate;
                    continue;
                }

                int comparison = comparator.compare(selected, candidate);
                if (isNaN(candidate)) {
                    selected = candidate;
                } else if (!isNaN(selected) && shouldSelectCandidate(comparison)) {
                    selected = candidate;
                }
            }
        }

        if (selected == null) {
            return sawNull ? ItemFactory.getInstance().createNullItem() : null;
        }
        if (selected.isNumeric()) {
            if (sawDouble) {
                return ItemFactory.getInstance().createDoubleItem(selected.castToDoubleValue());
            }
            if (sawFloat) {
                return ItemFactory.getInstance().createFloatItem(selected.castToFloatValue());
            }
        }
        if (sawString && (selected.isString() || selected.isAnyURI())) {
            return ItemFactory.getInstance().createStringItem(selected.getStringValue());
        }
        return selected;
    }

    private boolean shouldSelectCandidate(int comparison) {
        return this.kind == Kind.MIN ? comparison > 0 : comparison < 0;
    }

    private static boolean isNaN(Item item) {
        return (item.isFloat() || item.isDouble()) && item.isNaN();
    }

    private void validateCollation() {
        if (this.collationPlan == null) {
            return;
        }
        Item collation = LocalCursorUtils.materializeFirst(this.collationPlan, this.context);
        if (!CODEPOINT_COLLATION.equals(collation.getStringValue())) {
            throw new UnsupportedCollationException("Wrong collation parameter", this.metadata);
        }
    }

    private static void ensureSupported(Item item) {
        ItemType type = item.getDynamicType();
        if (
            item.isNumeric()
                || item.isString()
                || item.isAnyURI()
                || item.isBoolean()
                || type.equals(BuiltinTypesCatalogue.dateItem)
                || type.isSubtypeOf(BuiltinTypesCatalogue.dateTimeItem)
                || type.equals(BuiltinTypesCatalogue.dayTimeDurationItem)
                || type.equals(BuiltinTypesCatalogue.yearMonthDurationItem)
                || type.equals(BuiltinTypesCatalogue.timeItem)
        ) {
            return;
        }
        throw new OurBadException("Inconsistent state in state iteration");
    }

    private String functionName() {
        return this.kind == Kind.MIN ? "Min" : "Max";
    }
}
