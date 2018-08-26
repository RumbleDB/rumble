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
import sparksoniq.jsoniq.compiler.translator.expr.quantifiers.QuantifiedExpression;
import sparksoniq.jsoniq.item.BooleanItem;
import sparksoniq.jsoniq.item.Item;
import sparksoniq.jsoniq.item.metadata.ItemMetadata;
import sparksoniq.jsoniq.runtime.iterator.LocalRuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.semantics.DynamicContext;

import java.util.ArrayList;
import java.util.List;

public class QuantifiedExpressionIterator extends LocalRuntimeIterator {

    private Item result;
    private final QuantifiedExpression.QuantifiedOperators _operator;
    private final RuntimeIterator _evaluationExpression;

    public QuantifiedExpressionIterator(QuantifiedExpression.QuantifiedOperators operator,
                                        List<QuantifiedExpressionVarIterator> children,
                                        RuntimeIterator evaluationExpression, IteratorMetadata iteratorMetadata) {
        super(null, iteratorMetadata);
        this._operator = operator;
        children.forEach(c -> this._children.add(c));
        this._evaluationExpression = evaluationExpression;
        _children.add(_evaluationExpression);
    }

    @Override
    public Item next() {
        if (this.hasNext()) {
            this._hasNext = false;
            return result;
        }
        throw new IteratorFlowException(RuntimeIterator.FLOW_EXCEPTION_MESSAGE + "Quantified Expr", getMetadata());
    }

    @Override
    public void open(DynamicContext context) {
        super.open(context);

        List<DynamicContext> contexts = new ArrayList<>();
        contexts.add(new DynamicContext(_currentDynamicContext));
        for (RuntimeIterator iterator : _children) {
            if (iterator instanceof QuantifiedExpressionVarIterator) {
                QuantifiedExpressionVarIterator var = (QuantifiedExpressionVarIterator) iterator;
                contexts = generateContexts(contexts, var);
            }
        }

        List<BooleanItem> results = new ArrayList<>();
        for (DynamicContext cont : contexts) {
            _evaluationExpression.open(cont);
            _evaluationExpression.reset(cont);
            BooleanItem result = (BooleanItem) _evaluationExpression.next();
            _evaluationExpression.close();
            results.add(result);
        }

        boolean result = this._operator == QuantifiedExpression.QuantifiedOperators.EVERY;
        for (BooleanItem res : results) {
            result = this._operator == QuantifiedExpression.QuantifiedOperators.EVERY ?
                    result && res.getBooleanValue() : result || res.getBooleanValue();
        }
        this.result = new BooleanItem(result, ItemMetadata.fromIteratorMetadata(getMetadata()));
        this._hasNext = true;
    }

    public List<DynamicContext> generateContexts(List<DynamicContext> previousContexts, QuantifiedExpressionVarIterator var) {
        List<DynamicContext> results = new ArrayList<>();
        for (DynamicContext currentContext : previousContexts) {
            var.open(currentContext);
            while (var.hasNext()) {
                DynamicContext context = new DynamicContext(currentContext);
                List<Item> contents = new ArrayList<>();
                Item currentItem = var.next();
                if (currentItem != null) {
                    contents.add(currentItem);
                    context.addVariableValue("$" + var.getVariableReference(), contents);
                    results.add(context);
                }
            }
            var.close();
        }
        return results;
    }
}
