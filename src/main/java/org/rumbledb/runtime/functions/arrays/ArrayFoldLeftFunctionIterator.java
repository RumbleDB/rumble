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
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.context.NamedFunctions;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.exceptions.MoreThanOneItemException;
import org.rumbledb.exceptions.NoItemException;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.items.FunctionItem;
import org.rumbledb.items.structured.JSoundDataFrame;
import org.rumbledb.runtime.CommaExpressionIterator;
import org.rumbledb.runtime.ConstantRuntimeIterator;
import org.rumbledb.runtime.HybridRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.types.SequenceType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ArrayFoldLeftFunctionIterator extends HybridRuntimeIterator {

    private static final long serialVersionUID = 1L;

    private final RuntimeIterator arrayIterator;
    private final RuntimeIterator zeroIterator;
    private final RuntimeIterator functionIterator;

    private List<Item> resultSequence;
    private int resultIndex;

    public ArrayFoldLeftFunctionIterator(
            List<RuntimeIterator> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
        if (arguments.size() != 3) {
            throw new OurBadException("array:fold-left must have exactly three arguments.");
        }
        this.arrayIterator = arguments.get(0);
        this.zeroIterator = arguments.get(1);
        this.functionIterator = arguments.get(2);
        this.resultSequence = null;
        this.resultIndex = 0;
    }

    @Override
    protected void openLocal() {
        initializeResult(this.currentDynamicContextForLocalExecution);
        this.resultIndex = 0;
        this.hasNext = this.resultSequence != null && !this.resultSequence.isEmpty();
    }

    private void initializeResult(DynamicContext context) {
        Item arrayItem;
        try {
            arrayItem = this.arrayIterator.materializeExactlyOneItem(context);
        } catch (NoItemException e) {
            this.resultSequence = null;
            return;
        } catch (MoreThanOneItemException e) {
            throw new UnexpectedTypeException(
                    "array:fold-left expects exactly one array argument.",
                    getMetadata()
            );
        }

        if (!arrayItem.isArray()) {
            throw new UnexpectedTypeException(
                    "Type error; argument to array:fold-left must be an array.",
                    getMetadata()
            );
        }

        List<List<Item>> memberSequences;
        if (arrayItem.isJSONArray()) {
            int size = arrayItem.getSize();
            memberSequences = new ArrayList<>(size);
            for (int i = 0; i < size; i++) {
                Item member = arrayItem.getItemAt(i);
                if (member == null) {
                    memberSequences.add(Collections.emptyList());
                } else {
                    List<Item> singleton = new ArrayList<>(1);
                    singleton.add(member);
                    memberSequences.add(singleton);
                }
            }
        } else {
            memberSequences = arrayItem.getSequenceMembers();
        }

        List<Item> accumulator = this.zeroIterator.materialize(context);

        List<Item> functionItems = this.functionIterator.materialize(context);
        if (functionItems.isEmpty()) {
            throw new UnexpectedTypeException(
                    "Type error; third argument to array:fold-left must be a function item.",
                    getMetadata()
            );
        }
        if (functionItems.size() != 1 || !functionItems.get(0).isFunction()) {
            throw new UnexpectedTypeException(
                    "Type error; third argument to array:fold-left must be a single function item.",
                    getMetadata()
            );
        }

        FunctionItem functionItem = (FunctionItem) functionItems.get(0);

        for (List<Item> memberSequence : memberSequences) {
            accumulator = applyFunction(functionItem, accumulator, memberSequence, context);
        }

        this.resultSequence = accumulator;
    }

    private RuntimeIterator createSequenceIterator(List<Item> items) {
        if (items.isEmpty()) {
            RuntimeStaticContext staticContext = new RuntimeStaticContext(
                    getConfiguration(),
                    SequenceType.createSequenceType("item*"),
                    ExecutionMode.LOCAL,
                    getMetadata()
            );
            return new CommaExpressionIterator(Collections.emptyList(), staticContext);
        }

        List<RuntimeIterator> childIterators = new ArrayList<>(items.size());
        for (Item item : items) {
            RuntimeStaticContext childStaticContext = new RuntimeStaticContext(
                    getConfiguration(),
                    SequenceType.createSequenceType("item*"),
                    ExecutionMode.LOCAL,
                    getMetadata()
            );
            childIterators.add(new ConstantRuntimeIterator(item, childStaticContext));
        }

        RuntimeStaticContext staticContext = new RuntimeStaticContext(
                getConfiguration(),
                SequenceType.createSequenceType("item*"),
                ExecutionMode.LOCAL,
                getMetadata()
        );
        return new CommaExpressionIterator(childIterators, staticContext);
    }

    private List<Item> applyFunction(
            FunctionItem functionItem,
            List<Item> accumulator,
            List<Item> memberSequence,
            DynamicContext context
    ) {
        RuntimeIterator accIterator = createSequenceIterator(accumulator);
        RuntimeIterator memberIterator = createSequenceIterator(memberSequence);

        List<RuntimeIterator> arguments = new ArrayList<>(2);
        arguments.add(accIterator);
        arguments.add(memberIterator);

        RuntimeIterator functionCall = NamedFunctions.buildUserDefinedFunctionCallIterator(
            functionItem,
            getConfiguration(),
            ExecutionMode.LOCAL,
            getMetadata(),
            arguments
        );
        return functionCall.materialize(context);
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
        Item result = this.resultSequence.get(this.resultIndex++);
        if (this.resultIndex >= this.resultSequence.size()) {
            this.hasNext = false;
        }
        return result;
    }

    @Override
    protected void resetLocal() {
        initializeResult(this.currentDynamicContextForLocalExecution);
        this.resultIndex = 0;
        this.hasNext = this.resultSequence != null && !this.resultSequence.isEmpty();
    }

    @Override
    protected void closeLocal() {
        this.resultSequence = null;
        this.resultIndex = 0;
    }

    @Override
    public JavaRDD<Item> getRDDAux(DynamicContext dynamicContext) {
        throw new OurBadException(
                "array:fold-left is currently supported only in local execution mode."
        );
    }

    @Override
    protected boolean implementsDataFrames() {
        return false;
    }

    @Override
    public JSoundDataFrame getDataFrame(DynamicContext dynamicContext) {
        throw new OurBadException(
                "array:fold-left is currently supported only in local execution mode."
        );
    }
}

