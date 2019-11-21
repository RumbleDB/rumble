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
import sparksoniq.exceptions.TreatException;
import sparksoniq.exceptions.UnexpectedTypeException;
import sparksoniq.jsoniq.item.FunctionItem;
import sparksoniq.jsoniq.runtime.iterator.HybridRuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.base.FunctionIdentifier;
import sparksoniq.jsoniq.runtime.iterator.functions.base.FunctionSignature;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.semantics.DynamicContext;
import sparksoniq.semantics.types.SequenceType;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class UserDefinedFunctionCallIterator extends HybridRuntimeIterator {

    private static final long serialVersionUID = 1L;
    // parametrized fields
    private FunctionItem _functionItem;
    private List<RuntimeIterator> _functionArguments;

    // calculated fields
    private boolean _isPartialApplication;
    private FunctionIdentifier _functionIdentifier;
    private RuntimeIterator _functionCallIterator;
    private Item _nextResult;

    public UserDefinedFunctionCallIterator(
            FunctionItem functionItem,
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
        _functionIdentifier = functionItem.getIdentifier();
        _functionArguments = functionArguments;
        _functionItem = functionItem;
    }

    @Override
    public void openLocal() {
        try {
            processArguments();
            _functionCallIterator.open(_currentDynamicContext);
        } catch(TreatException e) {
            String exceptionMessage = e.getJSONiqErrorMessage();
            throw new UnexpectedTypeException("Invalid argument for "
                    + (_functionIdentifier.getName().equals("") ? "inline" :
                    _functionIdentifier.getName()) + " function. " + exceptionMessage, getMetadata());
        }
        setNextResult();
    }

    private void processArguments() {
        RuntimeIterator argIterator;
        String argName;
        Map<String, List<Item>> argumentValues = new LinkedHashMap<>(_functionItem.getNonLocalVariableBindings());

        if (!_isPartialApplication) {
            // calculate argument values
            for (int i = 0; i < _functionArguments.size(); i++) {
                argIterator = _functionArguments.get(i);
                argName = _functionItem.getParameterNames().get(i);

                List<Item> argValue = getItemsFromIteratorWithCurrentContext(argIterator);
                argumentValues.put(argName, argValue);
            }
            // place argument values into dynamic context
            _currentDynamicContext = new DynamicContext(_currentDynamicContext);
            for (Map.Entry<String, List<Item>> argumentEntry : argumentValues.entrySet()) {
                _currentDynamicContext.addVariableValue(
                    "$" + argumentEntry.getKey(),
                    argumentEntry.getValue()
                );
            }
        } else {
            List<String> partialAppParametersNames = new ArrayList<>();
            List<SequenceType> partialAppParameters = new ArrayList<>();

            for (int i = 0; i < _functionArguments.size(); i++) {
                argIterator = _functionArguments.get(i);
                argName = _functionItem.getParameterNames().get(i);

                if (argIterator == null) { // == ArgumentPlaceholder
                    partialAppParametersNames.add(argName);
                    partialAppParameters.add(_functionItem.getSignature().getParameters().get(i));
                } else {
                    List<Item> argValue = getItemsFromIteratorWithCurrentContext(argIterator);
                    argumentValues.put(argName, argValue);
                }
            }

            // partial application should return a new FunctionItem with given parameters set as NonLocalVariables
            // and argument placeholders as new parameters to the new FunctionItem
            FunctionItem partiallyAppliedFunction = new FunctionItem(
                    new FunctionIdentifier("", partialAppParametersNames.size()),
                    partialAppParametersNames,
                    new FunctionSignature(
                            partialAppParameters,
                            _functionItem.getSignature().getReturnType()
                    ),
                    _functionItem.getBodyIterator(),
                    argumentValues
            );
            _functionCallIterator = new FunctionRuntimeIterator(partiallyAppliedFunction, getMetadata());
        }
    }

    @Override
    public Item nextLocal() {
        if (this._hasNext) {
            Item result = _nextResult;
            setNextResult();
            return result;
        }
        throw new IteratorFlowException(
                RuntimeIterator.FLOW_EXCEPTION_MESSAGE + " in " + _functionIdentifier.getName() + "  function",
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
            try {
                _nextResult = _functionCallIterator.next();
            } catch(TreatException e) {
                String exceptionMessage = e.getJSONiqErrorMessage();
                throw new UnexpectedTypeException("Invalid argument for "
                        + (_functionIdentifier.getName().equals("") ? "inline" :
                        _functionIdentifier.getName()) + " function. " + exceptionMessage, getMetadata());
            }
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
        // TODO: how to handle partial function appliacation for RDDs
        JavaRDD<Item> result;
        try {
            processArguments();
            result = _functionCallIterator.getRDD(_currentDynamicContext);
        } catch(TreatException e) {
            String exceptionMessage = e.getJSONiqErrorMessage();
            throw new UnexpectedTypeException("Invalid argument for "
                    + (_functionIdentifier.getName().equals("") ? "inline" :
                    _functionIdentifier.getName()) + " function. " + exceptionMessage, getMetadata());
        }
        return result;
    }

    @Override
    public boolean initIsRDD() {
        if (_isPartialApplication) {
            return false;
        }
        _functionCallIterator = _functionItem.getBodyIterator();
        return _functionCallIterator.isRDD();
    }
}
