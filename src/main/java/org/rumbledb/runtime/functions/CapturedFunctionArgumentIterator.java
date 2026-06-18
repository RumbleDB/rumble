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
import org.rumbledb.items.PartiallyAppliedFunctionItem.ArgumentBinding;
import org.rumbledb.items.PartiallyAppliedFunctionItem.DataFrameBinding;
import org.rumbledb.items.PartiallyAppliedFunctionItem.LocalBinding;
import org.rumbledb.items.PartiallyAppliedFunctionItem.RDDBinding;
import org.rumbledb.items.structured.JSoundDataFrame;
import org.rumbledb.runtime.HybridRuntimeIterator;

import java.util.List;

/**
 * Context-independent iterator over a value captured by partial application.
 */
public class CapturedFunctionArgumentIterator extends HybridRuntimeIterator {

    private static final long serialVersionUID = 1L;

    private final ArgumentBinding binding;
    private int index;

    public CapturedFunctionArgumentIterator(ArgumentBinding binding, RuntimeStaticContext staticContext) {
        super(null, staticContext);
        this.binding = binding;
    }

    @Override
    protected void openLocal() {
        this.index = 0;
        this.hasNext = !getLocalValue().isEmpty();
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
        Item result = getLocalValue().get(this.index++);
        this.hasNext = this.index < getLocalValue().size();
        return result;
    }

    @Override
    protected void resetLocal() {
        this.index = 0;
        this.hasNext = !getLocalValue().isEmpty();
    }

    @Override
    protected void closeLocal() {
        this.index = 0;
    }

    @Override
    public JavaRDD<Item> getRDDAux(DynamicContext context) {
        return ((RDDBinding) this.binding).value();
    }

    @Override
    protected boolean implementsDataFrames() {
        return true;
    }

    @Override
    public JSoundDataFrame getDataFrame(DynamicContext context) {
        return ((DataFrameBinding) this.binding).value();
    }

    private List<Item> getLocalValue() {
        return ((LocalBinding) this.binding).value();
    }
}
