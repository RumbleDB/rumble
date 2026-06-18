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
package org.rumbledb.runtime.functions;

import org.apache.spark.api.java.JavaRDD;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.items.PartiallyAppliedFunctionItem.ArgumentBinding;
import org.rumbledb.items.PartiallyAppliedFunctionItem.DataFrameBinding;
import org.rumbledb.items.PartiallyAppliedFunctionItem.LocalBinding;
import org.rumbledb.items.PartiallyAppliedFunctionItem.RddBinding;
import org.rumbledb.items.structured.JSoundDataFrame;
import org.rumbledb.runtime.HybridRuntimeIterator;

import java.util.List;

/**
 * Context-independent iterator over a value captured by partial application.
 */
public class CapturedFunctionArgumentIterator extends HybridRuntimeIterator {

    private static final long serialVersionUID = 1L;

    private final List<Item> localValue;
    private final JavaRDD<Item> rddValue;
    private final JSoundDataFrame dataFrameValue;
    private int index;

    private CapturedFunctionArgumentIterator(
            List<Item> localValue,
            JavaRDD<Item> rddValue,
            JSoundDataFrame dataFrameValue,
            RuntimeStaticContext staticContext
    ) {
        super(null, staticContext);
        this.localValue = localValue;
        this.rddValue = rddValue;
        this.dataFrameValue = dataFrameValue;
    }

    public static CapturedFunctionArgumentIterator create(
            ArgumentBinding binding,
            RuntimeStaticContext staticContext
    ) {
        if (binding instanceof LocalBinding local) {
            return new CapturedFunctionArgumentIterator(local.value(), null, null, staticContext);
        }
        if (binding instanceof RddBinding rdd) {
            return new CapturedFunctionArgumentIterator(null, rdd.value(), null, staticContext);
        }
        if (binding instanceof DataFrameBinding dataFrame) {
            return new CapturedFunctionArgumentIterator(null, null, dataFrame.value(), staticContext);
        }
        throw new OurBadException("A placeholder cannot be converted to a captured argument.");
    }

    @Override
    protected void openLocal() {
        this.index = 0;
        this.hasNext = !this.localValue.isEmpty();
    }

    @Override
    protected boolean hasNextLocal() {
        return this.hasNext;
    }

    @Override
    protected Item nextLocal() {
        if (!this.hasNext) {
            throw new IteratorFlowException(FLOW_EXCEPTION_MESSAGE, getMetadata());
        }
        Item result = this.localValue.get(this.index++);
        this.hasNext = this.index < this.localValue.size();
        return result;
    }

    @Override
    protected void resetLocal() {
        openLocal();
    }

    @Override
    protected void closeLocal() {
        this.index = 0;
    }

    @Override
    public JavaRDD<Item> getRDDAux(DynamicContext context) {
        if (this.rddValue != null) {
            return this.rddValue;
        }
        return dataFrameToRDDOfItems(this.dataFrameValue, getMetadata());
    }

    @Override
    protected boolean implementsDataFrames() {
        return true;
    }

    @Override
    public JSoundDataFrame getDataFrame(DynamicContext context) {
        return this.dataFrameValue;
    }
}
