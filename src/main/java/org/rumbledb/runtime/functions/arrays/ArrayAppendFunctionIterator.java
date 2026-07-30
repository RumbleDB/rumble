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

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

import org.apache.spark.api.java.JavaRDD;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.items.structured.HomogeneousItemDataFrame;
import org.rumbledb.runtime.HybridRuntimeIterator;
import org.rumbledb.runtime.plan.DataFrameRuntimePlan;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.cursor.ComputedLocalCursor;
import org.rumbledb.runtime.cursor.LocalCursor;

/**
 * F&amp;O 3.1 array:append — returns a new array with one additional member (the appendage sequence).
 */
public class ArrayAppendFunctionIterator extends HybridRuntimeIterator implements DataFrameRuntimePlan<Item> {

    @Serial
    private static final long serialVersionUID = 1L;

    private final RuntimeIterator arrayIterator;
    private final RuntimeIterator appendageIterator;
    private Item resultItem;
    private boolean hasProducedResult;

    public ArrayAppendFunctionIterator(
            List<RuntimeIterator> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
        if (arguments.size() != 2) {
            throw new OurBadException("array:append must have exactly two arguments.");
        }
        this.arrayIterator = arguments.get(0);
        this.appendageIterator = arguments.get(1);
        this.resultItem = null;
        this.hasProducedResult = false;
    }

    @Override
    public LocalCursor<Item> createLocalCursor(DynamicContext context) {
        return new ComputedLocalCursor<>(
                () -> createResult(
                    requireArray(this.arrayIterator.materialize(context)),
                    this.appendageIterator.materialize(context)
                ),
                getMetadata()
        );
    }

    @Override
    protected void openLocal() {
        // Do not open child iterators here: materializeExactlyOneItem / materialize open and close them.
        initializeResult(this.currentDynamicContextForLocalExecution);
        this.hasNext = this.resultItem != null;
        this.hasProducedResult = false;
    }

    private void initializeResult(DynamicContext context) {
        Item arrayItem = requireArray(this.arrayIterator.materialize(context));
        this.resultItem = createResult(arrayItem, this.appendageIterator.materialize(context));
    }

    private Item requireArray(List<Item> items) {
        if (items.size() != 1) {
            throw new UnexpectedTypeException(
                    "array:append expects exactly one array as the first argument.",
                    getMetadata()
            );
        }
        Item arrayItem = items.get(0);
        if (!arrayItem.isArray()) {
            throw new UnexpectedTypeException(
                    "Type error; first argument to array:append must be an array.",
                    getMetadata()
            );
        }
        return arrayItem;
    }

    private Item createResult(Item arrayItem, List<Item> appendage) {
        if (arrayItem.isArrayOfItems() && appendage.size() == 1) {
            List<Item> newItems = new ArrayList<>(arrayItem.getSize() + 1);
            newItems.addAll(arrayItem.getItemMembers());
            newItems.add(appendage.get(0));
            return ItemFactory.getInstance()
                .createArrayItem(newItems, this.getRuntimeStaticContext().isQuerySideEffecting());
        } else {
            List<List<Item>> newMemberSequences = new ArrayList<>(arrayItem.getSize() + 1);
            newMemberSequences.addAll(arrayItem.getSequenceMembers());
            newMemberSequences.add(appendage);
            return ItemFactory.getInstance()
                .createSequenceArrayItem(newMemberSequences, this.getRuntimeStaticContext().isQuerySideEffecting());
        }
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
    protected void closeLocal() {
        if (this.arrayIterator.isOpen()) {
            this.arrayIterator.close();
        }
        if (this.appendageIterator.isOpen()) {
            this.appendageIterator.close();
        }
        this.resultItem = null;
        this.hasProducedResult = false;
    }

    @Override
    public JavaRDD<Item> getRDDAux(DynamicContext dynamicContext) {
        throw new OurBadException(
                "array:append is currently supported only in local execution mode."
        );
    }

    @Override
    public HomogeneousItemDataFrame getNativeDataFrame(DynamicContext dynamicContext) {
        throw new OurBadException(
                "array:append is currently supported only in local execution mode."
        );
    }
}
