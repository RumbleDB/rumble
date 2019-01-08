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
package sparksoniq.spark.closures;

import org.apache.spark.api.java.function.PairFunction;
import scala.Tuple2;
import sparksoniq.exceptions.InvalidGroupVariableException;
import sparksoniq.exceptions.NonAtomicKeyException;
import sparksoniq.jsoniq.item.Item;
import sparksoniq.jsoniq.tuple.FlworKey;
import sparksoniq.jsoniq.tuple.FlworTuple;
import sparksoniq.semantics.DynamicContext;
import sparksoniq.spark.iterator.flowr.expression.GroupByClauseSparkIteratorExpression;

import java.util.ArrayList;
import java.util.List;

public class GroupByToPairMapClosure implements PairFunction<FlworTuple, FlworKey, FlworTuple> {

    private final List<GroupByClauseSparkIteratorExpression> _groupVariables;

    public GroupByToPairMapClosure(List<GroupByClauseSparkIteratorExpression> variables) {
        this._groupVariables = variables;
    }

    @Override
    public Tuple2<FlworKey, FlworTuple> call(FlworTuple tuple) {
        //if a new variable is declared inside the group by clause, insert value in tuple
        List<Item> results = new ArrayList<>();
        for (GroupByClauseSparkIteratorExpression _groupVariable : _groupVariables) {
            if (_groupVariable.getExpression() != null) {
                if (tuple.contains(_groupVariable.getVariableReference().getVariableName()))
                    throw new InvalidGroupVariableException("Group by variable redeclaration is illegal",
                            _groupVariable.getIteratorMetadata());
                List<Item> newVariableResults = new ArrayList<>();
                _groupVariable.getExpression().open(new DynamicContext(tuple));
                while (_groupVariable.getExpression().hasNext()) {
                    Item resultItem = _groupVariable.getExpression().next();
                    if (!Item.isAtomic(resultItem))
                        throw new NonAtomicKeyException("Group by keys must be atomics",
                                _groupVariable.getIteratorMetadata().getExpressionMetadata());
                    newVariableResults.add(resultItem);
                }
                _groupVariable.getExpression().close();
                tuple.putValue(_groupVariable.getVariableReference().getVariableName(), newVariableResults,
                        false);
                results.addAll(newVariableResults);
            } else {
                if (!tuple.contains(_groupVariable.getVariableReference().getVariableName()))
                    throw new InvalidGroupVariableException("Variable " +
                            _groupVariable.getVariableReference().getVariableName() +
                            " cannot be used in group clause", _groupVariable.getIteratorMetadata());
                _groupVariable.getVariableReference().open(new DynamicContext(tuple));
                while (_groupVariable.getVariableReference().hasNext())
                    results.add(_groupVariable.getVariableReference().next());
                _groupVariable.getVariableReference().close();
            }
        }
        return new Tuple2<>(new FlworKey(results), tuple);
    }
}
