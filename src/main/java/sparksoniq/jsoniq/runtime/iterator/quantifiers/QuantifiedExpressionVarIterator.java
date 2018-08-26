/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Author: Stefan Irimescu
 *
 */
 package sparksoniq.jsoniq.runtime.iterator.quantifiers;

import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.jsoniq.item.Item;
import sparksoniq.jsoniq.runtime.iterator.LocalRuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.semantics.DynamicContext;
import sparksoniq.semantics.types.SequenceType;

import java.util.ArrayList;
import java.util.List;

public class QuantifiedExpressionVarIterator extends LocalRuntimeIterator {


    public String getVariableReference() {
        return _variableReference;
    }

    public QuantifiedExpressionVarIterator(String variableReference, SequenceType sequenceType,
                                           RuntimeIterator expression, IteratorMetadata iteratorMetadata) {
        super(null, iteratorMetadata);
        this._children.add(expression);
        this._variableReference = variableReference;
        this._sequenceType = sequenceType;
    }

    @Override
    public void reset(DynamicContext context){
        super.reset(context);
        this.results = null;
    }

    @Override
    public Item next() {
        if (this.hasNext()) {
            return getResult();
        }
        throw new IteratorFlowException(RuntimeIterator.FLOW_EXCEPTION_MESSAGE + " Quantified Expr Var", getMetadata());
    }

    protected Item getResult() {
        if (_currentIndex == results.size() - 1)
            _hasNext = false;
        return results.get(_currentIndex++);
    }

    @Override
    public void open(DynamicContext context) {
        if (this._isOpen)
            throw new IteratorFlowException("Runtime iterator cannot be opened twice", getMetadata());
        this._isOpen = true;
        this._currentDynamicContext = context;

        RuntimeIterator expression = this._children.get(0);
        results = new ArrayList<>();
        expression.open(_currentDynamicContext);
        while (expression.hasNext())
            results.add(expression.next());
        expression.close();
        _currentIndex = 0;

        if (results.size() == 0) {
            this._hasNext = false;
        } else {
            this._hasNext = true;
        }
    }

    private List<Item> results;
    private int _currentIndex;
    private final String _variableReference;
    private final SequenceType _sequenceType;

}
