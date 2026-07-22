package org.rumbledb.runtime.functions.sequences.general;

import org.apache.spark.api.java.JavaRDD;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.NamedFunctions;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.CannotAtomizeException;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.items.FunctionItem;
import org.rumbledb.items.structured.JSoundDataFrame;
import org.rumbledb.runtime.ConstantRuntimeIterator;
import org.rumbledb.runtime.HybridRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.functions.arrays.ArrayFunctionCallIterator;
import org.rumbledb.runtime.functions.maps.MapFunctionCallIterator;
import org.rumbledb.runtime.misc.SortKeyComparison;
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
public class SortFunctionIterator extends HybridRuntimeIterator {
    @Serial
    private static final long serialVersionUID = 1L;

    private final RuntimeIterator inputIterator;
    private final RuntimeIterator collationIterator;
    private final RuntimeIterator keyIterator;

    private List<Item> sortedItems;
    private int nextIndex;

    public SortFunctionIterator(
            List<RuntimeIterator> arguments,
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

    @Override
    protected void openLocal() {
        initializeResult(this.currentDynamicContextForLocalExecution);
        this.nextIndex = 0;
        this.hasNext = !this.sortedItems.isEmpty();
    }

    private void initializeResult(DynamicContext context) {
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

        this.sortedItems = new ArrayList<>(rows.size());
        for (SortRow row : rows) {
            this.sortedItems.add(row.item);
        }
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
        List<RuntimeIterator> arguments = new ArrayList<>(1);
        arguments.add(new ConstantRuntimeIterator(item, localStaticContext()));
        RuntimeIterator call = NamedFunctions.buildFunctionItemCallIterator(
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
        RuntimeIterator indexIterator = new ConstantRuntimeIterator(item, localStaticContext());
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
        RuntimeIterator keyIterator = new ConstantRuntimeIterator(atomized.get(0), localStaticContext());
        MapFunctionCallIterator lookup = new MapFunctionCallIterator(
                mapItem,
                keyIterator,
                localStaticContext()
        );
        return materializeKeyIterator(lookup, context);
    }

    private List<Item> materializeIterator(RuntimeIterator iterator, DynamicContext context) {
        iterator.open(context);
        try {
            List<Item> out = new ArrayList<>();
            while (iterator.hasNext()) {
                out.add(iterator.next());
            }
            return out;
        } finally {
            iterator.close();
        }
    }

    private List<Item> materializeKeyIterator(RuntimeIterator iterator, DynamicContext context) {
        List<Item> rawItems = materializeIterator(iterator, context);
        List<Item> atomizedKeys = new ArrayList<>();
        for (Item rawItem : rawItems) {
            fnDataAppend(rawItem, atomizedKeys);
        }
        return atomizedKeys;
    }

    private RuntimeStaticContext localStaticContext() {
        return getRuntimeStaticContext()
            .withStaticType(SequenceType.createSequenceType("item*"))
            .withExecutionMode(ExecutionMode.LOCAL)
            .withMetadata(getMetadata());
    }

    @Override
    protected boolean hasNextLocal() {
        return this.hasNext;
    }

    @Override
    protected Item nextLocal() {
        if (!this.hasNext) {
            throw new IteratorFlowException(RuntimeIterator.FLOW_EXCEPTION_MESSAGE, getMetadata());
        }
        Item result = this.sortedItems.get(this.nextIndex++);
        this.hasNext = this.nextIndex < this.sortedItems.size();
        return result;
    }

    @Override
    protected void resetLocal() {
        this.inputIterator.reset(this.currentDynamicContextForLocalExecution);
        if (this.collationIterator != null) {
            this.collationIterator.reset(this.currentDynamicContextForLocalExecution);
        }
        if (this.keyIterator != null) {
            this.keyIterator.reset(this.currentDynamicContextForLocalExecution);
        }
        initializeResult(this.currentDynamicContextForLocalExecution);
        this.nextIndex = 0;
        this.hasNext = !this.sortedItems.isEmpty();
    }

    @Override
    protected void closeLocal() {
        if (this.inputIterator.isOpen()) {
            this.inputIterator.close();
        }
        if (this.collationIterator != null && this.collationIterator.isOpen()) {
            this.collationIterator.close();
        }
        if (this.keyIterator != null && this.keyIterator.isOpen()) {
            this.keyIterator.close();
        }
        this.sortedItems = null;
        this.nextIndex = 0;
    }

    @Override
    protected boolean implementsDataFrames() {
        return false;
    }

    @Override
    public JavaRDD<Item> getRDDAux(DynamicContext context) {
        throw new OurBadException("fn:sort is currently supported only in local execution mode.");
    }

    @Override
    public JSoundDataFrame getDataFrame(DynamicContext dynamicContext) {
        throw new OurBadException("fn:sort is currently supported only in local execution mode.");
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
