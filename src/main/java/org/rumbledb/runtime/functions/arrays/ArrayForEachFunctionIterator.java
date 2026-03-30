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
import org.rumbledb.items.FunctionItem;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.items.structured.JSoundDataFrame;
import org.rumbledb.runtime.HybridRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.expressions.ExecutionMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * XPath and XQuery Functions and Operators 3.1 {@code array:for-each}:
 * {@code array:for-each($array as array(*), $action as function(item()*) as item()*) as array(*)}.
 */
public class ArrayForEachFunctionIterator extends HybridRuntimeIterator {

    private static final long serialVersionUID = 1L;

    private final RuntimeIterator arrayIterator;
    private final RuntimeIterator functionIterator;
    private Item resultItem;
    private boolean hasProducedResult;

    public ArrayForEachFunctionIterator(
            List<RuntimeIterator> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
        if (arguments.size() != 2) {
            throw new OurBadException("array:for-each must have exactly two arguments.");
        }
        this.arrayIterator = arguments.get(0);
        this.functionIterator = arguments.get(1);
        this.resultItem = null;
        this.hasProducedResult = false;
    }

    @Override
    protected void openLocal() {
        // Do not open child iterators here: materializeExactlyOneItem / materialize open and close them.
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
                    "array:for-each expects exactly one array argument.",
                    getMetadata()
            );
        }

        if (!arrayItem.isArray()) {
            throw new UnexpectedTypeException(
                    "Type error; argument to array:for-each must be an array.",
                    getMetadata()
            );
        }

        List<List<Item>> memberSequences = arrayItem.getSequenceMembers();

        List<Item> actionItems = this.functionIterator.materialize(context);
        if (actionItems.isEmpty()) {
            throw new UnexpectedTypeException(
                    "Type error; second argument to array:for-each must be a function item.",
                    getMetadata()
            );
        }
        if (actionItems.size() != 1) {
            throw new UnexpectedTypeException(
                    "Type error; second argument to array:for-each must be exactly one function item.",
                    getMetadata()
            );
        }

        Item action = actionItems.get(0);
        if (!action.isFunction()) {
            throw new UnexpectedTypeException(
                    "Type error; second argument to array:for-each must be a function item "
                        + "(function(item()*) as item()*).",
                    getMetadata()
            );
        }
        FunctionItem functionItem = (FunctionItem) action;

        boolean allSingleton = true;
        List<List<Item>> resultMemberSequences = new ArrayList<>(memberSequences.size());
        for (List<Item> memberSequence : memberSequences) {
            List<Item> result = applyAction(functionItem, memberSequence, context);
            if (allSingleton && result.size() != 1) {
                allSingleton = false;
            }
            resultMemberSequences.add(result);
        }

        if (allSingleton) {
            List<Item> items = new ArrayList<>(memberSequences.size());
            for (List<Item> member : resultMemberSequences) {
                items.add(member.get(0));
            }
            this.resultItem = ItemFactory.getInstance().createArrayItem(items, false);
        } else {
            this.resultItem = ItemFactory.getInstance().createSequenceArrayItem(resultMemberSequences, false);
        }
    }

    private RuntimeIterator createSequenceIterator(List<Item> items) {
        if (items.isEmpty()) {
            RuntimeStaticContext staticContext = new RuntimeStaticContext(
                    getConfiguration(),
                    org.rumbledb.types.SequenceType.createSequenceType("item*"),
                    ExecutionMode.LOCAL,
                    getMetadata()
            );
            return new org.rumbledb.runtime.CommaExpressionIterator(
                    Collections.emptyList(),
                    staticContext
            );
        }

        List<RuntimeIterator> childIterators = new ArrayList<>(items.size());
        for (Item item : items) {
            RuntimeStaticContext childStaticContext = new RuntimeStaticContext(
                    getConfiguration(),
                    org.rumbledb.types.SequenceType.createSequenceType("item*"),
                    ExecutionMode.LOCAL,
                    getMetadata()
            );
            childIterators.add(
                new org.rumbledb.runtime.ConstantRuntimeIterator(
                        item,
                        childStaticContext
                )
            );
        }

        RuntimeStaticContext staticContext = new RuntimeStaticContext(
                getConfiguration(),
                org.rumbledb.types.SequenceType.createSequenceType("item*"),
                ExecutionMode.LOCAL,
                getMetadata()
        );
        return new org.rumbledb.runtime.CommaExpressionIterator(childIterators, staticContext);
    }

    /**
     * Invokes {@code $action} with the array member as {@code item()*} (one argument, sequence type).
     */
    private List<Item> applyAction(
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
        return functionCall.materialize(context);
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
        this.functionIterator.reset(this.currentDynamicContextForLocalExecution);
        initializeResult(this.currentDynamicContextForLocalExecution);
        this.hasNext = this.resultItem != null;
        this.hasProducedResult = false;
    }

    @Override
    protected void closeLocal() {
        if (this.arrayIterator.isOpen()) {
            this.arrayIterator.close();
        }
        if (this.functionIterator.isOpen()) {
            this.functionIterator.close();
        }
        this.resultItem = null;
        this.hasProducedResult = false;
    }

    @Override
    public JavaRDD<Item> getRDDAux(DynamicContext dynamicContext) {
        throw new OurBadException(
                "array:for-each is currently supported only in local execution mode."
        );
    }

    @Override
    protected boolean implementsDataFrames() {
        return false;
    }

    @Override
    public JSoundDataFrame getDataFrame(DynamicContext dynamicContext) {
        throw new OurBadException(
                "array:for-each is currently supported only in local execution mode."
        );
    }
}
