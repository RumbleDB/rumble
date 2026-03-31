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

package org.rumbledb.runtime.functions.arrays;

import org.apache.spark.api.java.JavaRDD;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.context.NamedFunctions;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.CannotAtomizeException;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.exceptions.MoreThanOneItemException;
import org.rumbledb.exceptions.NoItemException;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.items.FunctionItem;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.items.structured.JSoundDataFrame;
import org.rumbledb.runtime.CommaExpressionIterator;
import org.rumbledb.runtime.ConstantRuntimeIterator;
import org.rumbledb.runtime.HybridRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.misc.SortKeyComparison;
import org.rumbledb.types.SequenceType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * XPath and XQuery Functions and Operators 3.1 {@code array:sort}:
 * {@code array:sort($array)}, {@code array:sort($array, $collation?)},
 * {@code array:sort($array, $collation?, $key)}.
 */
public class ArraySortFunctionIterator extends HybridRuntimeIterator {

    private static final long serialVersionUID = 1L;

    private final RuntimeIterator arrayIterator;
    private final RuntimeIterator collationIterator;
    private final RuntimeIterator keyIterator;

    private Item resultItem;
    private boolean hasProducedResult;

    public ArraySortFunctionIterator(
            List<RuntimeIterator> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
        int n = arguments.size();
        if (n < 1 || n > 3) {
            throw new OurBadException("array:sort expects 1, 2, or 3 arguments.");
        }
        this.arrayIterator = arguments.get(0);
        this.collationIterator = n >= 2 ? arguments.get(1) : null;
        this.keyIterator = n == 3 ? arguments.get(2) : null;
        this.resultItem = null;
        this.hasProducedResult = false;
    }

    @Override
    protected void openLocal() {
        initializeResult(this.currentDynamicContextForLocalExecution);
        this.hasNext = this.resultItem != null;
        this.hasProducedResult = false;
    }

    private void initializeResult(DynamicContext context) {
        Item arrayItem;
        try {
            arrayItem = this.arrayIterator.materializeExactlyOneItem(context);
        } catch (NoItemException e) {
            this.resultItem = null;
            return;
        } catch (MoreThanOneItemException e) {
            throw new UnexpectedTypeException(
                    "array:sort expects exactly one array argument.",
                    getMetadata()
            );
        }

        if (!arrayItem.isArray()) {
            throw new UnexpectedTypeException(
                    "Type error; first argument to array:sort must be an array.",
                    getMetadata()
            );
        }

        String collationUri = resolveCollationUri(context);
        SortKeyComparison.checkCollationSupported(collationUri, getMetadata());

        List<List<Item>> memberSequences = arrayItem.getSequenceMembers();
        KeyComputer keyComputer = buildKeyComputer(context);

        List<SortRow> rows = new ArrayList<>(memberSequences.size());
        for (List<Item> member : memberSequences) {
            List<Item> keys = keyComputer.computeKeys(member, context);
            rows.add(new SortRow(new ArrayList<>(member), keys));
        }

        RuntimeStaticContext sortStaticContext = new RuntimeStaticContext(
                getConfiguration(),
                SequenceType.createSequenceType("item*"),
                ExecutionMode.LOCAL,
                getMetadata()
        );
        Comparator<SortRow> cmp = (r1, r2) -> {
            if (SortKeyComparison.sortKeysDeepEqual(r1.keys, r2.keys, collationUri, sortStaticContext)) {
                return 0;
            }
            boolean less = SortKeyComparison.sortKeysDeepLessThan(r1.keys, r2.keys, collationUri, sortStaticContext);
            return less ? -1 : 1;
        };
        rows.sort(cmp);

        boolean allSingleton = true;
        List<List<Item>> sortedMembers = new ArrayList<>(rows.size());
        for (SortRow row : rows) {
            if (allSingleton && row.member.size() != 1) {
                allSingleton = false;
            }
            sortedMembers.add(row.member);
        }
        if (allSingleton) {
            List<Item> items = new ArrayList<>(rows.size());
            for (List<Item> member : sortedMembers) {
                items.add(member.get(0));
            }
            this.resultItem = ItemFactory.getInstance().createArrayItem(items, false);
        } else {
            this.resultItem = ItemFactory.getInstance().createSequenceArrayItem(sortedMembers, false);
        }
    }

