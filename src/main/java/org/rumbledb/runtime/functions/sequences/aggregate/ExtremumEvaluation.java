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
package org.rumbledb.runtime.functions.sequences.aggregate;

import java.io.Serializable;
import java.util.EnumSet;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.storage.StorageLevel;

import lombok.NonNull;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.InvalidArgumentTypeException;
import org.rumbledb.exceptions.UnsupportedCollationException;
import org.rumbledb.items.ItemComparator;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.cursor.Cursor;
import org.rumbledb.runtime.plan.ItemRuntimePlan;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.ItemType;

/**
 * Local and RDD evaluation shared by the {@code min()} and {@code max()} aggregate plans.
 */
final class ExtremumEvaluation {

    private enum Kind {
        MIN,
        MAX
    }

    private static final String CODEPOINT_COLLATION = "http://www.w3.org/2005/xpath-functions/collation/codepoint";

    private ExtremumEvaluation() {}

    public static Item minRDD(JavaRDD<Item> input, ExceptionMetadata metadata) {
        return evaluateRDD(input, metadata, Kind.MIN);
    }

    public static Item maxRDD(JavaRDD<Item> input, ExceptionMetadata metadata) {
        return evaluateRDD(input, metadata, Kind.MAX);
    }

    private static Item evaluateRDD(JavaRDD<Item> input, ExceptionMetadata metadata, Kind kind) {
        // Both passes must see the same converted input, including for non-deterministic source plans.
        JavaRDD<Item> converted =
                input.map(item -> normalizeCandidate(item, kind, metadata)).persist(StorageLevel.MEMORY_AND_DISK());
        try {
            Promotion promotion = converted.map(Promotion::of).fold(Promotion.EMPTY, Promotion::merge);
            if (!promotion.hasItems()) {
                return null;
            }
            // The result's type depends on every input, including values that are not the extremum.
            // Convert to the common type before comparing, rather than returning an original RDD item.
            JavaRDD<Item> promoted = converted.map(promotion::apply);
            ItemComparator comparator = new ItemComparator(kind == Kind.MIN, inputError(kind, metadata));
            return kind == Kind.MIN ? promoted.min(comparator) : promoted.max(comparator);
        } finally {
            converted.unpersist();
        }
    }

    public static Item min(
            ItemRuntimePlan childPlan,
            ItemRuntimePlan collationPlan,
            DynamicContext context,
            ExceptionMetadata metadata) {
        return evaluate(childPlan, collationPlan, context, metadata, Kind.MIN);
    }

    public static Item max(
            ItemRuntimePlan childPlan,
            ItemRuntimePlan collationPlan,
            DynamicContext context,
            ExceptionMetadata metadata) {
        return evaluate(childPlan, collationPlan, context, metadata, Kind.MAX);
    }

    private static Item evaluate(
            @NonNull ItemRuntimePlan childPlan,
            ItemRuntimePlan collationPlan,
            @NonNull DynamicContext context,
            @NonNull ExceptionMetadata metadata,
            @NonNull Kind kind) {
        validateCollation(collationPlan, context, metadata);

        Item selected = null;
        boolean sawNull = false;
        Promotion promotion = Promotion.EMPTY;
        ItemComparator comparator = new ItemComparator(kind == Kind.MIN, inputError(kind, metadata));

        try (Cursor<Item> childCursor = childPlan.getCursor(context)) {
            while (childCursor.hasNext()) {
                Item candidate = childCursor.next();
                if (candidate.isNull()) {
                    if (kind == Kind.MIN) {
                        return ItemFactory.getInstance().createNullItem();
                    }
                    sawNull = true;
                    continue;
                }
                candidate = normalizeCandidate(candidate, kind, metadata);
                promotion = promotion.merge(Promotion.of(candidate));

                if (selected == null) {
                    selected = candidate;
                    continue;
                }

                int comparison = comparator.compare(selected, candidate);
                if (isNaN(candidate)) {
                    selected = candidate;
                } else if (!isNaN(selected) && shouldSelectCandidate(comparison, kind)) {
                    selected = candidate;
                }
            }
        }

        if (selected == null) {
            return sawNull ? ItemFactory.getInstance().createNullItem() : null;
        }
        return promotion.apply(selected);
    }

    private static Item normalizeCandidate(Item item, Kind kind, ExceptionMetadata metadata) {
        if (item.isNull()) {
            return item;
        }
        if (item.isUntypedAtomic()) {
            item = ItemFactory.getInstance().createDoubleItem(item.castToDoubleValue());
        }
        ensureSupported(item, kind, metadata);
        return item;
    }

