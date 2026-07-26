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
import org.rumbledb.context.NamedFunctions;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.items.structured.JSoundDataFrame;
import org.rumbledb.runtime.HybridRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.cursor.AbstractLocalCursor;
import org.rumbledb.runtime.cursor.LocalCursor;
import org.rumbledb.runtime.update.PendingUpdateList;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

/**
 * Dynamic invocation of a function item that represents a builtin named function reference.
 */
public class BuiltinFunctionItemCallIterator extends HybridRuntimeIterator {

    @Serial
    private static final long serialVersionUID = 1L;

    private final Item functionItem;
    private final List<RuntimeIterator> functionArguments;

    public BuiltinFunctionItemCallIterator(
            Item functionItem,
            List<RuntimeIterator> functionArguments,
            RuntimeStaticContext staticContext
    ) {
        super(
            functionArguments.stream().filter(arg -> arg != null).toList(),
            staticContext.toBuilder().isUpdating(functionItem.getSignature().isUpdating()).build()
        );

        this.functionItem = functionItem;
        this.functionArguments = functionArguments;

        FunctionCallArgumentConversion.validateArity(functionItem, this.functionArguments, getMetadata());
        FunctionCallArgumentConversion.wrapAccordingToSignature(
            functionItem,
            this.functionArguments,
            staticContext
        );
    }

    @Override
    public LocalCursor<Item> createLocalCursor(DynamicContext context) {
        return new BuiltinCallLocalCursor(this, context);
    }

    private static final class BuiltinCallLocalCursor extends AbstractLocalCursor<Item> {
        private final BuiltinFunctionItemCallIterator plan;
        private final DynamicContext context;
        private LocalCursor<Item> delegate;

        private BuiltinCallLocalCursor(BuiltinFunctionItemCallIterator plan, DynamicContext context) {
            super(plan.getMetadata());
            this.plan = plan;
            this.context = context;
        }

        @Override
        protected void openLocal() {
            this.delegate = this.plan.newBuiltinDelegate().createLocalCursor(this.context);
            this.delegate.open();
        }

        @Override
        protected boolean hasNextLocal() {
            return this.delegate.hasNext();
        }

        @Override
        protected Item nextLocal() {
            return this.delegate.next();
        }

        @Override
        protected void closeLocal() {
            if (this.delegate != null) {
                this.delegate.close();
                this.delegate = null;
            }
        }
    }

    private RuntimeIterator newBuiltinDelegate() {
        return NamedFunctions.getBuiltInFunctionIterator(
            this.functionItem.getIdentifier(),
            new ArrayList<>(this.functionArguments),
            this.staticContext,
            true
        );
    }

    @Override
    public JavaRDD<Item> getRDDAux(DynamicContext dynamicContext) {
        RuntimeIterator delegate = newBuiltinDelegate();
        return delegate.getRDD(dynamicContext);
    }

    @Override
    protected boolean implementsDataFrames() {
        return true;
    }

    @Override
    public JSoundDataFrame getDataFrame(DynamicContext dynamicContext) {
        RuntimeIterator delegate = newBuiltinDelegate();
        return delegate.getDataFrame(dynamicContext);
    }

    @Override
    public PendingUpdateList getPendingUpdateList(DynamicContext context) {
        if (!isUpdating()) {
            return new PendingUpdateList();
        }
        RuntimeIterator delegate = newBuiltinDelegate();
        return delegate.getPendingUpdateList(context);
    }
}
