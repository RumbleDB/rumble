/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
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

import org.apache.spark.api.java.function.MapFunction;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.types.StructType;
import sparksoniq.jsoniq.item.Item;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.semantics.DynamicContext;
import sparksoniq.spark.DataFrameUtils;

import java.util.ArrayList;
import java.util.List;

public class LetClauseMapClosure implements MapFunction<Row, Row> {
    private final RuntimeIterator _expression;
    StructType _inputSchema;
    private int _duplicateColumnIndex;

    private List<List<Item>> _rowColumns;
    private DynamicContext _context;
    private List<Item> _newColumn;

    public LetClauseMapClosure(
            RuntimeIterator expression,
            StructType oldSchema,
            int duplicateColumnIndex) {
        this._expression = expression;
        this._inputSchema = oldSchema;
        this._duplicateColumnIndex = duplicateColumnIndex;

        _rowColumns = new ArrayList<>();
        _context = new DynamicContext();
        _newColumn = new ArrayList<>();

    }

    @Override
    public Row call(Row row) throws Exception {
        _rowColumns.clear();
        _context.removeAllVariables();
        _newColumn.clear();

        String[] columnNames = _inputSchema.fieldNames();

        // Deserialize row
        List<Object> deserializedRow = DataFrameUtils.deserializeEntireRow(row);
        for (Object columnObject : deserializedRow) {
            List<Item> column = (List<Item>) columnObject;
            _rowColumns.add(column);
        }

        // Create dynamic context with deserialized data
        for (int columnIndex = 0; columnIndex < columnNames.length; columnIndex++) {
            _context.addVariableValue(columnNames[columnIndex], _rowColumns.get(columnIndex));
        }

        // Apply expression to the context
        _expression.open(_context);
        while (_expression.hasNext()) {
            Item nextItem = _expression.next();
            _newColumn.add(nextItem);
        }
        _expression.close();

        return DataFrameUtils.reserializeRowWithNewData(row, _newColumn, _duplicateColumnIndex);
    }
}