    /**
     * The primitive types seen in the input, which decide the common type of the result.
     */
    private static final class Promotion implements Serializable {
        private static final long serialVersionUID = 1L;

        private enum Seen {
            ANY_ITEM,
            DOUBLE,
            FLOAT,
            DECIMAL,
            STRING,
            ANY_URI
        }

        private static final Promotion EMPTY = new Promotion(EnumSet.noneOf(Seen.class));
        private final EnumSet<Seen> seen;

        // Spark's Kryo serializer needs an ordinary class rather than a Java record.
        private Promotion() {
            this(EnumSet.noneOf(Seen.class));
        }

        private Promotion(EnumSet<Seen> seen) {
            this.seen = seen;
        }

        static Promotion of(Item item) {
            EnumSet<Seen> seen = EnumSet.of(Seen.ANY_ITEM);
            if (item.isDouble()) {
                seen.add(Seen.DOUBLE);
            }
            if (item.isFloat()) {
                seen.add(Seen.FLOAT);
            }
            if (item.isDecimal()) {
                seen.add(Seen.DECIMAL);
            }
            if (item.isString()) {
                seen.add(Seen.STRING);
            }
            if (item.isAnyURI()) {
                seen.add(Seen.ANY_URI);
            }
            return new Promotion(seen);
        }

        Promotion merge(Promotion other) {
            EnumSet<Seen> merged = EnumSet.copyOf(this.seen);
            merged.addAll(other.seen);
            return new Promotion(merged);
        }

        boolean hasItems() {
            return this.seen.contains(Seen.ANY_ITEM);
        }

        Item apply(Item item) {
            // With one primitive type, retain the original item and its derived type.
            // Only mixtures of primitive types require conversion to their common type.
            if (item.isNumeric()) {
                if (saw(Seen.DOUBLE) && (saw(Seen.FLOAT) || saw(Seen.DECIMAL))) {
                    return ItemFactory.getInstance().createDoubleItem(item.castToDoubleValue());
                }
                if (saw(Seen.FLOAT) && saw(Seen.DECIMAL)) {
                    return ItemFactory.getInstance().createFloatItem(item.castToFloatValue());
                }
            }
            if (saw(Seen.STRING) && saw(Seen.ANY_URI) && (item.isString() || item.isAnyURI())) {
                return ItemFactory.getInstance().createStringItem(item.getStringValue());
            }
            return item;
        }

        private boolean saw(Seen kind) {
            return this.seen.contains(kind);
        }
    }

    private static InvalidArgumentTypeException inputError(Kind kind, ExceptionMetadata metadata) {
        return new InvalidArgumentTypeException(
                functionName(kind) + " expression input error. Input has to be non-null atomics of matching types",
                metadata);
    }

    private static boolean shouldSelectCandidate(int comparison, Kind kind) {
        return kind == Kind.MIN ? comparison > 0 : comparison < 0;
    }

    private static boolean isNaN(Item item) {
        return (item.isFloat() || item.isDouble()) && item.isNaN();
    }

    static void validateCollation(ItemRuntimePlan collationPlan, DynamicContext context, ExceptionMetadata metadata) {
        if (collationPlan == null) {
            return;
        }
        Item collation = collationPlan.materializeFirstOrNull(context);
        if (!CODEPOINT_COLLATION.equals(collation.getStringValue())) {
            throw new UnsupportedCollationException("Wrong collation parameter", metadata);
        }
    }

    private static void ensureSupported(Item item, Kind kind, ExceptionMetadata metadata) {
        ItemType type = item.getDynamicType();
        if (item.isNumeric()
                || item.isString()
                || item.isAnyURI()
                || item.isBoolean()
                || type.equals(BuiltinTypesCatalogue.dateItem)
                || type.isSubtypeOf(BuiltinTypesCatalogue.dateTimeItem)
                || type.equals(BuiltinTypesCatalogue.dayTimeDurationItem)
                || type.equals(BuiltinTypesCatalogue.yearMonthDurationItem)
                || type.equals(BuiltinTypesCatalogue.timeItem)
                || type.equals(BuiltinTypesCatalogue.hexBinaryItem)
                || type.equals(BuiltinTypesCatalogue.base64BinaryItem)) {
            return;
        }
        throw inputError(kind, metadata);
    }

    private static String functionName(Kind kind) {
        return kind == Kind.MIN ? "Min" : "Max";
    }
}