    private String resolveCollationUri(DynamicContext context) {
        if (this.collationIterator == null) {
            return Name.DEFAULT_COLLATION_NS;
        }
        List<Item> c = this.collationIterator.materialize(context);
        if (c.isEmpty()) {
            return Name.DEFAULT_COLLATION_NS;
        }
        if (c.size() != 1 || !c.get(0).isString()) {
            throw new UnexpectedTypeException(
                    "Type error; second argument to array:sort must be empty sequence or a single xs:string.",
                    getMetadata()
            );
        }
        return c.get(0).getStringValue();
    }

    private KeyComputer buildKeyComputer(DynamicContext context) {
        if (this.keyIterator == null) {
            return (member, ctx) -> fnDataKeySequence(member, ctx);
        }
        List<Item> keySpec = this.keyIterator.materialize(context);
        if (keySpec.isEmpty()) {
            throw new UnexpectedTypeException(
                    "Type error; third argument to array:sort must be exactly one item.",
                    getMetadata()
            );
        }
        if (keySpec.size() != 1) {
            throw new UnexpectedTypeException(
                    "Type error; third argument to array:sort must be exactly one item.",
                    getMetadata()
            );
        }
        Item spec = keySpec.get(0);
        if (spec.isFunction()) {
            FunctionItem fn = (FunctionItem) spec;
            return (member, ctx) -> invokeKeyFunction(fn, member, ctx);
        }
        if (spec.isArray()) {
            Item keyArray = spec;
            return (member, ctx) -> keyFromArrayLookup(keyArray, member, ctx);
        }
        if (spec.isObject()) {
            Item map = spec;
            return (member, ctx) -> keyFromMapLookup(map, member, ctx);
        }
        throw new UnexpectedTypeException(
                "Type error; third argument to array:sort must be a function item, map, or array.",
                getMetadata()
        );
    }

    /**
     * F&amp;O {@code fn:data} on a member sequence: arrays are flattened by member (recursive); maps and functions
     * error; nodes and atomics are atomized.
     */
    private List<Item> fnDataKeySequence(List<Item> member, DynamicContext context) {
        List<Item> out = new ArrayList<>();
        for (Item item : member) {
            fnDataAppend(item, out);
        }
        return out;
    }

    private void fnDataAppend(Item item, List<Item> out) {
        if (item.isArray()) {
            int n = item.getSize();
            for (int i = 0; i < n; i++) {
                List<Item> sub = item.getSequenceAt(i);
                for (Item subItem : sub) {
                    fnDataAppend(subItem, out);
                }
            }
            return;
        }
        if (item.isObject()) {
            throw new CannotAtomizeException("The sequence cannot be atomized.", getMetadata());
        }
        if (item.isFunction()) {
            throw new CannotAtomizeException("The sequence cannot be atomized.", getMetadata());
        }
        out.addAll(item.atomizedValue());
    }

    private List<Item> invokeKeyFunction(
            FunctionItem functionItem,
            List<Item> memberSequence,
            DynamicContext context
    ) {
        RuntimeIterator memberIterator = createSequenceIterator(memberSequence);
        List<RuntimeIterator> arguments = new ArrayList<>(1);
        arguments.add(memberIterator);
        RuntimeIterator call = NamedFunctions.buildFunctionItemCallIterator(
            functionItem,
            staticContext,
            ExecutionMode.LOCAL,
            arguments
        );
        return materializeIterator(call, context);
    }

