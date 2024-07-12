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

package org.rumbledb.runtime.functions;

import org.apache.spark.api.java.JavaRDD;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.NamedFunctions;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.ExitStatementException;
import org.rumbledb.exceptions.InvalidRumbleMLParamException;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.exceptions.MoreThanOneItemException;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.items.structured.JSoundDataFrame;
import org.rumbledb.runtime.HybridRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.update.PendingUpdateList;

import java.util.List;

public class DynamicFunctionCallIterator extends HybridRuntimeIterator {
    // dynamic: functionIdentifier is not known at compile time
    // it is known only after evaluating postfix expression at runtime

    private static final long serialVersionUID = 1L;
    // parametrized fields
    private RuntimeIterator functionItemIterator;
    private List<RuntimeIterator> functionArguments;

    // calculated fields
    private Item functionItem;
    private RuntimeIterator functionCallIterator;
    private boolean isPartialApplication;
    private Item nextResult;

    // Exit statement fields
    private boolean encounteredExitStatement;
    private PendingUpdateList pendingUpdateList;
    private List<Item> exitStatementLocalResult;
    private JavaRDD<Item> rddResult;
    private JSoundDataFrame dataFrameResult;
    private int nextExitStatementResult;

    public DynamicFunctionCallIterator(
            RuntimeIterator functionItemIterator,
            List<RuntimeIterator> functionArguments,
            RuntimeStaticContext staticContext
    ) {
        super(null, staticContext);
        this.isPartialApplication = false;
        this.nextExitStatementResult = 0;
        for (RuntimeIterator arg : functionArguments) {
            if (arg != null) {
                this.children.add(arg);
            } else {
                this.isPartialApplication = true;
            }
        }
        if (!this.children.contains(functionItemIterator)) {
            this.children.add(functionItemIterator);
        }
        this.functionItemIterator = functionItemIterator;
        this.functionArguments = functionArguments;
    }

    @Override
    public void openLocal() {
        setFunctionItemAndIteratorWithCurrentContext(this.currentDynamicContextForLocalExecution);
        try {
            this.functionCallIterator.open(this.currentDynamicContextForLocalExecution);
        } catch (InvalidRumbleMLParamException e) {
            String m = e.getMLMessage();
            throw new InvalidRumbleMLParamException(m, getMetadata());
        } catch (ExitStatementException exitStatementException) {
            this.encounteredExitStatement = true;
            this.exitStatementLocalResult = exitStatementException.getLocalResult();
        }
        setNextResult();
    }

    @Override
    protected boolean hasNextLocal() {
        return this.hasNext;
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

    public void setNextResult() {
        this.nextResult = null;
        if (!this.encounteredExitStatement && this.functionCallIterator.hasNext()) {
            try {
                this.nextResult = this.functionCallIterator.next();
            } catch (InvalidRumbleMLParamException e) {
                String m = e.getMLMessage();
                throw new InvalidRumbleMLParamException(m, getMetadata());
            } catch (ExitStatementException exitStatementException) {
                this.encounteredExitStatement = true;
                this.exitStatementLocalResult = exitStatementException.getLocalResult();
            }
        }
        if (this.encounteredExitStatement) {
            if (this.nextExitStatementResult < this.exitStatementLocalResult.size()) {
                this.nextResult = this.exitStatementLocalResult.get(this.nextExitStatementResult++);
                this.hasNext = true;
            } else {
                this.hasNext = false;
            }
        } else {
            this.hasNext = this.nextResult != null;
        }
    }

    private void setFunctionItemAndIteratorWithCurrentContext(DynamicContext context) {
        try {
            this.functionItem = this.functionItemIterator.materializeAtMostOneItemOrNull(context);
        } catch (MoreThanOneItemException e) {
            throw new UnexpectedTypeException(
                    "A dynamic function call can not be performed on a sequence of more than one item.",
                    getMetadata()
            );
        }
        if (this.functionItem == null || !this.functionItem.isFunction()) {
            throw new UnexpectedTypeException(
                    "Dynamic function calls can only be performed on functions.",
                    getMetadata()
            );
        }
        if (
            this.functionItem.getBodyIterator().getHighestExecutionMode().equals(ExecutionMode.LOCAL)
                && this.getHighestExecutionMode().equals(ExecutionMode.DATAFRAME)
        ) {
            throw new OurBadException(
                    "Execution mode mismatch in dynamic function call. At this point, Rumble only supports higher-order functions "
                        + "that are either machine learning models or estimators (which process validated and structured sequences of objects at any scale), or that take and return just one item at a time.",
                    getMetadata()
            );
        }
        this.functionCallIterator = NamedFunctions.buildUserDefinedFunctionCallIterator(
            this.functionItem,
            getConfiguration(),
            this.isPartialApplication
                ? ExecutionMode.LOCAL
                : this.functionItem.getBodyIterator().getHighestExecutionMode(),
            getMetadata(),
            this.functionArguments
        );
    }

    @Override
    public void resetLocal() {
        this.functionCallIterator.reset(this.currentDynamicContextForLocalExecution);
        setNextResult();
    }

    @Override
    public void closeLocal() {
        // ensure that recursive function calls terminate gracefully
        // the function call in the body of the deepest recursion call is never visited, never opened and never closed
        if (this.isOpen) {
            this.functionCallIterator.close();
        }
        this.nextExitStatementResult = 0;
        this.encounteredExitStatement = false;
    }

    protected boolean implementsDataFrames() {
        return true;
    }

    @Override
    public JavaRDD<Item> getRDDAux(DynamicContext dynamicContext) {
        try {
            setFunctionItemAndIteratorWithCurrentContext(dynamicContext);
            return this.functionCallIterator.getRDD(dynamicContext);
        } catch (ExitStatementException exitStatementException) {
            return exitStatementException.getRddResult();
        }
    }

    @Override
    public JSoundDataFrame getDataFrame(DynamicContext dynamicContext) {
        setFunctionItemAndIteratorWithCurrentContext(dynamicContext);
        try {
            return this.functionCallIterator.getDataFrame(dynamicContext);
        } catch (InvalidRumbleMLParamException e) {
            String m = e.getMLMessage();
            throw new InvalidRumbleMLParamException(m, getMetadata());
        } catch (ExitStatementException exitStatementException) {
            return exitStatementException.getDataFrameResult();
        }
    }
}
