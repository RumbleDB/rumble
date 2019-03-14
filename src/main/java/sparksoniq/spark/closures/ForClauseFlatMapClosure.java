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
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.MapFunction;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.types.StructType;
import sparksoniq.jsoniq.item.Item;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.semantics.DynamicContext;
import sparksoniq.spark.SparkSessionManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ForClauseFlatMapClosure implements FlatMapFunction<Row, Row> {
    RuntimeIterator _expression;

    public ForClauseFlatMapClosure(RuntimeIterator expression) {
        this._expression = expression;
    }

    @Override
    public Iterator<Row> call(Row row) {
        StructType schema = row.schema();
        String[] columnNames = schema.fieldNames();

        // Deserialize row
        List<Object> deserializedRow = ClosureUtils.deserializeEntireRow(row);
        List<List<Item>> rowColumns = new ArrayList<>();
        for (Object columnObject:deserializedRow) {
            List<Item> column = (List<Item>) columnObject;
            rowColumns.add(column);
        }

        // Create dynamic context with deserialized data
        DynamicContext context = new DynamicContext();
        for (int columnIndex = 0; columnIndex < columnNames.length; columnIndex++) {
            context.addVariableValue(columnNames[columnIndex], rowColumns.get(columnIndex));
        }

        // Apply expression to the context
        List<Row> results = new ArrayList<>();
        _expression.open(context);
        while (_expression.hasNext()) {
            Item nextItem = _expression.next();
            List<Item> newColumn = new ArrayList<>();
            newColumn.add(nextItem);

            List<byte[]> newRowColumns = new ArrayList<>();
            for (List<Item> column:rowColumns) {
                newRowColumns.add(ClosureUtils.serializeItemList(column));
            }
            newRowColumns.add(ClosureUtils.serializeItemList(newColumn));

            results.add(RowFactory.create(newRowColumns.toArray()));
        }
        _expression.close();

        return results.iterator();
    }
}
