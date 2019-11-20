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

import org.apache.spark.api.java.JavaRDD;
import org.rumbledb.api.Item;
import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.exceptions.UnexpectedTypeException;
import sparksoniq.jsoniq.item.FunctionItem;
import sparksoniq.jsoniq.runtime.iterator.HybridRuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.base.FunctionIdentifier;
import sparksoniq.jsoniq.runtime.iterator.functions.base.Functions;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.semantics.DynamicContext;
import sparksoniq.semantics.types.SequenceType;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DynamicFunctionCallIterator extends HybridRuntimeIterator {

    private static final long serialVersionUID = 1L;
    // parametrized fields
    private RuntimeIterator _functionItemIterator;
    private List<RuntimeIterator> _functionArguments;

    // calculated fields
    private boolean _isPartialApplication;
    private FunctionItem _functionItem;
    private RuntimeIterator _functionCallIterator;
    private Item _nextResult;

    public DynamicFunctionCallIterator(
            RuntimeIterator functionItemIterator,
            List<RuntimeIterator> functionArguments,
            IteratorMetadata iteratorMetadata
    ) {
        super(null, iteratorMetadata);
        for (RuntimeIterator arg : functionArguments) {
            if (arg == null) {
                _isPartialApplication = true;
            } else {
                _children.add(arg);
            }
        }
        if (!_children.contains(functionItemIterator)) {
            _children.add(functionItemIterator);
        }
        _functionItemIterator = functionItemIterator;
        _functionArguments = functionArguments;
    }

    @Override
    public void openLocal() {
        setFunctionItemAndIteratorWithCurrentContext();
        _functionCallIterator.open(_currentDynamicContext);
        setNextResult();
    }

    @Override
    public Item nextLocal() {
        if (this._hasNext) {
            Item result = _nextResult;
            setNextResult();
            return result;
        }
        throw new IteratorFlowException(
                RuntimeIterator.FLOW_EXCEPTION_MESSAGE
                    + " in "
                    + _functionItem.getIdentifier().getName()
                    + "  function",
                getMetadata()
        );
    }

    @Override
    protected boolean hasNextLocal() {
        return _hasNext;
    }

    @Override
    protected void resetLocal(DynamicContext context) {
        _functionCallIterator.reset(_currentDynamicContext);
        setNextResult();
    }

    @Override
    protected void closeLocal() {
        // ensure that recursive function calls terminate gracefully
        // the function call in the body of the deepest recursion call is never visited, never opened and never closed
        if (this.isOpen()) {
            _functionCallIterator.close();
        }
    }

    public void setNextResult() {
        _nextResult = null;
        if (_functionCallIterator.hasNext()) {
            _nextResult = _functionCallIterator.next();
        }

        if (_nextResult == null) {
            this._hasNext = false;
            _functionCallIterator.close();
        } else {
            this._hasNext = true;
        }
    }

    @Override
    public JavaRDD<Item> getRDDAux(DynamicContext dynamicContext) {
        setFunctionItemAndIteratorWithCurrentContext();
        return _functionCallIterator.getRDD(_currentDynamicContext);
    }

    @Override
    public boolean initIsRDD() {
        if (_isPartialApplication) {
            return false;
        }

        setFunctionItemAndIteratorWithCurrentContext();
        return _functionCallIterator.isRDD();
    }

    private void setFunctionItemAndIteratorWithCurrentContext() {
        try {
            _functionItem = getSingleItemOfTypeFromIterator(
                _functionItemIterator,
                FunctionItem.class,
                new UnexpectedTypeException(
                        "Dynamic function call can not be performed on a sequence.",
                        getMetadata()
                )
            );
        } catch (UnexpectedTypeException e) {
            throw new UnexpectedTypeException(
                    "Dynamic function call can only be performed on functions.",
                    getMetadata()
            );
        }
        _functionCallIterator = Functions.buildUserDefinedFunctionIterator(
            _functionItem,
            getMetadata(),
            _functionArguments
        );
    }
}
