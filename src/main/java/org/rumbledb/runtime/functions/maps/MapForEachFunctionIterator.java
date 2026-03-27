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

package org.rumbledb.runtime.functions.maps;

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
import org.rumbledb.runtime.ConstantRuntimeIterator;
import org.rumbledb.runtime.HybridRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.types.SequenceType;

import java.util.ArrayList;
import java.util.List;

/**
 * F&O 3.1 map:for-each($map as map(*), $action as function(xs:anyAtomicType, item()*) as item()*)
 * as item()*.
 */
public class MapForEachFunctionIterator extends HybridRuntimeIterator {

    private static final long serialVersionUID = 1L;

    private final RuntimeIterator mapIterator;
    private final RuntimeIterator actionIterator;
    private RuntimeIterator currentCallbackIterator;
    private List<Item> mapKeys;
    private Item mapItem;
    private Item actionFunction;
    private int keyIndex;
    private RuntimeStaticContext keyArgumentContext;
    private RuntimeStaticContext valueArgumentContext;

    public MapForEachFunctionIterator(
            List<RuntimeIterator> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
        if (arguments.size() != 2) {
            throw new OurBadException("map:for-each must have exactly two arguments.");
        }
        this.mapIterator = arguments.get(0);
        this.actionIterator = arguments.get(1);
        this.currentCallbackIterator = null;
        this.mapKeys = null;
        this.mapItem = null;
        this.actionFunction = null;
        this.keyIndex = 0;
    }

    @Override
    protected void openLocal() {
        initializeState(this.currentDynamicContextForLocalExecution);
        advanceToNextResult(this.currentDynamicContextForLocalExecution);
    }

    private void initializeState(DynamicContext context) {
        List<Item> mapArguments = this.mapIterator.materialize(context);
        if (mapArguments.size() != 1 || !mapArguments.get(0).isMap()) {
            throw new UnexpectedTypeException(
                    "The first argument of map:for-each must be a single map item [err:XPTY0004].",
                    getMetadata()
            );
        }
        this.mapItem = mapArguments.get(0);

        List<Item> functionArguments = this.actionIterator.materialize(context);
        if (functionArguments.size() != 1 || !functionArguments.get(0).isFunction()) {
            throw new UnexpectedTypeException(
                    "The second argument of map:for-each must be a single function item [err:XPTY0004].",
                    getMetadata()
            );
        }
        this.actionFunction = functionArguments.get(0);
        if (this.actionFunction.getIdentifier().getArity() != 2) {
            throw new UnexpectedTypeException(
                    "The function passed to map:for-each must accept exactly two arguments [err:XPTY0004].",
                    getMetadata()
            );
        }

        this.keyArgumentContext = new RuntimeStaticContext(
                getConfiguration(),
                SequenceType.createSequenceType("anyAtomicType"),
                ExecutionMode.LOCAL,
                getMetadata()
        );
        this.valueArgumentContext = new RuntimeStaticContext(
                getConfiguration(),
                SequenceType.createSequenceType("item*"),
                ExecutionMode.LOCAL,
                getMetadata()
        );
        this.mapKeys = this.mapItem.getItemKeys();
        this.keyIndex = 0;
        this.currentCallbackIterator = null;
    }

    private RuntimeIterator buildCallbackIteratorForKey(Item key) {
        List<Item> valueSequence = this.mapItem.getSequenceByKey(key);
        RuntimeIterator keyArgumentIterator = new ConstantRuntimeIterator(key, this.keyArgumentContext);
        RuntimeIterator valueArgumentIterator = new ConstantSequenceRuntimeIterator(
                valueSequence,
                this.valueArgumentContext
        );
        List<RuntimeIterator> callbackArguments = new ArrayList<>();
        callbackArguments.add(keyArgumentIterator);
        callbackArguments.add(valueArgumentIterator);
        return NamedFunctions.buildUserDefinedFunctionCallIterator(
            this.actionFunction,
            getConfiguration(),
            ExecutionMode.LOCAL,
            getMetadata(),
            callbackArguments
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
            this.currentCallbackIterator = null;

            if (this.mapKeys == null || this.keyIndex >= this.mapKeys.size()) {
                this.hasNext = false;
                return;
            }

            Item key = this.mapKeys.get(this.keyIndex++);
            this.currentCallbackIterator = buildCallbackIteratorForKey(key);
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
        this.mapIterator.reset(this.currentDynamicContextForLocalExecution);
        this.actionIterator.reset(this.currentDynamicContextForLocalExecution);
        initializeState(this.currentDynamicContextForLocalExecution);
        advanceToNextResult(this.currentDynamicContextForLocalExecution);
    }

    @Override
    protected void closeLocal() {
        if (this.mapIterator.isOpen()) {
            this.mapIterator.close();
        }
        if (this.actionIterator.isOpen()) {
            this.actionIterator.close();
        }
        if (this.currentCallbackIterator != null && this.currentCallbackIterator.isOpen()) {
            this.currentCallbackIterator.close();
        }
        this.currentCallbackIterator = null;
        this.mapKeys = null;
        this.mapItem = null;
        this.actionFunction = null;
        this.keyIndex = 0;
        this.keyArgumentContext = null;
        this.valueArgumentContext = null;
    }

    @Override
    protected boolean implementsDataFrames() {
        return false;
    }

    @Override
    public JavaRDD<Item> getRDDAux(DynamicContext context) {
        throw new OurBadException("map:for-each is currently supported only in local execution mode.");
    }

    @Override
    public JSoundDataFrame getDataFrame(DynamicContext dynamicContext) {
        throw new OurBadException("map:for-each is currently supported only in local execution mode.");
    }

    private static class ConstantSequenceRuntimeIterator extends HybridRuntimeIterator {

        private static final long serialVersionUID = 1L;

        private final List<Item> items;
        private int position;

        ConstantSequenceRuntimeIterator(List<Item> items, RuntimeStaticContext staticContext) {
            super(null, staticContext);
            this.items = items == null ? new ArrayList<>() : new ArrayList<>(items);
            this.position = 0;
        }

        @Override
        protected void openLocal() {
            this.position = 0;
            this.hasNext = !this.items.isEmpty();
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
            Item result = this.items.get(this.position);
            this.position++;
            this.hasNext = this.position < this.items.size();
            return result;
        }

        @Override
        protected void resetLocal() {
            this.position = 0;
            this.hasNext = !this.items.isEmpty();
        }

        @Override
        protected void closeLocal() {
            this.position = 0;
            this.hasNext = false;
        }

        @Override
        protected boolean implementsDataFrames() {
            return false;
        }

        @Override
        public JavaRDD<Item> getRDDAux(DynamicContext context) {
            throw new OurBadException("Constant sequence iterators are local-only.");
        }

        @Override
        public JSoundDataFrame getDataFrame(DynamicContext dynamicContext) {
            throw new OurBadException("Constant sequence iterators are local-only.");
        }
    }
}
