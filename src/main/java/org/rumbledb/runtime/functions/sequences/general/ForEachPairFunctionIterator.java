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
 *
 * Authors: Stefan Irimescu, Can Berker Cikis
 *
 */

package org.rumbledb.runtime.functions.sequences.general;

import org.apache.spark.api.java.JavaRDD;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.NamedFunctions;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.items.structured.JSoundDataFrame;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.HybridRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.types.SequenceType;

import java.util.ArrayList;
import java.util.List;

public class ForEachPairFunctionIterator extends HybridRuntimeIterator {
    private static final long serialVersionUID = 1L;

    private final RuntimeIterator sequenceIterator1;
    private final RuntimeIterator sequenceIterator2;
    private final RuntimeIterator actionIterator;
    private RuntimeIterator currentCallbackIterator;
    private List<Item> inputItems1;
    private List<Item> inputItems2;
    private Item actionFunction;
    private int pairIndex;
    private RuntimeStaticContext firstArgumentContext;
    private RuntimeStaticContext secondArgumentContext;
    private MutableArgumentIterator mutableFirstArgumentIterator;
    private MutableArgumentIterator mutableSecondArgumentIterator;

    public ForEachPairFunctionIterator(
            List<RuntimeIterator> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
        if (arguments.size() != 3) {
            throw new OurBadException("fn:for-each-pair must have exactly three arguments.");
        }
        this.sequenceIterator1 = arguments.get(0);
        this.sequenceIterator2 = arguments.get(1);
        this.actionIterator = arguments.get(2);
    }

    @Override
    protected void openLocal() {
        initializeState(this.currentDynamicContextForLocalExecution);
        advanceToNextResult(this.currentDynamicContextForLocalExecution);
    }

    private void initializeState(DynamicContext context) {
        this.inputItems1 = this.sequenceIterator1.materialize(context);
        this.inputItems2 = this.sequenceIterator2.materialize(context);

        List<Item> functionItems = this.actionIterator.materialize(context);
        if (functionItems.size() != 1 || !functionItems.get(0).isFunction()) {
            throw new UnexpectedTypeException(
                    "The third argument of fn:for-each-pair must be a single function item [err:XPTY0004].",
                    getMetadata()
            );
        }
        this.actionFunction = functionItems.get(0);
        if (this.actionFunction.getIdentifier().getArity() != 2) {
            throw new UnexpectedTypeException(
                    "The function passed to fn:for-each-pair must accept exactly two arguments [err:XPTY0004].",
                    getMetadata()
            );
        }

        this.firstArgumentContext = new RuntimeStaticContext(
                getConfiguration(),
                SequenceType.createSequenceType("item"),
                ExecutionMode.LOCAL,
                getMetadata()
        );
        this.secondArgumentContext = new RuntimeStaticContext(
                getConfiguration(),
                SequenceType.createSequenceType("item"),
                ExecutionMode.LOCAL,
                getMetadata()
        );
        this.pairIndex = 0;

        this.mutableFirstArgumentIterator = new MutableArgumentIterator(this.firstArgumentContext);
        this.mutableSecondArgumentIterator = new MutableArgumentIterator(this.secondArgumentContext);
        List<RuntimeIterator> callbackArguments = new ArrayList<>(2);
        callbackArguments.add(this.mutableFirstArgumentIterator);
        callbackArguments.add(this.mutableSecondArgumentIterator);
        this.currentCallbackIterator = NamedFunctions.buildFunctionItemCallIterator(
            this.actionFunction,
            this.staticContext,
            ExecutionMode.LOCAL,
            callbackArguments,
            false
        );
    }

    private void advanceToNextResult(DynamicContext context) {
        while (true) {
            if (this.currentCallbackIterator != null && this.currentCallbackIterator.hasNext()) {
                this.hasNext = true;
                return;
            }
            if (this.currentCallbackIterator != null && this.currentCallbackIterator.isOpen()) {
                this.currentCallbackIterator.close();
            }

            if (
                this.inputItems1 == null
                    || this.inputItems2 == null
                    || this.pairIndex >= this.inputItems1.size()
                    || this.pairIndex >= this.inputItems2.size()
            ) {
                this.hasNext = false;
                return;
            }

            this.mutableFirstArgumentIterator.setCurrentItem(this.inputItems1.get(this.pairIndex));
            this.mutableSecondArgumentIterator.setCurrentItem(this.inputItems2.get(this.pairIndex));
            this.pairIndex++;
            this.currentCallbackIterator.open(context);
        }
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
        Item result = this.currentCallbackIterator.next();
        advanceToNextResult(this.currentDynamicContextForLocalExecution);
        return result;
    }

    @Override
    protected void resetLocal() {
        if (this.currentCallbackIterator != null && this.currentCallbackIterator.isOpen()) {
            this.currentCallbackIterator.close();
        }
        this.sequenceIterator1.reset(this.currentDynamicContextForLocalExecution);
        this.sequenceIterator2.reset(this.currentDynamicContextForLocalExecution);
        this.actionIterator.reset(this.currentDynamicContextForLocalExecution);
        initializeState(this.currentDynamicContextForLocalExecution);
        advanceToNextResult(this.currentDynamicContextForLocalExecution);
    }

    @Override
    protected void closeLocal() {
        if (this.sequenceIterator1.isOpen()) {
            this.sequenceIterator1.close();
        }
        if (this.sequenceIterator2.isOpen()) {
            this.sequenceIterator2.close();
        }
        if (this.actionIterator.isOpen()) {
            this.actionIterator.close();
        }
        if (this.currentCallbackIterator != null && this.currentCallbackIterator.isOpen()) {
            this.currentCallbackIterator.close();
        }
        this.currentCallbackIterator = null;
        this.inputItems1 = null;
        this.inputItems2 = null;
        this.actionFunction = null;
        this.pairIndex = 0;
        this.firstArgumentContext = null;
        this.secondArgumentContext = null;
        this.mutableFirstArgumentIterator = null;
        this.mutableSecondArgumentIterator = null;
    }

    @Override
    protected boolean implementsDataFrames() {
        return false;
    }

    @Override
    public JavaRDD<Item> getRDDAux(DynamicContext context) {
        throw new OurBadException("fn:for-each-pair is currently supported only in local execution mode.");
    }

    @Override
    public JSoundDataFrame getDataFrame(DynamicContext dynamicContext) {
        throw new OurBadException("fn:for-each-pair is currently supported only in local execution mode.");
    }

    private static class MutableArgumentIterator extends AtMostOneItemLocalRuntimeIterator {
        private static final long serialVersionUID = 1L;
        private Item currentItem;

        MutableArgumentIterator(RuntimeStaticContext staticContext) {
            super(null, staticContext);
        }

        void setCurrentItem(Item item) {
            this.currentItem = item;
        }

        @Override
        public Item materializeFirstItemOrNull(DynamicContext context) {
            return this.currentItem;
        }
    }
}
