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
import org.rumbledb.exceptions.ArrayIndexOutOfBoundsException;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.exceptions.MoreThanOneItemException;
import org.rumbledb.exceptions.NoItemException;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.items.structured.JSoundDataFrame;
import org.rumbledb.runtime.HybridRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * F&amp;O 3.1 array:remove — returns a new array with members at the given 1-based positions omitted
 * (distinct positions; order preserved). Raises FOAY0001 if any position is out of bounds.
 */
public class ArrayRemoveFunctionIterator extends HybridRuntimeIterator {

    private static final long serialVersionUID = 1L;

    private final RuntimeIterator arrayIterator;
    private final RuntimeIterator positionsIterator;
    private Item resultItem;
    private boolean hasProducedResult;

    public ArrayRemoveFunctionIterator(
            List<RuntimeIterator> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
        if (arguments.size() != 2) {
            throw new OurBadException("array:remove must have exactly two arguments.");
        }
        this.arrayIterator = arguments.get(0);
        this.positionsIterator = arguments.get(1);
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
            throw new UnexpectedTypeException(
                    "array:remove expects exactly one array as the first argument.",
                    getMetadata()
            );
        } catch (MoreThanOneItemException e) {
            throw new UnexpectedTypeException(
                    "array:remove expects exactly one array as the first argument.",
                    getMetadata()
            );
        }

        if (!arrayItem.isArray()) {
            throw new UnexpectedTypeException(
                    "Type error; first argument to array:remove must be an array.",
                    getMetadata()
            );
        }

        int size = arrayItem.getSize();
        List<Item> positionItems = this.positionsIterator.materialize(context);

        if (positionItems.isEmpty()) {
            this.resultItem = arrayItem;
            return;
        }

        Set<BigInteger> positionsToRemove = new HashSet<>();
        BigInteger min = BigInteger.ONE;
        BigInteger max = BigInteger.valueOf(size);

        for (Item p : positionItems) {
            if (!p.isNumeric()) {
                throw new UnexpectedTypeException(
                        "Type error; positions in array:remove must be numeric.",
                        getMetadata()
                );
            }
            BigInteger pos = p.isInteger() ? p.castToIntegerValue() : BigInteger.valueOf(p.castToIntValue());
            if (pos.compareTo(min) < 0 || pos.compareTo(max) > 0) {
                throw new ArrayIndexOutOfBoundsException(
                        "array:remove position out of bounds: " + pos + ", array length: " + size,
                        getMetadata()
                );
            }
            positionsToRemove.add(pos);
        }

        List<List<Item>> originalMembers = arrayItem.getSequenceMembers();
        List<List<Item>> keptMembers = new ArrayList<>(Math.max(0, size - positionsToRemove.size()));
        for (int i = 0; i < size; i++) {
            BigInteger oneBased = BigInteger.valueOf((long) i + 1);
            if (!positionsToRemove.contains(oneBased)) {
                keptMembers.add(originalMembers.get(i));
            }
        }

        this.resultItem = ItemFactory.getInstance().createArrayFromMemberSequences(keptMembers, false);
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
        this.positionsIterator.reset(this.currentDynamicContextForLocalExecution);
        initializeResult(this.currentDynamicContextForLocalExecution);
        this.hasNext = this.resultItem != null;
        this.hasProducedResult = false;
    }

    @Override
    protected void closeLocal() {
        if (this.arrayIterator.isOpen()) {
            this.arrayIterator.close();
        }
        if (this.positionsIterator.isOpen()) {
            this.positionsIterator.close();
        }
        this.resultItem = null;
        this.hasProducedResult = false;
    }

    @Override
    public JavaRDD<Item> getRDDAux(DynamicContext dynamicContext) {
        throw new OurBadException(
                "array:remove is currently supported only in local execution mode."
        );
    }

    @Override
    protected boolean implementsDataFrames() {
        return false;
    }

    @Override
    public JSoundDataFrame getDataFrame(DynamicContext dynamicContext) {
        throw new OurBadException(
                "array:remove is currently supported only in local execution mode."
        );
    }
}
