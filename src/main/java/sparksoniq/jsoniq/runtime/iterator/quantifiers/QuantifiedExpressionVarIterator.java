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
 * Authors: Stefan Irimescu, Can Berker Cikis
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

public class QuantifiedExpressionVarIterator extends LocalRuntimeIterator {

    private final String _variableReference;
    private final SequenceType _sequenceType;
    private RuntimeIterator _iterator;
    private Item _nextResult;

    /*private List<Item> result = null;
    private int currentResultIndex;*/

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
    public void open(DynamicContext context) {
        super.open(context);

        _iterator = this._children.get(0);
        _iterator.open(_currentDynamicContext);

        setNextResult();
    }

    @Override
    public Item next() {
        if(_hasNext == true){
            Item result = _nextResult;  // save the result to be returned
            setNextResult();            // calculate and store the next result
            return result;
        }
        throw new IteratorFlowException("Invalid next() call in QuantifiedExpressionVar", getMetadata());
    }

    public void setNextResult() {
        _nextResult = null;

        while (_iterator.hasNext()) {
            _nextResult = _iterator.next();
            break;
        }


        if (_nextResult == null) {
            this._hasNext = false;
            _iterator.close();
        } else {
            this._hasNext = true;
        }
    }
}
