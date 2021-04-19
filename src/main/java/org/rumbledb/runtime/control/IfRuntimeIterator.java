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

package org.rumbledb.runtime.control;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.runtime.HybridRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;

public class IfRuntimeIterator extends HybridRuntimeIterator {


    private static final long serialVersionUID = 1L;
    private RuntimeIterator selectedIterator = null;

    public IfRuntimeIterator(
            RuntimeIterator condition,
            RuntimeIterator branch,
            RuntimeIterator elseBranch,
            ExecutionMode executionMode,
            ExceptionMetadata iteratorMetadata
    ) {
        super(null, executionMode, iteratorMetadata);
        this.children.add(condition);
        this.children.add(branch);
        this.children.add(elseBranch);
    }

    @Override
    public void resetLocal() {
        this.selectedIterator.close();
        this.selectedIterator = selectApplicableIterator(this.currentDynamicContextForLocalExecution);
        this.selectedIterator.open(this.currentDynamicContextForLocalExecution);
        this.hasNext = this.selectedIterator.hasNext();
    }

    @Override
    public void openLocal() {
        this.selectedIterator = selectApplicableIterator(this.currentDynamicContextForLocalExecution);
        this.selectedIterator.open(this.currentDynamicContextForLocalExecution);
        this.hasNext = this.selectedIterator.hasNext();
    }

    @Override
    public void closeLocal() {
        this.selectedIterator.close();
    }

    @Override
    public Item nextLocal() {
        if (!this.hasNext) {
            throw new IteratorFlowException("No next item.");
        }
        Item result = this.selectedIterator.next();
        this.hasNext = this.selectedIterator.hasNext();
        return result;
    }

    @Override
    public boolean hasNextLocal() {
        return this.hasNext;
    }

    public RuntimeIterator selectApplicableIterator(DynamicContext dynamicContext) {
        RuntimeIterator condition = this.children.get(0);
        boolean effectiveBooleanValue = condition.getEffectiveBooleanValue(dynamicContext);
        if (effectiveBooleanValue) {
            return this.children.get(1);
        } else {
            return this.children.get(2);
        }
    }

    @Override
    public JavaRDD<Item> getRDDAux(DynamicContext dynamicContext) {
        RuntimeIterator iterator = selectApplicableIterator(dynamicContext);
        return iterator.getRDD(dynamicContext);
    }

    @Override
    protected boolean implementsDataFrames() {
        return true;
    }

    @Override
    public Dataset<Row> getDataFrame(DynamicContext dynamicContext) {
        RuntimeIterator iterator = selectApplicableIterator(dynamicContext);

        return iterator.getDataFrame(dynamicContext);
    }
}
