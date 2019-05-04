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
package sparksoniq.spark.udf;

import org.apache.spark.sql.api.java.UDF1;
import org.apache.spark.sql.types.StructType;
import scala.collection.mutable.WrappedArray;
import sparksoniq.jsoniq.item.Item;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.semantics.DynamicContext;
import sparksoniq.spark.closures.ClosureUtils;

import java.util.ArrayList;
import java.util.List;

public class ForClauseUDF implements UDF1<WrappedArray, List> {
    private RuntimeIterator _expression;
    private StructType _inputSchema;

    private List<List<Item>> _deserializedParams;
    private DynamicContext _context;
    private List<Item> _nextResult;
    private List<byte[]> _results;

    public ForClauseUDF(
            RuntimeIterator expression,
            StructType inputSchema) {
        _expression = expression;
        _inputSchema = inputSchema;

        _deserializedParams = new ArrayList<>();
        _context = new DynamicContext();
        _nextResult = new ArrayList<>();
        _results = new ArrayList<>();
    }


    @Override
    public List call(WrappedArray wrappedParameters) {
        _deserializedParams.clear();
        _context.removeAllVariables();
        _results.clear();

        _deserializedParams = ClosureUtils.deserializeWrappedParameters(wrappedParameters, _deserializedParams);

        String[] columnNames = _inputSchema.fieldNames();

        // prepare dynamic context
        for (int columnIndex = 0; columnIndex < columnNames.length; columnIndex++) {
            _context.addVariableValue(columnNames[columnIndex], _deserializedParams.get(columnIndex));
        }

        // apply expression in the dynamic context
        _expression.open(_context);
        while (_expression.hasNext()) {
            _nextResult.clear();
            Item nextItem = _expression.next();
            _nextResult.add(nextItem);
            _results.add(ClosureUtils.serializeItemList(_nextResult));
        }
        _expression.close();

        return _results;
    }
}
