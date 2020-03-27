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

package org.rumbledb.runtime.quantifiers;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.expressions.quantifiers.QuantifiedExpression;
import org.rumbledb.items.BooleanItem;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.LocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import sparksoniq.jsoniq.ExecutionMode;

import java.util.ArrayList;
import java.util.List;

public class QuantifiedExpressionIterator extends LocalRuntimeIterator {


    private static final long serialVersionUID = 1L;
    private final QuantifiedExpression.QuantifiedOperators operator;
    private final RuntimeIterator evaluationExpression;

    public QuantifiedExpressionIterator(
            QuantifiedExpression.QuantifiedOperators operator,
            List<QuantifiedExpressionVarIterator> children,
            RuntimeIterator evaluationExpression,
            ExecutionMode executionMode,
            ExceptionMetadata iteratorMetadata
    ) {
        super(null, executionMode, iteratorMetadata);
        this.operator = operator;
        this.children.addAll(children);
        this.evaluationExpression = evaluationExpression;
        this.children.add(this.evaluationExpression);
    }

    @Override
    public Item next() {
        if (this.hasNext) {
            this.hasNext = false;
            List<DynamicContext> contexts = new ArrayList<>();
            contexts.add(new DynamicContext(this.currentDynamicContextForLocalExecution));
            for (RuntimeIterator iterator : this.children) {
                if (iterator instanceof QuantifiedExpressionVarIterator) {
                    QuantifiedExpressionVarIterator var = (QuantifiedExpressionVarIterator) iterator;
                    contexts = generateContexts(contexts, var);
                }
            }

            List<BooleanItem> results = new ArrayList<>();
            for (DynamicContext context : contexts) {
                this.evaluationExpression.open(context);
                BooleanItem result = (BooleanItem) this.evaluationExpression.next();
                this.evaluationExpression.close();
                results.add(result);
            }

            boolean result = this.operator == QuantifiedExpression.QuantifiedOperators.EVERY;
            for (BooleanItem res : results) {
                result = this.operator == QuantifiedExpression.QuantifiedOperators.EVERY
                    ? result && res.getBooleanValue()
                    : result || res.getBooleanValue();
            }
            return ItemFactory.getInstance().createBooleanItem(result);
        }
        throw new IteratorFlowException(RuntimeIterator.FLOW_EXCEPTION_MESSAGE + "Quantified Expr", getMetadata());
    }

    public List<DynamicContext> generateContexts(
            List<DynamicContext> previousContexts,
            QuantifiedExpressionVarIterator var
    ) {
        List<DynamicContext> results = new ArrayList<>();
        for (DynamicContext currentContext : previousContexts) {
            var.open(currentContext);
            while (var.hasNext()) {
                DynamicContext context = new DynamicContext(currentContext);
                List<Item> contents = new ArrayList<>();
                Item currentItem = var.next();
                if (currentItem != null) {
                    contents.add(currentItem);
                    context.addVariableValue(var.getVariableReference(), contents);
                    results.add(context);
                }
            }
            var.close();
        }
        return results;
    }

}
