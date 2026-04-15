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
import java.util.List;

/**
 * F&amp;O 3.1 array:put — returns a new array with the member at a 1-based position replaced
 * by a given sequence (FOAY0001 if position is out of bounds).
 */
public class ArrayPutFunctionIterator extends HybridRuntimeIterator {

    private static final long serialVersionUID = 1L;

    private final RuntimeIterator arrayIterator;
    private final RuntimeIterator positionIterator;
    private final RuntimeIterator memberIterator;
    private Item resultItem;
    private boolean hasProducedResult;

    public ArrayPutFunctionIterator(
            List<RuntimeIterator> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
        if (arguments.size() != 3) {
            throw new OurBadException("array:put must have exactly three arguments.");
        }
        this.arrayIterator = arguments.get(0);
        this.positionIterator = arguments.get(1);
        this.memberIterator = arguments.get(2);
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
                    "array:put expects exactly one array as the first argument.",
                    getMetadata()
            );
        } catch (MoreThanOneItemException e) {
            throw new UnexpectedTypeException(
                    "array:put expects exactly one array as the first argument.",
                    getMetadata()
            );
        }

        if (!arrayItem.isArray()) {
            throw new UnexpectedTypeException(
                    "Type error; first argument to array:put must be an array.",
                    getMetadata()
            );
        }

        Item positionItem;
        try {
            positionItem = this.positionIterator.materializeExactlyOneItem(context);
        } catch (NoItemException e) {
            throw new UnexpectedTypeException(
                    "array:put expects exactly one position argument.",
                    getMetadata()
            );
        } catch (MoreThanOneItemException e) {
            throw new UnexpectedTypeException(
                    "array:put expects exactly one position argument.",
                    getMetadata()
            );
        }

        if (!positionItem.isNumeric()) {
            throw new UnexpectedTypeException(
                    "Type error; position argument to array:put must be numeric.",
                    getMetadata()
            );
        }

        BigInteger positionInteger;
        if (positionItem.isInteger()) {
            positionInteger = positionItem.castToIntegerValue();
        } else {
            positionInteger = BigInteger.valueOf(positionItem.castToIntValue());
        }

        int size = arrayItem.getSize();
        BigInteger min = BigInteger.ONE;
        BigInteger max = BigInteger.valueOf((long) size);
        if (positionInteger.compareTo(min) < 0 || positionInteger.compareTo(max) > 0) {
            throw new ArrayIndexOutOfBoundsException(
                    "array:put position out of bounds: "
                        + positionInteger
                        + ", array length: "
                        + size,
                    getMetadata()
            );
        }

        int replaceIndex = positionInteger.intValue() - 1;
        List<Item> memberSequence = this.memberIterator.materialize(context);

        if (arrayItem.isArrayOfItems() && memberSequence.size() == 1) {
            List<Item> newItems = new ArrayList<>(size);
            for (int i = 0; i < size; i++) {
                if (i == replaceIndex) {
                    newItems.add(memberSequence.get(0));
                } else {
                    newItems.add(arrayItem.getItemAt(i));
                }
            }
            this.resultItem = ItemFactory.getInstance().createArrayItem(newItems, false);
        } else {
            List<List<Item>> newMemberSequences = new ArrayList<>(size);
            for (int i = 0; i < size; i++) {
                if (i == replaceIndex) {
                    newMemberSequences.add(memberSequence);
                } else {
                    newMemberSequences.add(arrayItem.getSequenceAt(i));
                }
            }
            this.resultItem = ItemFactory.getInstance().createSequenceArrayItem(newMemberSequences, false);
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
    protected void resetLocal() {
        this.arrayIterator.reset(this.currentDynamicContextForLocalExecution);
        this.positionIterator.reset(this.currentDynamicContextForLocalExecution);
        this.memberIterator.reset(this.currentDynamicContextForLocalExecution);
        initializeResult(this.currentDynamicContextForLocalExecution);
        this.hasNext = this.resultItem != null;
        this.hasProducedResult = false;
    }

    @Override
    protected void closeLocal() {
        if (this.arrayIterator.isOpen()) {
            this.arrayIterator.close();
        }
        if (this.positionIterator.isOpen()) {
            this.positionIterator.close();
        }
        if (this.memberIterator.isOpen()) {
            this.memberIterator.close();
        }
        this.resultItem = null;
        this.hasProducedResult = false;
    }

    @Override
    public JavaRDD<Item> getRDDAux(DynamicContext dynamicContext) {
        throw new OurBadException(
                "array:put is currently supported only in local execution mode."
        );
    }

    @Override
    protected boolean implementsDataFrames() {
        return false;
    }

    @Override
    public JSoundDataFrame getDataFrame(DynamicContext dynamicContext) {
        throw new OurBadException(
                "array:put is currently supported only in local execution mode."
        );
    }
}
