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
 * See the License for the specific permissions and limitations under
 * the License.
 */

package org.rumbledb.runtime.functions.arrays;

import org.apache.spark.api.java.JavaRDD;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.items.structured.JSoundDataFrame;
import org.rumbledb.runtime.HybridRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;

import java.util.ArrayList;
import java.util.List;

/**
 * F&amp;O 3.1 array:join — concatenates the members of a sequence of arrays in order into one array.
 */
public class ArrayJoinFunctionIterator extends HybridRuntimeIterator {

    private static final long serialVersionUID = 1L;

    private final RuntimeIterator arraysIterator;
    private Item resultItem;
    private boolean hasProducedResult;

    public ArrayJoinFunctionIterator(
            List<RuntimeIterator> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
        if (arguments.size() != 1) {
            throw new OurBadException("array:join must have exactly one argument.");
        }
        this.arraysIterator = arguments.get(0);
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
        List<Item> arrays = this.arraysIterator.materialize(context);
        List<List<Item>> joined = new ArrayList<>();
        for (Item arrayItem : arrays) {
            if (!arrayItem.isArray()) {
                throw new UnexpectedTypeException(
                        "Type error; array:join expects a sequence of arrays.",
                        getMetadata()
                );
            }
            int n = arrayItem.getSize();
            for (int i = 0; i < n; i++) {
                joined.add(new ArrayList<>(arrayItem.getSequenceAt(i)));
            }
        }
        // when joining, we always create a sequence array for now
        this.resultItem = ItemFactory.getInstance().createSequenceArrayItem(joined, false);
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
        this.arraysIterator.reset(this.currentDynamicContextForLocalExecution);
        initializeResult(this.currentDynamicContextForLocalExecution);
        this.hasNext = this.resultItem != null;
        this.hasProducedResult = false;
    }

    @Override
    protected void closeLocal() {
        if (this.arraysIterator.isOpen()) {
            this.arraysIterator.close();
        }
        this.resultItem = null;
        this.hasProducedResult = false;
    }

    @Override
    public JavaRDD<Item> getRDDAux(DynamicContext dynamicContext) {
        throw new OurBadException(
                "array:join is currently supported only in local execution mode."
        );
    }

    @Override
    protected boolean implementsDataFrames() {
        return false;
    }

    @Override
    public JSoundDataFrame getDataFrame(DynamicContext dynamicContext) {
        throw new OurBadException(
                "array:join is currently supported only in local execution mode."
        );
    }
}
