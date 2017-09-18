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
 package jiqs.jsoniq.runtime.iterator.quantifiers;

import jiqs.jsoniq.compiler.translator.expr.quantifiers.QuantifiedExpression;
import jiqs.jsoniq.exceptions.IteratorFlowException;
import jiqs.jsoniq.item.BooleanItem;
import jiqs.jsoniq.item.Item;
import jiqs.jsoniq.runtime.iterator.LocalRuntimeIterator;
import jiqs.jsoniq.runtime.iterator.RuntimeIterator;
import jiqs.semantics.DynamicContext;
import scala.Tuple2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.apache.avro.TypeEnum.c;

public class QuantifiedExpressionIterator extends LocalRuntimeIterator {

    public QuantifiedExpressionIterator(QuantifiedExpression.QuantifiedOperators operator,
                                        List<QuantifiedExpressionVarIterator> children,
                                        RuntimeIterator evaluationExpression) {
        super(null);
        this._operator = operator;
        children.forEach(c -> this._children.add(c));
        this._evaluationExpression = evaluationExpression;
        _children.add(_evaluationExpression);
    }

    @Override
    public Item next() {
        if(this._hasNext) {
            this._hasNext = false;
            List<DynamicContext> contexts = new ArrayList<>();
            contexts.add(new DynamicContext(_currentDynamicContext));
            for(RuntimeIterator iterator: _children){
                if(iterator instanceof QuantifiedExpressionVarIterator){
                    QuantifiedExpressionVarIterator var = (QuantifiedExpressionVarIterator) iterator;
                    contexts = generateContexts(contexts, var);
                }
            }

            List<BooleanItem> results = new ArrayList<>();
            for (DynamicContext context : contexts){
                _evaluationExpression.open(context);
                _evaluationExpression.reset(context);
                BooleanItem result = (BooleanItem) _evaluationExpression.next();
                _evaluationExpression.close();
                results.add(result);
            }

            boolean result = this._operator == QuantifiedExpression.QuantifiedOperators.EVERY ? true : false;
            for (BooleanItem res: results)
                result = this._operator == QuantifiedExpression.QuantifiedOperators.EVERY ?
                        result && res.getBooleanValue() : result || res.getBooleanValue();
            return new BooleanItem(result);
        }
        throw new IteratorFlowException(RuntimeIterator.FLOW_EXCEPTION_MESSAGE + "Quantified Expr");
    }

    public List<DynamicContext> generateContexts(List<DynamicContext> previousContexts, QuantifiedExpressionVarIterator var){
        List<DynamicContext> results = new ArrayList<>();
        for(DynamicContext currentContext : previousContexts){
            var.open(currentContext);
            var.reset(currentContext);
            while (var.hasNext()) {
                DynamicContext context = new DynamicContext(currentContext);
                List<Item> contents = new ArrayList<>();
                Item currentItem = var.next();
                if(currentItem != null){
                    contents.add(currentItem);
                    context.addVariableValue("$" + var.getVariableReference().getVariableName(), contents);
                    results.add(context);
                }
            }
            var.close();
        }
        return results;
    }

    private final QuantifiedExpression.QuantifiedOperators _operator;
    private final RuntimeIterator _evaluationExpression;

}