    private List<Item> keyFromArrayLookup(Item keyArray, List<Item> memberSequence, DynamicContext context) {
        if (memberSequence.isEmpty()) {
            throw new UnexpectedTypeException(
                    "Type error; when the key is an array, each member must be exactly one numeric index.",
                    getMetadata()
            );
        }
        if (memberSequence.size() != 1) {
            throw new UnexpectedTypeException(
                    "Type error; when the key is an array, each member must be exactly one numeric index.",
                    getMetadata()
            );
        }
        Item indexItem = memberSequence.get(0);
        if (!indexItem.isNumeric()) {
            throw new UnexpectedTypeException(
                    "Type error; when the key is an array, each member must be exactly one numeric index.",
                    getMetadata()
            );
        }
        RuntimeIterator indexIterator = new ConstantRuntimeIterator(indexItem, localStaticContext());
        ArrayFunctionCallIterator lookup = new ArrayFunctionCallIterator(
                keyArray,
                indexIterator,
                localStaticContext()
        );
        return materializeIterator(lookup, context);
    }

    private List<Item> keyFromMapLookup(Item mapItem, List<Item> memberSequence, DynamicContext context) {
        List<Item> atomized = fnDataKeySequence(memberSequence, context);
        if (atomized.size() != 1) {
            throw new UnexpectedTypeException(
                    "Type error; map key function expects each member to atomize to a single atomic value.",
                    getMetadata()
            );
        }
        String key = atomized.get(0).getStringValue();
        Item value = mapItem.getItemByKey(key);
        if (value == null) {
            return Collections.emptyList();
        }
        return Collections.singletonList(value);
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

    private RuntimeStaticContext localStaticContext() {
        return new RuntimeStaticContext(
                getConfiguration(),
                SequenceType.createSequenceType("item*"),
                ExecutionMode.LOCAL,
                getMetadata()
        );
    }

    private RuntimeIterator createSequenceIterator(List<Item> items) {
        if (items.isEmpty()) {
            return new CommaExpressionIterator(
                    Collections.emptyList(),
                    localStaticContext()
            );
        }
        List<RuntimeIterator> childIterators = new ArrayList<>(items.size());
        for (Item item : items) {
            childIterators.add(new ConstantRuntimeIterator(item, localStaticContext()));
        }
        return new CommaExpressionIterator(childIterators, localStaticContext());
    }

    @Override
    protected boolean hasNextLocal() {
        return this.hasNext;
    }

    @Override
    protected Item nextLocal() {
        if (!this.hasNext || this.hasProducedResult) {
            throw new IteratorFlowException(RuntimeIterator.FLOW_EXCEPTION_MESSAGE, getMetadata());
        }
        this.hasProducedResult = true;
        this.hasNext = false;
        return this.resultItem;
    }

    @Override
    protected void resetLocal() {
        this.arrayIterator.reset(this.currentDynamicContextForLocalExecution);
        if (this.collationIterator != null) {
            this.collationIterator.reset(this.currentDynamicContextForLocalExecution);
        }
        if (this.keyIterator != null) {
            this.keyIterator.reset(this.currentDynamicContextForLocalExecution);
        }
        initializeResult(this.currentDynamicContextForLocalExecution);
        this.hasNext = this.resultItem != null;
        this.hasProducedResult = false;
    }

    @Override
    protected void closeLocal() {
        if (this.arrayIterator.isOpen()) {
            this.arrayIterator.close();
        }
        if (this.collationIterator != null && this.collationIterator.isOpen()) {
            this.collationIterator.close();
        }
        if (this.keyIterator != null && this.keyIterator.isOpen()) {
            this.keyIterator.close();
        }
        this.resultItem = null;
        this.hasProducedResult = false;
    }

    @Override
    public JavaRDD<Item> getRDDAux(DynamicContext dynamicContext) {
        throw new OurBadException("array:sort is currently supported only in local execution mode.");
    }

    @Override
    protected boolean implementsDataFrames() {
        return false;
    }

    @Override
    public JSoundDataFrame getDataFrame(DynamicContext dynamicContext) {
        throw new OurBadException("array:sort is currently supported only in local execution mode.");
    }

    @FunctionalInterface
    private interface KeyComputer {
        List<Item> computeKeys(List<Item> member, DynamicContext context);
    }

    private static final class SortRow {
        final List<Item> member;
        final List<Item> keys;

        SortRow(List<Item> member, List<Item> keys) {
            this.member = member;
            this.keys = keys;
        }
    }
}
