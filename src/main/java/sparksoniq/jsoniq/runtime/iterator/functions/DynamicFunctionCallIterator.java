/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
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
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.semantics.DynamicContext;
import sparksoniq.semantics.types.SequenceType;
import sparksoniq.semantics.visitor.RuntimeIteratorVisitor;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DynamicFunctionCallIterator extends HybridRuntimeIterator {

    private static final long serialVersionUID = 1L;
    private RuntimeIterator _fnItemIterator;
    private List<RuntimeIterator> _fnArguments;

    private FunctionItem _fnItem;
    private RuntimeIterator _fnCallIterator;

    private Item _nextResult;

    public DynamicFunctionCallIterator(
            RuntimeIterator fnItemIterator,
            List<RuntimeIterator> fnArguments,
            IteratorMetadata iteratorMetadata) {
        super(fnArguments, iteratorMetadata);
        if (!_children.contains(fnItemIterator)) {
            _children.add(fnItemIterator);
        }
        _fnItemIterator = fnItemIterator;
        _fnArguments = fnArguments;
    }

    @Override
    public void openLocal() {
        processArguments();
        _fnCallIterator.open(_currentDynamicContext);
        setNextResult();
    }

    private void processArguments() {
        if (_fnItem.getParameterNames().size() != _fnArguments.size()) {
            String formattedName = (!_fnItem.getIdentifier().getName().equals(""))
                    ? _fnItem.getIdentifier().getName() + " "
                    : "";
            throw new UnexpectedTypeException(
                    "Dynamic function " + formattedName
                            + "invoked with incorrect number of arguments. Expected: " + _fnItem.getParameterNames().size()
                            + ", Found: " + _fnArguments.size()
                    , getMetadata()
            );
        }

        boolean isPartialApplication = false;
        List<String> partialAppParamNames = null;
        List<SequenceType> partialAppSignature = null;

        RuntimeIterator argIterator;
        String argName;
        Map<String, List<Item>> argumentValues = new LinkedHashMap<>(
                _fnItem.getNonLocalVariableBindings()
        );

        for (int i = 0; i < _fnArguments.size(); i++) {
            argIterator = _fnArguments.get(i);
            argName = _fnItem.getParameterNames().get(i);

            if (argIterator == null) {  // check ArgumentPlaceholder
                isPartialApplication = true;
                if (partialAppParamNames == null) {
                    partialAppParamNames = new ArrayList<>();
                }
                if (partialAppSignature == null) {
                    partialAppSignature = new ArrayList<>();
                }
                partialAppParamNames.add(argName);
                partialAppSignature.add(_fnItem.getSignature().get(i));
            } else {
                List<Item> argValue = getItemsFromIteratorWithCurrentContext(argIterator);
                argumentValues.put(argName, argValue);
            }
        }

        // partial application should return a new FunctionItem with given parameters set as NonLocalVariables
        // and argument placeholders as new parameters to the new FunctionItem
        if (isPartialApplication) {
            partialAppSignature.add(_fnItem.getSignature().get(_fnItem.getSignature().size()-1));   // add return type

            FunctionItem partiallyAppliedFunction = new FunctionItem(
                    new FunctionIdentifier("", partialAppParamNames.size()),
                    partialAppParamNames,
                    partialAppSignature,
                    _fnItem.getBodyExpression(),
                    argumentValues
            );
            _fnCallIterator = new FunctionItemIterator(partiallyAppliedFunction, getMetadata());
        } else {
            _currentDynamicContext = new DynamicContext(_currentDynamicContext);
            for (Map.Entry<String, List<Item>> argumentEntry : argumentValues.entrySet()) {
                _currentDynamicContext.addVariableValue(
                        "$" + argumentEntry.getKey(),
                        argumentEntry.getValue()
                );
            }
        }
    }

    @Override
    public Item nextLocal() {
        if (this._hasNext) {
            Item result = _nextResult;
            setNextResult();
            return result;
        }
        throw new IteratorFlowException(RuntimeIterator.FLOW_EXCEPTION_MESSAGE + " in "
                + _fnItem.getIdentifier().getName() + "  function",
                getMetadata());
    }

    @Override
    protected boolean hasNextLocal() {
        return _hasNext;
    }

    @Override
    protected void resetLocal(DynamicContext context) {
        _fnCallIterator.reset(_currentDynamicContext);
        setNextResult();
    }

    @Override
    protected void closeLocal() {
        // ensure that recursive function calls terminate gracefully
        // the function call in the body of the deepest recursion call is never visited, never opened and never closed
        if (this.isOpen()) {
            _fnCallIterator.close();
        }
    }

    public void setNextResult() {
        _nextResult = null;
        if (_fnCallIterator.hasNext()) {
            _nextResult = _fnCallIterator.next();
        }

        if (_nextResult == null) {
            this._hasNext = false;
            _fnCallIterator.close();
        } else {
            this._hasNext = true;
        }
    }

    @Override
    public JavaRDD<Item> getRDD(DynamicContext dynamicContext) {
        // TODO: how to handle partial function appliacation for RDDs
        processArguments();
        return _fnCallIterator.getRDD(_currentDynamicContext);
    }

    @Override
    public boolean initIsRDD() {
        initializeFunctionCallIterator();
        return _fnCallIterator.isRDD();
    }

    private void initializeFunctionCallIterator() {
        try {
            _fnItem = getSingleItemOfTypeFromIterator(_fnItemIterator, FunctionItem.class, new UnexpectedTypeException(
                    "Dynamic function call can not be performed on a sequence."
                    , getMetadata()
            ));
            _fnCallIterator = new RuntimeIteratorVisitor().visit(_fnItem.getBodyExpression(), null);
        } catch (UnexpectedTypeException e) {
            throw new UnexpectedTypeException(
                    "Dynamic function call can only be performed on functions."
                    , getMetadata()
            );
        }
    }
}
