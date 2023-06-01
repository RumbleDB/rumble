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

package org.rumbledb.runtime;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.expressions.ExecutionMode;

import org.rumbledb.runtime.update.PendingUpdateList;
import sparksoniq.spark.SparkSessionManager;

import java.util.List;

public class CommaExpressionIterator extends HybridRuntimeIterator {

    private static final long serialVersionUID = 1L;
    private RuntimeIterator currentChild;
    private Item nextResult;
    private int childIndex;


    public CommaExpressionIterator(
            List<RuntimeIterator> childIterators,
            boolean isUpdating,
            ExecutionMode executionMode,
            ExceptionMetadata iteratorMetadata
    ) {
        super(childIterators, executionMode, iteratorMetadata);
        this.isUpdating = isUpdating;
    }

    public CommaExpressionIterator(
            List<RuntimeIterator> childIterators,
            ExecutionMode executionMode,
            ExceptionMetadata iteratorMetadata
    ) {
        this(childIterators, false, executionMode, iteratorMetadata);
    }

    @Override
    public Item nextLocal() {
        if (this.hasNext) {
            Item result = this.nextResult; // save the result to be returned
            setNextResult(); // calculate and store the next result
            return result;
        }
        throw new IteratorFlowException("Invalid next() call in Comma expression", getMetadata());
    }

    private void startLocal() {
        this.childIndex = 0;

        if (this.children.size() >= 1) {
            this.currentChild = this.children.get(this.childIndex);
            this.currentChild.open(this.currentDynamicContextForLocalExecution);
        } else {
            this.currentChild = null;
        }

        setNextResult();
    }

    @Override
    public void openLocal() {
        startLocal();
    }

    public void setNextResult() {
        if (this.currentChild == null) {
            this.hasNext = false;
            return;
        }

        this.nextResult = null;

        while (this.nextResult == null) {
            if (this.currentChild.hasNext()) {
                this.nextResult = this.currentChild.next();
            } else {
                this.currentChild.close();
                if (++this.childIndex == this.children.size()) {
                    this.currentChild = null;
                    break;
                }
                this.currentChild = this.children.get(this.childIndex);
                this.currentChild.open(this.currentDynamicContextForLocalExecution);
            }
        }

        this.hasNext = this.nextResult != null;
    }

    @Override
    protected boolean hasNextLocal() {
        return this.hasNext;
    }

    @Override
    protected void resetLocal() {
        startLocal();
    }

    @Override
    protected void closeLocal() {
        if (this.currentChild != null) {
            this.currentChild.close();
        }
    }

    @Override
    public JavaRDD<Item> getRDDAux(DynamicContext dynamicContext) {
        if (!this.children.isEmpty()) {
            this.childIndex = 0;
            this.currentChild = this.children.get(this.childIndex);

            JavaRDD<Item> childRDD = this.currentChild.getRDD(dynamicContext);
            this.childIndex++;

            while (this.childIndex < this.children.size()) {
                this.currentChild = this.children.get(this.childIndex);
                JavaRDD<Item> nextChildRDD = this.currentChild.getRDD(dynamicContext);
                childRDD = childRDD.union(nextChildRDD);
                this.childIndex++;
            }
            return childRDD;
        } else {
            JavaSparkContext sparkContext = SparkSessionManager.getInstance().getJavaSparkContext();
            return sparkContext.emptyRDD();
        }
    }

    @Override
    public PendingUpdateList getPendingUpdateList(DynamicContext context) {
        if (!isUpdating()) {
            return new PendingUpdateList();
        }

        PendingUpdateList pul = new PendingUpdateList();
        for (RuntimeIterator child : this.children) {
            pul = PendingUpdateList.mergeUpdates(pul, child.getPendingUpdateList(context), this.getMetadata());
        }
        return pul;
    }
}
