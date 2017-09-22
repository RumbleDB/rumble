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

import sparksoniq.jsoniq.compiler.translator.expr.primary.VariableReference;
import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.jsoniq.item.Item;
import sparksoniq.jsoniq.runtime.iterator.LocalRuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.semantics.DynamicContext;
import sparksoniq.semantics.types.SequenceType;

import java.util.ArrayList;
import java.util.List;

public class QuantifiedExpressionVarIterator extends LocalRuntimeIterator {

    public VariableReference getVariableReference() {
        return _variableReference;
    }

    public QuantifiedExpressionVarIterator(VariableReference variableReference, SequenceType sequenceType,
                                           RuntimeIterator expression) {
        super(null);
        this._children.add(expression);
        this._variableReference = variableReference;
        this._variableReference.setType(sequenceType);
    }

    @Override
    public void reset(DynamicContext context){
        super.reset(context);
        this.result = null;
    }

    @Override
    public Item next() {
        if(result == null){
            RuntimeIterator expression = this._children.get(0);
            result = new ArrayList<>();
            expression.open(_currentDynamicContext);
            while (expression.hasNext())
                result.add(expression.next());
            expression.close();
            currentResultIndex = 0;
        }

        if(currentResultIndex > result.size() -1)
            throw new IteratorFlowException(RuntimeIterator.FLOW_EXCEPTION_MESSAGE + " Quantified Expr Var");

        if(currentResultIndex == result.size() -1)
            this._hasNext = false;

        return result.get(currentResultIndex++);
    }

    private List<Item> result = null;
    private int currentResultIndex;
    private final VariableReference _variableReference;

}
