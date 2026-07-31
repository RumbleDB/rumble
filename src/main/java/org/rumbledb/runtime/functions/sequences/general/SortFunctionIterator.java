package org.rumbledb.runtime.functions.sequences.general;


import org.rumbledb.runtime.plan.AbstractItemRuntimePlan;
import org.rumbledb.runtime.plan.LocalRuntimePlan;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.NamedFunctions;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.CannotAtomizeException;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.items.FunctionItem;
import org.rumbledb.runtime.ConstantRuntimeIterator;
import org.rumbledb.runtime.cursor.IteratorLocalCursor;
import org.rumbledb.runtime.cursor.Cursor;
import org.rumbledb.runtime.functions.arrays.ArrayFunctionCallIterator;
import org.rumbledb.runtime.functions.maps.MapFunctionCallIterator;
import org.rumbledb.runtime.misc.SortKeyComparison;
import org.rumbledb.runtime.plan.RuntimePlan;
import org.rumbledb.types.SequenceType;

import java.io.Serial;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * XPath and XQuery Functions and Operators 3.1 {@code fn:sort}:
 * {@code fn:sort($input)}, {@code fn:sort($input, $collation?)},
 * {@code fn:sort($input, $collation?, $key)}.
 */
public class SortFunctionIterator extends AbstractItemRuntimePlan
        implements
            LocalRuntimePlan<Item> {

    @Override
    public Cursor<Item> createNativeCursor(DynamicContext context) {
        return new IteratorLocalCursor<>(() -> computeResult(context).iterator(), getMetadata());
    }

    @Serial
    private static final long serialVersionUID = 1L;

    private final RuntimePlan<Item> inputIterator;
    private final RuntimePlan<Item> collationIterator;
    private final RuntimePlan<Item> keyIterator;

    public SortFunctionIterator(
            List<RuntimePlan<Item>> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
        int n = arguments.size();
        if (n < 1 || n > 3) {
            throw new OurBadException("fn:sort expects 1, 2, or 3 arguments.");
        }
        this.inputIterator = arguments.get(0);
        this.collationIterator = n >= 2 ? arguments.get(1) : null;
        this.keyIterator = n == 3 ? arguments.get(2) : null;
    }

    private List<Item> computeResult(DynamicContext context) {
        List<Item> inputItems = this.inputIterator.materialize(context);
        String collationUri = resolveCollationUri(context);
        SortKeyComparison.checkCollationSupported(collationUri, getMetadata());

        KeyComputer keyComputer = buildKeyComputer(context);
        List<SortRow> rows = new ArrayList<>(inputItems.size());
        for (Item item : inputItems) {
            List<Item> keys = keyComputer.computeKeys(item, context);
            rows.add(new SortRow(item, keys));
        }

        RuntimeStaticContext sortStaticContext = localStaticContext();
        Comparator<SortRow> comparator = (left, right) -> {
            if (SortKeyComparison.sortKeysDeepEqual(left.keys, right.keys, collationUri, sortStaticContext)) {
                return 0;
            }
            boolean less = SortKeyComparison.sortKeysDeepLessThan(
                left.keys,
                right.keys,
                collationUri,
                sortStaticContext
            );
            return less ? -1 : 1;
        };
        rows.sort(comparator);

        List<Item> sortedItems = new ArrayList<>(rows.size());
        for (SortRow row : rows) {
            sortedItems.add(row.item);
        }
        return sortedItems;
    }

    private String resolveCollationUri(DynamicContext context) {
        if (this.collationIterator == null) {
            return getRuntimeStaticContext().getDefaultCollation();
        }
        List<Item> collation = this.collationIterator.materialize(context);
        if (collation.isEmpty()) {
            return getRuntimeStaticContext().getDefaultCollation();
        }
        if (collation.size() != 1 || !collation.get(0).isString()) {
            throw new UnexpectedTypeException(
                    "Type error; second argument to fn:sort must be empty sequence or a single xs:string.",
                    getMetadata()
            );
        }
        return collation.get(0).getStringValue();
    }

    private KeyComputer buildKeyComputer(DynamicContext context) {
        if (this.keyIterator == null) {
            return (item, ctx) -> fnDataKeySequence(item);
        }
        List<Item> keySpec = this.keyIterator.materialize(context);
        if (keySpec.isEmpty()) {
            throw new UnexpectedTypeException(
                    "Type error; third argument to fn:sort must be exactly one item.",
                    getMetadata()
            );
        }
        if (keySpec.size() != 1) {
            throw new UnexpectedTypeException(
                    "Type error; third argument to fn:sort must be exactly one item.",
                    getMetadata()
            );
        }

        Item spec = keySpec.get(0);
        if (spec.isFunction()) {
            FunctionItem fn = (FunctionItem) spec;
            return (item, ctx) -> invokeKeyFunction(fn, item, ctx);
        }
        if (spec.isArray()) {
            Item keyArray = spec;
            return (item, ctx) -> keyFromArrayLookup(keyArray, item, ctx);
        }
        if (spec.isObject()) {
            Item keyMap = spec;
            return (item, ctx) -> keyFromMapLookup(keyMap, item, ctx);
        }
        throw new UnexpectedTypeException(
                "Type error; third argument to fn:sort must be a function item, map, or array.",
                getMetadata()
        );
    }

    private List<Item> fnDataKeySequence(Item item) {
        List<Item> out = new ArrayList<>();
        fnDataAppend(item, out);
        return out;
    }

    private void fnDataAppend(Item item, List<Item> out) {
        if (item.isArray()) {
            int n = item.getSize();
            for (int i = 0; i < n; i++) {
                List<Item> member = item.getSequenceAt(i);
                for (Item subItem : member) {
                    fnDataAppend(subItem, out);
                }
            }
            return;
        }
        if (item.isObject() || item.isFunction()) {
            throw new CannotAtomizeException("The sequence cannot be atomized.", getMetadata());
        }
        out.addAll(item.atomizedValue());
    }

    private List<Item> invokeKeyFunction(
            FunctionItem functionItem,
            Item item,
            DynamicContext context
    ) {
        List<RuntimePlan<Item>> arguments = new ArrayList<>(1);
        arguments.add(new ConstantRuntimeIterator(item, localStaticContext()));
        RuntimePlan<Item> call = NamedFunctions
            .buildFunctionItemCallIterator(
                functionItem,
                this.staticContext,
                ExecutionMode.LOCAL,
                arguments,
                false
            );
        return materializeKeyIterator(call, context);
    }

    private List<Item> keyFromArrayLookup(Item keyArray, Item item, DynamicContext context) {
        if (!item.isNumeric()) {
            throw new UnexpectedTypeException(
                    "Type error; when the key is an array, each input item must be a single numeric index.",
                    getMetadata()
            );
        }
        RuntimePlan<Item> indexIterator = new ConstantRuntimeIterator(
                item,
                localStaticContext()
        );
        ArrayFunctionCallIterator lookup = new ArrayFunctionCallIterator(
                keyArray,
                indexIterator,
                localStaticContext()
        );
        return materializeKeyIterator(lookup, context);
    }

    private List<Item> keyFromMapLookup(Item mapItem, Item item, DynamicContext context) {
        List<Item> atomized = fnDataKeySequence(item);
        if (atomized.size() != 1) {
            throw new UnexpectedTypeException(
                    "Type error; map key function expects each input item to atomize to a single atomic value.",
                    getMetadata()
            );
        }
        RuntimePlan<Item> keyIterator = new ConstantRuntimeIterator(
                atomized.get(0),
                localStaticContext()
        );
        MapFunctionCallIterator lookup = new MapFunctionCallIterator(
                mapItem,
                keyIterator,
                localStaticContext()
        );
        return materializeKeyIterator(lookup, context);
    }

    private List<Item> materializeIterator(
            RuntimePlan<Item> iterator,
            DynamicContext context
    ) {
        return iterator.materialize(context);
    }

    private List<Item> materializeKeyIterator(
            RuntimePlan<Item> iterator,
            DynamicContext context
    ) {
        List<Item> rawItems = materializeIterator(iterator, context);
        List<Item> atomizedKeys = new ArrayList<>();
        for (Item rawItem : rawItems) {
            fnDataAppend(rawItem, atomizedKeys);
        }
        return atomizedKeys;
    }

    private RuntimeStaticContext localStaticContext() {
        return getRuntimeStaticContext()
            .toBuilder()
            .staticType(SequenceType.createSequenceType("item*"))
            .executionMode(ExecutionMode.LOCAL)
            .metadata(getMetadata())
            .build();
    }

    @FunctionalInterface
    private interface KeyComputer {
        List<Item> computeKeys(Item item, DynamicContext context);
    }

    private static class SortRow {
        private final Item item;
        private final List<Item> keys;

        private SortRow(Item item, List<Item> keys) {
            this.item = item;
            this.keys = keys;
        }
    }
}
