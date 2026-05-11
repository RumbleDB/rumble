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

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.runtime.HybridRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;

import java.util.List;

public class RemoveFunctionIterator extends HybridRuntimeIterator {


    private static final long serialVersionUID = 1L;
    private RuntimeIterator sequenceIterator;
    private RuntimeIterator positionIterator;
    private Item nextResult;
    private int removePosition; // position to remove the item
    private int currentPosition; // current position


    public RemoveFunctionIterator(
            List<RuntimeIterator> parameters,
            RuntimeStaticContext staticContext
    ) {
        super(parameters, staticContext);
        this.sequenceIterator = this.children.get(0);
        this.positionIterator = this.children.get(1);
    }

    @Override
    protected JavaRDD<Item> getRDDAux(DynamicContext context) {
        init(context);
        JavaRDD<Item> childRDD = this.sequenceIterator.getRDD(context);

        JavaPairRDD<Item, Long> zippedRDD = childRDD.zipWithIndex();
        JavaPairRDD<Item, Long> filteredRDD = zippedRDD.filter((item) -> item._2() != this.removePosition - 1);
        return filteredRDD.map((item) -> item._1);
    }

    @Override
    protected void openLocal() {
        init(this.currentDynamicContextForLocalExecution);
        this.currentPosition = 1;

        this.sequenceIterator.open(this.currentDynamicContextForLocalExecution);
        setNextResult();
    }

    @Override
    protected void closeLocal() {
        this.sequenceIterator.close();
    }

    @Override
    protected void resetLocal() {
        this.currentPosition = 1;

        this.sequenceIterator.reset(this.currentDynamicContextForLocalExecution);
        setNextResult();
    }

    @Override
    protected boolean hasNextLocal() {
        return this.hasNext;
    }

    @Override
    protected Item nextLocal() {
        if (this.hasNext()) {
            Item result = this.nextResult; // save the result to be returned
            setNextResult(); // calculate and store the next result
            return result;
        }
        throw new IteratorFlowException(FLOW_EXCEPTION_MESSAGE + "remove function", getMetadata());
    }

    private void init(DynamicContext context) {
        Item positionItem = this.positionIterator.materializeFirstItemOrNull(context);
        this.removePosition = positionItem.getIntValue();
    }

    public void setNextResult() {
        this.nextResult = null;

        if (this.sequenceIterator.hasNext()) {
            if (this.currentPosition == this.removePosition) {
                this.sequenceIterator.next(); // skip item to be removed
                this.currentPosition++;
                if (this.sequenceIterator.hasNext()) {
                    this.nextResult = this.sequenceIterator.next();
                    this.currentPosition++;
                }
            } else {
                this.nextResult = this.sequenceIterator.next();
                this.currentPosition++;
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
