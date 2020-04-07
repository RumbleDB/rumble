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

package org.rumbledb.runtime.functions.sequences.value;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.exceptions.NonAtomicKeyException;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.HybridRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import sparksoniq.jsoniq.ExecutionMode;

import java.util.List;

public class IndexOfFunctionIterator extends HybridRuntimeIterator {


    private static final long serialVersionUID = 1L;
    private RuntimeIterator sequenceIterator;
    private RuntimeIterator searchIterator;
    private Item search;
    private Item nextResult;
    private int currentIndex;

    public IndexOfFunctionIterator(
            List<RuntimeIterator> arguments,
            ExecutionMode executionMode,
            ExceptionMetadata iteratorMetadata
    ) {
        super(arguments, executionMode, iteratorMetadata);
        this.sequenceIterator = this.children.get(0);
        this.searchIterator = this.children.get(1);
    }

    @Override
    protected JavaRDD<Item> getRDDAux(DynamicContext context) {
        JavaRDD<Item> childRDD = this.sequenceIterator.getRDD(context);
        this.search = this.searchIterator.materializeFirstItemOrNull(context);

        JavaPairRDD<Item, Long> zippedRDD = childRDD.zipWithIndex();
        JavaPairRDD<Item, Long> filteredRDD = zippedRDD.filter((item) -> item._1().equals(this.search));
        return filteredRDD.map((item) -> ItemFactory.getInstance().createIntegerItem(item._2.intValue() + 1));
    }

    @Override
    protected void openLocal() {
        this.currentIndex = 0;
        this.sequenceIterator.open(this.currentDynamicContextForLocalExecution);
        this.search = this.searchIterator.materializeFirstItemOrNull(this.currentDynamicContextForLocalExecution);
        setNextResult();
    }

    @Override
    protected void closeLocal() {
        this.sequenceIterator.close();
    }

    @Override
    protected void resetLocal(DynamicContext context) {
        this.currentIndex = 0;
        this.sequenceIterator.reset(this.currentDynamicContextForLocalExecution);
        setNextResult();
    }

    @Override
    protected boolean hasNextLocal() {
        return this.hasNext;
    }

    @Override
    protected Item nextLocal() {
        if (this.hasNext) {
            Item result = this.nextResult; // save the result to be returned
            setNextResult(); // calculate and store the next result
            return result;
        }
        throw new IteratorFlowException(FLOW_EXCEPTION_MESSAGE + "index-of function", getMetadata());
    }

    public void setNextResult() {
        this.nextResult = null;

        while (this.sequenceIterator.hasNext()) {
            this.currentIndex += 1;
            Item item = this.sequenceIterator.next();
            if (!item.isAtomic()) {
                throw new NonAtomicKeyException(
                        "Invalid args. index-of can't be performed with a non-atomic in the input sequence",
                        getMetadata()
                );
            } else {
                if (item.compareTo(this.search) == 0) {
                    this.nextResult = ItemFactory.getInstance().createIntegerItem(this.currentIndex);
                    break;
                }
            }
        }

        if (this.nextResult == null) {
            this.hasNext = false;
            this.sequenceIterator.close();
        } else {
            this.hasNext = true;
        }
    }
}
