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

import org.apache.spark.api.java.function.FlatMapFunction;
import sparksoniq.jsoniq.item.Item;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.tuple.FlworTuple;
import sparksoniq.semantics.DynamicContext;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ForClauseClosure implements FlatMapFunction<FlworTuple, FlworTuple> {
    private final String _variableName;
    private final RuntimeIterator _expression;


    public ForClauseClosure(RuntimeIterator expression, String variableName) {
        this._variableName = variableName;
        this._expression = expression;
    }

    @Override
    public Iterator<FlworTuple> call(FlworTuple tuple) throws Exception {
        List<FlworTuple> results = new ArrayList<>();
        List<Item> items = new ArrayList<>();
        _expression.open(new DynamicContext(tuple));
        while(_expression.hasNext())
            items.add(_expression.next());
        _expression.close();
        for(Item result : items){
            List<Item> values = new ArrayList<>();
            values.add(result);
            FlworTuple newTuple = createNewTuple(tuple, _variableName, values);
            results.add(newTuple);

        }
        return results.iterator();
    }

    private FlworTuple createNewTuple(FlworTuple tuple, String newKey, List<Item> value) {
        FlworTuple result = new FlworTuple();
        for(String key: tuple.getKeys())
                result.putValue(key, tuple.getValue(key), true);
        result.putValue(newKey, value, false);
        return result;
    }

}
