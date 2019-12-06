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

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.types.StructType;
import org.rumbledb.api.Item;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.semantics.DynamicContext;
import sparksoniq.spark.DataFrameUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class ReturnFlatMapClosure implements FlatMapFunction<Row, Item> {

    private static final long serialVersionUID = 1L;
    private RuntimeIterator _expression;
    private StructType _oldSchema;
    private DynamicContext _parentContext;
    private DynamicContext _context;

    private transient Kryo _kryo;
    private transient Input _input;

    public ReturnFlatMapClosure(RuntimeIterator expression, DynamicContext context, StructType oldSchema) {
        this._expression = expression;
        this._oldSchema = oldSchema;
        _parentContext = context;
        _context = new DynamicContext(_parentContext);

        _kryo = new Kryo();
        _kryo.setReferences(false);
        DataFrameUtils.registerKryoClassesKryo(_kryo);
        _input = new Input();
    }

    @Override
    public Iterator<Item> call(Row row) {
        String[] columnNames = _oldSchema.fieldNames();
        Map<String, DynamicContext.VariableDependency> dependencies = _expression.getVariableDependencies();
        _context.removeAllVariables();
        // Create dynamic context with deserialized data but only with dependencies
        for (int columnIndex = 0; columnIndex < columnNames.length; columnIndex++) {
            String field = columnNames[columnIndex];
            if (dependencies.containsKey(field)) {
                List<Item> i = DataFrameUtils.deserializeRowField(row, columnIndex, _kryo, _input); // rowColumns.get(columnIndex);
                if (dependencies.get(field).equals(DynamicContext.VariableDependency.COUNT)) {
                    _context.addVariableCount(field, i.get(0));
                } else {
                    _context.addVariableValue(field, i);
                }
            }
        }

        // Apply expression to the context
        List<Item> results = new ArrayList<>();
        _expression.open(_context);
        while (_expression.hasNext()) {
            results.add(_expression.next());
        }
        _expression.close();

        return results.iterator();
    }

    private void readObject(java.io.ObjectInputStream in)
            throws IOException,
                ClassNotFoundException {
        in.defaultReadObject();

        _kryo = new Kryo();
        _kryo.setReferences(false);
        DataFrameUtils.registerKryoClassesKryo(_kryo);
        _input = new Input();
    }
}
