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
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.flwor.FlworDataFrameUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class ReturnFlatMapClosure implements FlatMapFunction<Row, Item> {

    private static final long serialVersionUID = 1L;
    private Map<String, List<String>> columnNamesByType;
    private List<Name> serializedVariableNames;
    private List<Name> countedVariableNames;
    private List<List<Item>> deserializedParams;
    private RuntimeIterator expression;
    private List<Item> longParams;
    private DynamicContext context;

    private transient Kryo kryo;
    private transient Input input;

    public ReturnFlatMapClosure(
            RuntimeIterator expression,
            DynamicContext context,
            Map<String, List<String>> columnNamesByType
    ) {
        this.expression = expression;
        this.columnNamesByType = columnNamesByType;
        List<String> serializedColumNames = this.columnNamesByType.get("byte[]");
        this.serializedVariableNames = new ArrayList<>(serializedColumNames.size());
        for (String columnName : serializedColumNames) {
            this.serializedVariableNames.add(Name.createVariableInNoNamespace(columnName));
        }
        List<String> countedColumNames = this.columnNamesByType.get("Long");
        this.countedVariableNames = new ArrayList<>(countedColumNames.size());
        for (String columnName : countedColumNames) {
            this.countedVariableNames.add(Name.createVariableInNoNamespace(columnName));
        }

        this.deserializedParams = new ArrayList<>();
        this.longParams = new ArrayList<>();

        this.context = new DynamicContext(context);

        this.kryo = new Kryo();
        this.kryo.setReferences(false);
        FlworDataFrameUtils.registerKryoClassesKryo(this.kryo);
        this.input = new Input();
    }

    @Override
    public Iterator<Item> call(Row row) {
        this.deserializedParams.clear();

        Map<Name, DynamicContext.VariableDependency> dependencies = this.expression
            .getVariableDependencies();
        this.context.getVariableValues().removeAllVariables();
        // Create dynamic context with deserialized data but only with dependencies
        for (Name field : this.serializedVariableNames) {
            if (dependencies.containsKey(field)) {
                int columnIndex = row.fieldIndex(field.getLocalName());
                List<Item> i = FlworDataFrameUtils.deserializeRowField(row, columnIndex, this.kryo, this.input); // rowColumns.get(columnIndex);
                this.deserializedParams.add(i);
            }
        }
        for (Name field : this.countedVariableNames) {
            if (dependencies.containsKey(field)) {
                int columnIndex = row.fieldIndex(field.getLocalName());
                long count = FlworDataFrameUtils.getCountOfField(row, columnIndex);
                Item i = ItemFactory.getInstance().createLongItem(count);
                this.longParams.add(i);
            }
        }

        FlworDataFrameUtils.prepareDynamicContext(
            this.context,
            this.serializedVariableNames,
            this.countedVariableNames,
            this.deserializedParams,
            this.longParams
        );



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
