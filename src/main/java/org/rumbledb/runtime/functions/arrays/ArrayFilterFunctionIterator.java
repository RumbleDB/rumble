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

package org.rumbledb.runtime.functions.arrays;

import org.apache.spark.api.java.JavaRDD;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.NamedFunctions;
import org.rumbledb.context.RuntimeStaticContext;
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
import org.rumbledb.types.SequenceType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * XPath and XQuery Functions and Operators 3.1 {@code array:filter}:
 * {@code array:filter($array as array(*), $predicate as function(item()*) as xs:boolean) as array(*)}.
 * Map-as-predicate is not supported yet ({@code isObject()} predicates are rejected at runtime).
 */
public class ArrayFilterFunctionIterator extends HybridRuntimeIterator {

    private static final long serialVersionUID = 1L;

    private final RuntimeIterator arrayIterator;
    private final RuntimeIterator predicateIterator;
    private Item resultItem;
    private boolean hasProducedResult;

    public ArrayFilterFunctionIterator(
            List<RuntimeIterator> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
        if (arguments.size() != 2) {
            throw new OurBadException("array:filter must have exactly two arguments.");
        }
        this.arrayIterator = arguments.get(0);
        this.predicateIterator = arguments.get(1);
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
                    "array:filter expects exactly one array argument.",
                    getMetadata()
            );
        }

        if (!arrayItem.isArray()) {
            throw new UnexpectedTypeException(
                    "Type error; argument to array:filter must be an array.",
                    getMetadata()
            );
        }

        List<List<Item>> memberSequences = arrayItem.getSequenceMembers();

        List<Item> predicateItems = this.predicateIterator.materialize(context);
        if (predicateItems.isEmpty()) {
            throw new UnexpectedTypeException(
                    "Type error; second argument to array:filter must be exactly one item.",
                    getMetadata()
            );
        }
        if (predicateItems.size() != 1) {
            throw new UnexpectedTypeException(
                    "Type error; second argument to array:filter must be exactly one item.",
                    getMetadata()
            );
        }

        Item predicate = predicateItems.get(0);
        if (predicate.isObject()) {
            throw new UnexpectedTypeException(
                    "Type error; map-as-predicate for array:filter is not supported yet.",
                    getMetadata()
            );
        }
        boolean allSingleton = true;
        List<List<Item>> kept = new ArrayList<>();
        if (predicate.isFunction()) {
            FunctionItem functionItem = (FunctionItem) predicate;
            for (List<Item> memberSequence : memberSequences) {
                if (predicateHoldsForFunction(functionItem, memberSequence, context)) {
                    if (allSingleton && memberSequence.size() != 1) {
                        allSingleton = false;
                    }
                    kept.add(memberSequence);
                }
            }
        } else if (predicate.isArray()) {
            for (List<Item> memberSequence : memberSequences) {
                if (predicateHoldsForArray(predicate, memberSequence, context)) {
                    if (allSingleton && memberSequence.size() != 1) {
                        allSingleton = false;
                    }
                    kept.add(memberSequence);
                }
            }
        } else {
            throw new UnexpectedTypeException(
                    "Type error; second argument to array:filter must be a function item or an array.",
                    getMetadata()
            );
        }

        if (allSingleton) {
            List<Item> items = new ArrayList<>();
            for (List<Item> member : kept) {
                items.add(member.get(0));
            }
            this.resultItem = ItemFactory.getInstance().createArrayItem(items, false);
        } else {
            this.resultItem = ItemFactory.getInstance().createSequenceArrayItem(kept, false);
        }
    }

    private boolean predicateHoldsForFunction(
            FunctionItem functionItem,
            List<Item> memberSequence,
            DynamicContext context
    ) {
        RuntimeIterator memberIterator = createSequenceIterator(memberSequence);
        List<RuntimeIterator> arguments = new ArrayList<>(1);
        arguments.add(memberIterator);
        RuntimeIterator functionCall = NamedFunctions.buildUserDefinedFunctionCallIterator(
            functionItem,
            getConfiguration(),
            ExecutionMode.LOCAL,
            getMetadata(),
            arguments
        );
        List<Item> result = functionCall.materialize(context);
        return booleanValueFromFilterResult(result);
    }

    private boolean predicateHoldsForArray(
            Item predicateArray,
            List<Item> memberSequence,
            DynamicContext context
    ) {
        if (memberSequence.isEmpty()) {
            throw new UnexpectedTypeException(
                    "Type error; when the second argument is an array, each member of the first array must be "
                        + "exactly one numeric value usable as a 1-based index.",
                    getMetadata()
            );
        }
        if (memberSequence.size() != 1) {
            throw new UnexpectedTypeException(
                    "Type error; when the second argument is an array, each member of the first array must be "
                        + "exactly one numeric value usable as a 1-based index.",
                    getMetadata()
            );
        }
        Item indexItem = memberSequence.get(0);
        if (!indexItem.isNumeric()) {
            throw new UnexpectedTypeException(
                    "Type error; when the second argument is an array, each member of the first array must be "
                        + "exactly one numeric value usable as a 1-based index.",
                    getMetadata()
            );
        }

        RuntimeStaticContext indexStaticContext = new RuntimeStaticContext(
                getConfiguration(),
                SequenceType.createSequenceType("item*"),
                ExecutionMode.LOCAL,
                getMetadata()
        );
        RuntimeIterator indexIterator = new ConstantRuntimeIterator(indexItem, indexStaticContext);
        RuntimeStaticContext callStaticContext = new RuntimeStaticContext(
                getConfiguration(),
                SequenceType.createSequenceType("item*"),
                ExecutionMode.LOCAL,
                getMetadata()
        );
        ArrayFunctionCallIterator lookup = new ArrayFunctionCallIterator(
                predicateArray,
                indexIterator,
                callStaticContext
        );
        lookup.open(context);
        try {
            List<Item> lookedUp = new ArrayList<>();
            while (lookup.hasNext()) {
                lookedUp.add(lookup.next());
            }
            return booleanValueFromFilterResult(lookedUp);
        } finally {
            lookup.close();
        }
    }

    private boolean booleanValueFromFilterResult(List<Item> items) {
        if (items.size() != 1) {
            throw new UnexpectedTypeException(
                    "Type error; array:filter predicate must return exactly one xs:boolean value.",
                    getMetadata()
            );
        }
        Item value = items.get(0);
        if (!value.isBoolean()) {
            throw new UnexpectedTypeException(
                    "Type error; array:filter predicate must return exactly one xs:boolean value.",
                    getMetadata()
            );
        }
        return value.getBooleanValue();
    }

    private RuntimeIterator createSequenceIterator(List<Item> items) {
        if (items.isEmpty()) {
            RuntimeStaticContext staticContext = new RuntimeStaticContext(
                    getConfiguration(),
                    SequenceType.createSequenceType("item*"),
                    ExecutionMode.LOCAL,
                    getMetadata()
            );
            return new CommaExpressionIterator(
                    Collections.emptyList(),
                    staticContext
            );
        }

        List<RuntimeIterator> childIterators = new ArrayList<>(items.size());
        for (Item item : items) {
            RuntimeStaticContext childStaticContext = new RuntimeStaticContext(
                    getConfiguration(),
                    SequenceType.createSequenceType("item*"),
                    ExecutionMode.LOCAL,
                    getMetadata()
            );
            childIterators.add(
                new ConstantRuntimeIterator(
                        item,
                        childStaticContext
                )
            );
        }

        RuntimeStaticContext staticContext = new RuntimeStaticContext(
                getConfiguration(),
                SequenceType.createSequenceType("item*"),
                ExecutionMode.LOCAL,
                getMetadata()
        );
        return new CommaExpressionIterator(childIterators, staticContext);
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
        this.predicateIterator.reset(this.currentDynamicContextForLocalExecution);
        initializeResult(this.currentDynamicContextForLocalExecution);
        this.hasNext = this.resultItem != null;
        this.hasProducedResult = false;
    }

    @Override
    protected void closeLocal() {
        if (this.arrayIterator.isOpen()) {
            this.arrayIterator.close();
        }
        if (this.predicateIterator.isOpen()) {
            this.predicateIterator.close();
        }
        this.resultItem = null;
        this.hasProducedResult = false;
    }

    @Override
    public JavaRDD<Item> getRDDAux(DynamicContext dynamicContext) {
        throw new OurBadException(
                "array:filter is currently supported only in local execution mode."
        );
    }

    @Override
    protected boolean implementsDataFrames() {
        return false;
    }

    @Override
    public JSoundDataFrame getDataFrame(DynamicContext dynamicContext) {
        throw new OurBadException(
                "array:filter is currently supported only in local execution mode."
        );
    }
}
