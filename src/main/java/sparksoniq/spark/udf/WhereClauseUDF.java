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
import sparksoniq.jsoniq.item.BooleanItem;
import sparksoniq.jsoniq.item.Item;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.semantics.DynamicContext;
import sparksoniq.spark.DataFrameUtils;

import java.util.ArrayList;
import java.util.List;

public class WhereClauseUDF implements UDF1<WrappedArray, Boolean> {
    private RuntimeIterator _expression;
    private StructType _inputSchema;

    private List<List<Item>> _deserializedParams;
    private DynamicContext _context;

    public WhereClauseUDF(
            RuntimeIterator expression,
            StructType inputSchema) {
        _expression = expression;
        _inputSchema = inputSchema;

        _deserializedParams = new ArrayList<>();
        _context = new DynamicContext();
    }


    @Override
    public Boolean call(WrappedArray wrappedParameters) {
        _deserializedParams.clear();
        _context.removeAllVariables();

        DataFrameUtils.deserializeWrappedParameters(wrappedParameters, _deserializedParams);

        String[] columnNames = _inputSchema.fieldNames();

        // prepare dynamic context
        for (int columnIndex = 0; columnIndex < columnNames.length; columnIndex++) {
            _context.addVariableValue(columnNames[columnIndex], _deserializedParams.get(columnIndex));
        }

        // apply expression in the dynamic context
        _expression.open(_context);
        Item nextItem = _expression.next();
        _expression.close();

        return ((BooleanItem) nextItem).getBooleanValue();
    }
}
