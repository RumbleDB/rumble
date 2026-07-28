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
import java.util.Collections;
import java.util.List;

import org.apache.spark.api.java.JavaRDD;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.exceptions.MoreThanOneItemException;
import org.rumbledb.exceptions.NoItemException;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.items.structured.JSoundDataFrame;
import org.rumbledb.runtime.HybridRuntimeIterator;
import org.rumbledb.runtime.plan.DataFrameRuntimePlan;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.cursor.ComputedLocalCursor;
import org.rumbledb.runtime.cursor.LocalCursor;

public class ArrayReverseFunctionIterator extends HybridRuntimeIterator implements DataFrameRuntimePlan<Item> {

    @Serial
    private static final long serialVersionUID = 1L;

    private final RuntimeIterator arrayIterator;
    private Item resultItem;
    private boolean hasProducedResult;

    public ArrayReverseFunctionIterator(
            List<RuntimeIterator> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
        if (arguments.size() != 1) {
            throw new OurBadException("array:reverse must have exactly one argument.");
        }
        this.arrayIterator = arguments.get(0);
        this.resultItem = null;
        this.hasProducedResult = false;
    }

    @Override
    public LocalCursor<Item> createLocalCursor(DynamicContext context) {
        return new ComputedLocalCursor<>(
                () -> reverseArgument(this.arrayIterator.materialize(context)),
                getMetadata()
        );
    }

    @Override
    protected void openLocal() {
        this.arrayIterator.open(this.currentDynamicContextForLocalExecution);
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
                    "array:reverse expects exactly one array argument.",
                    getMetadata()
            );
        }

        this.resultItem = reverse(arrayItem);
    }

    private Item reverse(Item arrayItem) {
        if (arrayItem == null) {
            return null;
        }
        if (!arrayItem.isArray()) {
            throw new UnexpectedTypeException(
                    "Type error; argument to array:reverse must be an array.",
                    getMetadata()
            );
        }

        if (arrayItem.isArrayOfItems()) {
            List<Item> originalMembers = arrayItem.getItemMembers();
            List<Item> reversedMembers = new ArrayList<>(originalMembers);
            Collections.reverse(reversedMembers);
            return ItemFactory.getInstance()
                .createArrayItem(reversedMembers, this.getRuntimeStaticContext().isQuerySideEffecting());
        } else {
            List<List<Item>> originalMembers = arrayItem.getSequenceMembers();
            List<List<Item>> reversedMembers = new ArrayList<>(originalMembers);
            Collections.reverse(reversedMembers);
            return ItemFactory.getInstance()
                .createSequenceArrayItem(reversedMembers, this.getRuntimeStaticContext().isQuerySideEffecting());
        }
    }

    private Item reverseArgument(List<Item> items) {
        if (items.size() > 1) {
            throw new UnexpectedTypeException(
                    "array:reverse expects exactly one array argument.",
                    getMetadata()
            );
        }
        return reverse(items.isEmpty() ? null : items.get(0));
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
        this.resultItem = null;
        this.hasProducedResult = false;
    }

    @Override
    public JavaRDD<Item> getRDDAux(DynamicContext dynamicContext) {
        throw new OurBadException(
                "array:reverse is currently supported only in local execution mode."
        );
    }

    @Override
    public JSoundDataFrame getNativeDataFrame(DynamicContext dynamicContext) {
        throw new OurBadException(
                "array:reverse is currently supported only in local execution mode."
        );
    }
}
