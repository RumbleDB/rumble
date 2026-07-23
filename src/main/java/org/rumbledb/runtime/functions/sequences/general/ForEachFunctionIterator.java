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
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.items.structured.JSoundDataFrame;
import org.rumbledb.runtime.ConstantRuntimeIterator;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.HybridRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.functions.DynamicFunctionCallIterator;
import org.rumbledb.types.SequenceType;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

public class ForEachFunctionIterator extends HybridRuntimeIterator {
    @Serial
    private static final long serialVersionUID = 1L;

    private final RuntimeIterator sequenceIterator;
    private final RuntimeIterator actionIterator;
    private RuntimeIterator currentCallbackIterator;
    private List<Item> inputItems;
    private Item actionFunction;
    private int itemIndex;
    private RuntimeStaticContext argumentContext;
    private MutableArgumentIterator mutableArgumentIterator;

    public ForEachFunctionIterator(
            List<RuntimeIterator> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
        if (arguments.size() != 2) {
            throw new OurBadException("fn:for-each must have exactly two arguments.");
        }
        this.sequenceIterator = arguments.get(0);
        this.actionIterator = arguments.get(1);
    }

    @Override
    protected void openLocal() {
        initializeState(this.currentDynamicContextForLocalExecution);
        advanceToNextResult(this.currentDynamicContextForLocalExecution);
    }

    private void initializeState(DynamicContext context) {
        this.inputItems = this.sequenceIterator.materialize(context);

        List<Item> functionItems = this.actionIterator.materialize(context);
        if (functionItems.size() != 1) {
            throw new UnexpectedTypeException(
                    "The second argument of fn:for-each must be a single function item [err:XPTY0004].",
                    getMetadata()
            );
        }
        this.actionFunction = functionItems.get(0);
        if (!acceptsSingleArgument(this.actionFunction)) {
            throw new UnexpectedTypeException(
                    "The function passed to fn:for-each must accept exactly one argument [err:XPTY0004].",
                    getMetadata()
            );
        }

        this.argumentContext = RuntimeStaticContext.builder()
            .configuration(getConfiguration())
            .staticType(SequenceType.createSequenceType("item"))
            .executionMode(ExecutionMode.LOCAL)
            .metadata(getMetadata())
            .build();
        this.itemIndex = 0;
        this.mutableArgumentIterator = new MutableArgumentIterator(this.argumentContext);
        List<RuntimeIterator> callbackArguments = new ArrayList<>(1);
        callbackArguments.add(this.mutableArgumentIterator);
        RuntimeStaticContext functionItemContext = RuntimeStaticContext.builder()
            .configuration(getConfiguration())
            .staticType(SequenceType.createSequenceType("item*"))
            .executionMode(ExecutionMode.LOCAL)
            .metadata(getMetadata())
            .build();
        this.currentCallbackIterator = new DynamicFunctionCallIterator(
                new ConstantRuntimeIterator(this.actionFunction, functionItemContext),
                callbackArguments,
                functionItemContext
        );
    }

    private static boolean acceptsSingleArgument(Item item) {
        if (item.isMap() || item.isArray()) {
            return true;
        }
        return item.isFunction() && item.getIdentifier().getArity() == 1;
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

            if (this.inputItems == null || this.itemIndex >= this.inputItems.size()) {
                this.hasNext = false;
                return;
            }

            this.mutableArgumentIterator.setCurrentItem(this.inputItems.get(this.itemIndex++));
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
    protected void closeLocal() {
        if (this.sequenceIterator.isOpen()) {
            this.sequenceIterator.close();
        }
        if (this.actionIterator.isOpen()) {
            this.actionIterator.close();
        }
        if (this.currentCallbackIterator != null && this.currentCallbackIterator.isOpen()) {
            this.currentCallbackIterator.close();
        }
        this.currentCallbackIterator = null;
        this.inputItems = null;
        this.actionFunction = null;
        this.itemIndex = 0;
        this.argumentContext = null;
        this.mutableArgumentIterator = null;
    }

    @Override
    protected boolean implementsDataFrames() {
        return false;
    }

    @Override
    public JavaRDD<Item> getRDDAux(DynamicContext context) {
        throw new OurBadException("fn:for-each is currently supported only in local execution mode.");
    }

    @Override
    public JSoundDataFrame getDataFrame(DynamicContext dynamicContext) {
        throw new OurBadException("fn:for-each is currently supported only in local execution mode.");
    }

    private static class MutableArgumentIterator extends AtMostOneItemLocalRuntimeIterator {
        @Serial
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
