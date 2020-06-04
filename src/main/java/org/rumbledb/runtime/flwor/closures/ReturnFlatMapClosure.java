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

package org.rumbledb.runtime.flwor.closures;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.types.StructType;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.flwor.FlworDataFrameUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class ReturnFlatMapClosure implements FlatMapFunction<Row, Item> {

    private static final long serialVersionUID = 1L;
    private RuntimeIterator expression;
    private StructType oldSchema;
    private DynamicContext parentContext;
    private DynamicContext context;

    private transient Kryo kryo;
    private transient Input input;

    public ReturnFlatMapClosure(RuntimeIterator expression, DynamicContext context, StructType oldSchema) {
        this.expression = expression;
        this.oldSchema = oldSchema;
        this.parentContext = context;
        this.context = new DynamicContext(this.parentContext);

        this.kryo = new Kryo();
        this.kryo.setReferences(false);
        FlworDataFrameUtils.registerKryoClassesKryo(this.kryo);
        this.input = new Input();
    }

    @Override
    public Iterator<Item> call(Row row) {
        String[] columnNames = this.oldSchema.fieldNames();
        Map<Name, DynamicContext.VariableDependency> dependencies = this.expression
            .getVariableDependencies();
        this.context.removeAllVariables();
        // Create dynamic context with deserialized data but only with dependencies
        for (int columnIndex = 0; columnIndex < columnNames.length; columnIndex++) {
            Name field = Name.createVariableInNoNamespace(columnNames[columnIndex]);
            if (dependencies.containsKey(field)) {
                List<Item> i = FlworDataFrameUtils.deserializeRowField(row, columnIndex, this.kryo, this.input); // rowColumns.get(columnIndex);
                if (dependencies.get(field).equals(DynamicContext.VariableDependency.COUNT)) {
                    this.context.addVariableCount(field, i.get(0));
                } else {
                    this.context.addVariableValue(field, i);
                }
            }
        }

        // Apply expression to the context
        List<Item> results = new ArrayList<>();
        this.expression.open(this.context);
        while (this.expression.hasNext()) {
            results.add(this.expression.next());
        }
        this.expression.close();

        return results.iterator();
    }

    private void readObject(java.io.ObjectInputStream in)
            throws IOException,
                ClassNotFoundException {
        in.defaultReadObject();

        this.kryo = new Kryo();
        this.kryo.setReferences(false);
        FlworDataFrameUtils.registerKryoClassesKryo(this.kryo);
        this.input = new Input();
    }
}
