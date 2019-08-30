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

import org.apache.spark.api.java.function.Function;
import org.rumbledb.api.Item;

import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.tuple.FlworTuple;
import sparksoniq.semantics.DynamicContext;

import java.util.ArrayList;
import java.util.List;

public class OLD_LetClauseMapClosure implements Function<FlworTuple, FlworTuple> {

	private static final long serialVersionUID = 1L;
	private final String _variableName;
    private final RuntimeIterator _expression;

    public OLD_LetClauseMapClosure(String variableName, RuntimeIterator expression) {
        this._expression = expression;
        this._variableName = variableName;
    }
    @Override
    public FlworTuple call(FlworTuple v1) throws Exception {
        List<Item> result = new ArrayList<>();
        _expression.open(new DynamicContext(v1));
        while (_expression.hasNext())
            result.add(_expression.next());
        _expression.close();
        v1.putValue(_variableName, result, true);
        return v1;
    }
}
