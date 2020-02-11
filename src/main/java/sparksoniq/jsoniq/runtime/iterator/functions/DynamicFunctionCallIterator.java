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

package sparksoniq.jsoniq.runtime.iterator.functions;

import org.rumbledb.api.Item;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import sparksoniq.jsoniq.ExecutionMode;
import sparksoniq.jsoniq.item.FunctionItem;
import sparksoniq.jsoniq.runtime.iterator.LocalRuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.base.Functions;
import sparksoniq.semantics.DynamicContext;

import java.util.List;

public class DynamicFunctionCallIterator extends LocalRuntimeIterator {
    // dynamic: functionIdentifier is not known at compile time
    // it is known only after evaluating postfix expression at runtime

    private static final long serialVersionUID = 1L;
    // parametrized fields
    private RuntimeIterator _functionItemIterator;
    private List<RuntimeIterator> _functionArguments;

    // calculated fields
    private FunctionItem _functionItem;
    private RuntimeIterator _functionCallIterator;
    private Item _nextResult;

    public DynamicFunctionCallIterator(
            RuntimeIterator functionItemIterator,
            List<RuntimeIterator> functionArguments,
            ExecutionMode executionMode,
            ExceptionMetadata iteratorMetadata
    ) {
        super(null, executionMode, iteratorMetadata);
        for (RuntimeIterator arg : functionArguments) {
            if (arg != null) {
                this._children.add(arg);
            }
        }
        if (!this._children.contains(functionItemIterator)) {
            this._children.add(functionItemIterator);
        }
        this._functionItemIterator = functionItemIterator;
        this._functionArguments = functionArguments;
    }

    @Override
    public void open(DynamicContext context) {
        super.open(context);
        setFunctionItemAndIteratorWithCurrentContext();
        this._functionCallIterator.open(this._currentDynamicContextForLocalExecution);
        setNextResult();
    }

    @Override
    public Item next() {
        if (this._hasNext) {
            Item result = this._nextResult;
            setNextResult();
            return result;
        }
        throw new IteratorFlowException(
                RuntimeIterator.FLOW_EXCEPTION_MESSAGE
                    + " in "
                    + this._functionItem.getIdentifier().getName()
                    + "  function",
                getMetadata()
        );
    }

    public void setNextResult() {
        this._nextResult = null;
        if (this._functionCallIterator.hasNext()) {
            this._nextResult = this._functionCallIterator.next();
        }

        if (this._nextResult == null) {
            this._hasNext = false;
            this._functionCallIterator.close();
        } else {
            this._hasNext = true;
        }
    }

    private void setFunctionItemAndIteratorWithCurrentContext() {
        try {
            this._functionItemIterator.open(this._currentDynamicContextForLocalExecution);
            if (this._functionItemIterator.hasNext()) {
                this._functionItem = (FunctionItem) this._functionItemIterator.next();
            }
            if (this._functionItemIterator.hasNext()) {
                throw new UnexpectedTypeException(
                        "Dynamic function call can not be performed on a sequence.",
                        getMetadata()
                );
            }
            this._functionItemIterator.close();
        } catch (ClassCastException e) {
            throw new UnexpectedTypeException(
                    "Dynamic function call can only be performed on functions.",
                    getMetadata()
            );
        }
        this._functionCallIterator = Functions.buildUserDefinedFunctionCallIterator(
            this._functionItem,
            this._functionItem.getBodyIterator().getHighestExecutionMode(),
            getMetadata(),
            this._functionArguments
        );
    }

    @Override
    public void reset(DynamicContext context) {
        super.reset(context);
        this._functionCallIterator.reset(this._currentDynamicContextForLocalExecution);
        setNextResult();
    }

    @Override
    public void close() {
        // ensure that recursive function calls terminate gracefully
        // the function call in the body of the deepest recursion call is never visited, never opened and never closed
        if (this._isOpen) {
            this._functionCallIterator.close();
        }
        super.close();
    }
}
