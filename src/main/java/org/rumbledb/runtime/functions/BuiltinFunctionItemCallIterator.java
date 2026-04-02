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
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.items.structured.JSoundDataFrame;
import org.rumbledb.runtime.HybridRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.update.PendingUpdateList;

import java.util.ArrayList;
import java.util.List;

/**
 * Dynamic invocation of a function item that represents a builtin named function reference.
 */
public class BuiltinFunctionItemCallIterator extends HybridRuntimeIterator {

    private static final long serialVersionUID = 1L;

    private final Item functionItem;
    private final List<RuntimeIterator> functionArguments;

    private RuntimeIterator builtinDelegate;
    private Item nextResult;

    public BuiltinFunctionItemCallIterator(
            Item functionItem,
            List<RuntimeIterator> functionArguments,
            RuntimeStaticContext staticContext
    ) {
        super(null, staticContext);
        for (RuntimeIterator arg : functionArguments) {
            if (arg != null) {
                this.children.add(arg);
            }
        }
        this.functionItem = functionItem;
        this.functionArguments = functionArguments;
        this.isUpdating = functionItem.getSignature().isUpdating();

        FunctionCallArgumentCoercion.validateArity(functionItem, this.functionArguments, getMetadata());
        FunctionCallArgumentCoercion.wrapAccordingToSignature(
            functionItem,
            this.functionArguments,
            staticContext
        );
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
    public void openLocal() {
        this.builtinDelegate = newBuiltinDelegate();
        this.builtinDelegate.open(this.currentDynamicContextForLocalExecution);
        setNextResult();
    }

    @Override
    public Item nextLocal() {
        if (this.hasNext) {
            Item result = this.nextResult;
            setNextResult();
            return result;
        }
        throw new IteratorFlowException(
                RuntimeIterator.FLOW_EXCEPTION_MESSAGE
                    + " in "
                    + this.functionItem.getIdentifier().getName()
                    + "  function",
                getMetadata()
        );
    }

    @Override
    protected boolean hasNextLocal() {
        return this.hasNext;
    }

    @Override
    protected void resetLocal() {
        this.builtinDelegate.reset(this.currentDynamicContextForLocalExecution);
        setNextResult();
    }

    @Override
    protected void closeLocal() {
        if (this.builtinDelegate != null && this.builtinDelegate.isOpen()) {
            this.builtinDelegate.close();
        }
    }

    private void setNextResult() {
        this.nextResult = null;
        if (this.builtinDelegate.hasNext()) {
            this.nextResult = this.builtinDelegate.next();
        }
        if (this.nextResult == null) {
            this.hasNext = false;
            this.builtinDelegate.close();
        } else {
            this.hasNext = true;
        }
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
