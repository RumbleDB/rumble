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
 package sparksoniq.spark.closures;

import org.apache.spark.api.java.function.Function;
import sparksoniq.jsoniq.item.Item;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.tuple.FlworTuple;
import sparksoniq.semantics.DynamicContext;

public class WhereClauseClosure implements Function<FlworTuple, Boolean> {
    private final RuntimeIterator _expression;

    public WhereClauseClosure(RuntimeIterator expression) {
        this._expression = expression;
    }

    @Override
    public Boolean call(FlworTuple v1) throws Exception {
        _expression.open(new DynamicContext(v1));
        boolean effectiveBooleanValue = RuntimeIterator.getEffectiveBooleanValue(_expression);
        _expression.close();
        return effectiveBooleanValue;
    }
}
