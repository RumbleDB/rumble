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

package sparksoniq.spark.closures;

import org.apache.spark.api.java.function.Function;
import org.rumbledb.api.Item;

import scala.Tuple2;
import sparksoniq.jsoniq.tuple.FlworKey;
import sparksoniq.jsoniq.tuple.FlworTuple;
import sparksoniq.spark.iterator.flowr.expression.GroupByClauseSparkIteratorExpression;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GroupByLinearizeTupleClosure implements Function<Tuple2<FlworKey, Iterable<FlworTuple>>, FlworTuple> {

    private static final long serialVersionUID = 1L;
    private final List<GroupByClauseSparkIteratorExpression> _groupVariables;

    public GroupByLinearizeTupleClosure(List<GroupByClauseSparkIteratorExpression> groupVariable) {
        this._groupVariables = groupVariable;
    }

    @Override
    public FlworTuple call(Tuple2<FlworKey, Iterable<FlworTuple>> v1) throws Exception {
        Iterator<FlworTuple> iterator = v1._2().iterator();
        FlworTuple oldFirstTuple = iterator.next();
        FlworTuple newTuple = new FlworTuple(oldFirstTuple.getKeys().size());
        for (String tupleVariable : oldFirstTuple.getKeys()) {
            iterator = v1._2().iterator();
            if (
                _groupVariables.stream().anyMatch(v -> v.getVariableReference().getVariableName().equals(tupleVariable))
            )
                newTuple.putValue(tupleVariable, oldFirstTuple.getValue(tupleVariable), false);
            else {
                List<Item> allValues = new ArrayList<>();
                while (iterator.hasNext())
                    allValues.addAll(iterator.next().getValue(tupleVariable));
                newTuple.putValue(tupleVariable, allValues, false);
            }
        }
        return newTuple;
    }
}
