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
 * XPath and XQuery Functions and Operators 3.1 {@code array:for-each-pair}:
 * {@code array:for-each-pair($array1 as array(*), $array2 as array(*), $function as function(item()*, item()*) as item()*) as array(*)}.
 */
public class ArrayForEachPairFunctionIterator extends HybridRuntimeIterator {

    private static final long serialVersionUID = 1L;

    private final RuntimeIterator arrayIterator1;
    private final RuntimeIterator arrayIterator2;
    private final RuntimeIterator functionIterator;

    private Item resultItem;
    private boolean hasProducedResult;

    public ArrayForEachPairFunctionIterator(
            List<RuntimeIterator> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
        if (arguments.size() != 3) {
            throw new OurBadException("array:for-each-pair must have exactly three arguments.");
        }
        this.arrayIterator1 = arguments.get(0);
        this.arrayIterator2 = arguments.get(1);
        this.functionIterator = arguments.get(2);
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
        Item arrayItem1;
        try {
            arrayItem1 = this.arrayIterator1.materializeExactlyOneItem(context);
        } catch (NoItemException e) {
            this.resultItem = null;
            return;
        } catch (MoreThanOneItemException e) {
            throw new UnexpectedTypeException(
                    "array:for-each-pair expects exactly one array as the first argument.",
                    getMetadata()
            );
        }

        if (!arrayItem1.isArray()) {
            throw new UnexpectedTypeException(
                    "Type error; first argument to array:for-each-pair must be an array.",
                    getMetadata()
            );
        }

        Item arrayItem2;
        try {
            arrayItem2 = this.arrayIterator2.materializeExactlyOneItem(context);
        } catch (NoItemException e) {
            this.resultItem = null;
            return;
        } catch (MoreThanOneItemException e) {
            throw new UnexpectedTypeException(
                    "array:for-each-pair expects exactly one array as the second argument.",
                    getMetadata()
            );
        }

        if (!arrayItem2.isArray()) {
            throw new UnexpectedTypeException(
                    "Type error; second argument to array:for-each-pair must be an array.",
                    getMetadata()
            );
        }

        List<List<Item>> members1 = arrayItem1.getSequenceMembers();
        List<List<Item>> members2 = arrayItem2.getSequenceMembers();

        int n = Math.min(members1.size(), members2.size());

        List<Item> functionItems = this.functionIterator.materialize(context);
        if (functionItems.isEmpty()) {
            throw new UnexpectedTypeException(
                    "Type error; third argument to array:for-each-pair must be a function item.",
                    getMetadata()
            );
        }
        if (functionItems.size() != 1 || !functionItems.get(0).isFunction()) {
            throw new UnexpectedTypeException(
                    "Type error; third argument to array:for-each-pair must be a single function item.",
                    getMetadata()
            );
        }

        FunctionItem functionItem = (FunctionItem) functionItems.get(0);

        boolean allSingleton = true;
        List<List<Item>> resultMemberSequences = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            List<Item> result = applyFunction(functionItem, members1.get(i), members2.get(i), context);
            if (allSingleton && result.size() != 1) {
                allSingleton = false;
            }
            resultMemberSequences.add(result);
        }

        if (allSingleton) {
            List<Item> items = new ArrayList<>(n);
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
            List<Item> memberSequence1,
            List<Item> memberSequence2,
            DynamicContext context
    ) {
        RuntimeIterator firstArg = createSequenceIterator(memberSequence1);
        RuntimeIterator secondArg = createSequenceIterator(memberSequence2);

        List<RuntimeIterator> arguments = new ArrayList<>(2);
        arguments.add(firstArg);
        arguments.add(secondArg);

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
        this.arrayIterator1.reset(this.currentDynamicContextForLocalExecution);
        this.arrayIterator2.reset(this.currentDynamicContextForLocalExecution);
        this.functionIterator.reset(this.currentDynamicContextForLocalExecution);
        initializeResult(this.currentDynamicContextForLocalExecution);
        this.hasNext = this.resultItem != null;
        this.hasProducedResult = false;
    }

    @Override
    protected void closeLocal() {
        if (this.arrayIterator1.isOpen()) {
            this.arrayIterator1.close();
        }
        if (this.arrayIterator2.isOpen()) {
            this.arrayIterator2.close();
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
                "array:for-each-pair is currently supported only in local execution mode."
        );
    }

    @Override
    protected boolean implementsDataFrames() {
        return false;
    }

    @Override
    public JSoundDataFrame getDataFrame(DynamicContext dynamicContext) {
        throw new OurBadException(
                "array:for-each-pair is currently supported only in local execution mode."
        );
    }
}
