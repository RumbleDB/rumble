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
 * Authors: Stefan Irimescu, Can Berker Cikis, Ghislain Fourny
 *
 */

package org.rumbledb.runtime.flwor.udfs;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import org.apache.spark.sql.Row;
import org.apache.spark.sql.types.ArrayType;
import org.apache.spark.sql.types.DataType;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructType;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.items.parsing.ItemParser;
import org.rumbledb.runtime.flwor.FlworDataFrameColumn;
import org.rumbledb.runtime.flwor.FlworDataFrameUtils;
import org.rumbledb.types.ItemType;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class exposes a reusable context that is dynamically populated from the input tuples stored in DataFrames.
 * It also pools Kryo objects, inputs and outputs for better performance.
 * This class is meant to be used by various FLWOR clauses to get a context in which to evaluate their expressions.
 * 
 * @author Ghislain Fourny
 *
 */
public class DataFrameContext implements Serializable {

    private static final long serialVersionUID = 1L;
    private List<FlworDataFrameColumn> columns;
    private DynamicContext context;

    private transient Kryo kryo;
    private transient Output output;
    private transient Input input;

    /**
     * Builds a new data frame context that only serves to pool Kryo objects.
     * The only allowed methods are getKryo, getInput and getOutput.
     */
    public DataFrameContext() {
        this.kryo = new Kryo();
        this.kryo.setReferences(false);
        FlworDataFrameUtils.registerKryoClassesKryo(this.kryo);
        this.output = new Output(128, -1);
        this.input = new Input();
    }

    /**
     * Builds a new data frame context.
     * 
     * @param context the parent dynamic context, which contains all variable values except those in input tuples.
     * @param schema the schema.
     * @param columnNames the names of the DataFrame column names applicable to the calling clause, organized by
     *        types (currently Long for non-materialized counts, or byte[] for serialized sequences).
     */
    public DataFrameContext(
            DynamicContext context,
            StructType schema,
            List<String> columnNames
    ) {
        this.columns = new ArrayList<>();
        for (String columnName : columnNames) {
            this.columns.add(new FlworDataFrameColumn(columnName, schema));
        }

        this.context = new DynamicContext(context);

        this.kryo = new Kryo();
        this.kryo.setReferences(false);
        FlworDataFrameUtils.registerKryoClassesKryo(this.kryo);
        this.output = new Output(128, -1);
        this.input = new Input();
    }

    /**
     * Builds a new data frame context.
     * 
     * @param context the parent dynamic context, which contains all variable values except those in input tuples.
     * @param columns the DataFrame columns applicable to the calling clause.
     */
    public DataFrameContext(
            DynamicContext context,
            List<FlworDataFrameColumn> columns
    ) {
        this.columns = columns;

        this.context = new DynamicContext(context);

        this.kryo = new Kryo();
        this.kryo.setReferences(false);
        FlworDataFrameUtils.registerKryoClassesKryo(this.kryo);
        this.output = new Output(128, -1);
        this.input = new Input();
    }

    /**
     * Sets the context from a DataFrame row.
     * 
     * @param row An row, the column names and types of which must correspond to those passed in the constructor.
     * 
     */
    public void setFromRow(Row row) {
        setFromRow(row, null);
    }

    /**
     * Sets the context from a DataFrame row.
     *
     * @param row An row, the column names and types of which must correspond to those passed in the constructor.
     * @param itemType the itemType to use for the conversion.
     *
     */
    public void setFromRow(Row row, ItemType itemType) {
        this.context.getVariableValues().removeAllVariables();

        // Create dynamic context with deserialized data but only with dependencies
        for (FlworDataFrameColumn column : this.columns) {
            int columnIndex = row.fieldIndex(column.getColumnName());
            if (column.isNativeSequence()) {
                List<Item> i = readColumnAsSequenceOfItems(row, itemType, columnIndex);
                this.context.getVariableValues()
                    .addVariableValue(
                        column.getVariableName(),
                        i
                    );
            }
            if (!column.isCount()) {
                List<Item> i = readColumnAsSequenceOfItems(row, itemType, columnIndex);
                this.context.getVariableValues()
                    .addVariableValue(
                        column.getVariableName(),
                        i
                    );
            } else {
                long count = FlworDataFrameUtils.getCountOfField(row, columnIndex);
                Item i = ItemFactory.getInstance().createLongItem(count);
                this.context.getVariableValues()
                    .addVariableCount(
                        column.getVariableName(),
                        i
                    );
            }
        }
    }

    /**
     * Gets the currently populated dynamic context. It is a child of the context passed to the constructor,
     * populated with the current input tuple with one of the two set* functions.
     * 
     * @return the dynamic context, for evaluating and expression.
     */
    public DynamicContext getContext() {
        return this.context;
    }

    /**
     * Gets a Kryo output that the caller can use for serialization purposes.
     * 
     * @return a Kryo output.
     */
    public Output getOutput() {
        return this.output;
    }

    /**
     * Gets a Kryo object that the caller can use for serialization and deserialization purposes.
     * 
     * @return a Kryo.
     */
    public Kryo getKryo() {
        return this.kryo;
    }

    /**
     * Gets a Kryo input that the caller can use for deserialization purposes.
     * 
     * @return a Kryo input.
     */
    public Input getInput() {
        return this.input;
    }

    private void readObject(java.io.ObjectInputStream in)
            throws IOException,
                ClassNotFoundException {
        in.defaultReadObject();

        this.kryo = new Kryo();
        this.kryo.setReferences(false);
        FlworDataFrameUtils.registerKryoClassesKryo(this.kryo);
        this.output = new Output(128, -1);
        this.input = new Input();
    }

    @SuppressWarnings("unchecked")
    private List<Item> readColumnAsSequenceOfItems(Row row, ItemType itemType, int columnIndex) {
        Object o = row.get(columnIndex);
        DataType dt = row.schema().fields()[columnIndex].dataType();
        // There are three special cases:
        // - NULL: this is an empty sequence
        // - A binary value: this is a serialized sequence
        // - An array of binary values: this is a sequence of serialized items
        // Otherwise we fall back to a sequence of just one item with the regular item parser
        if (o == null) {
            return Collections.emptyList();
        }
        if (o instanceof byte[]) {
            byte[] bytes = (byte[]) o;
            this.input.setBuffer(bytes);
            try {
                return (List<Item>) this.kryo.readClassAndObject(this.input);
            } catch (Exception e) {
                RuntimeException ex = new OurBadException(
                        "Error while deserializing column " + row.schema().fields()[columnIndex].name()
                );
                ex.initCause(e);
                throw ex;
            }
        }
        if (dt instanceof ArrayType) {
            ArrayType arrayType = (ArrayType) dt;
            if (arrayType.elementType().equals(DataTypes.BinaryType)) {
                List<Object> objects = row.getList(columnIndex);
                List<Item> items = new ArrayList<>();
                for (Object object : objects) {
                    byte[] bytes = (byte[]) object;
                    this.input.setBuffer(bytes);
                    Item item = (Item) this.kryo.readClassAndObject(this.input);
                    items.add(item);
                }
                return items;
            }
            FlworDataFrameColumn dfColumn = new FlworDataFrameColumn(
                    row.schema().fields()[columnIndex].name(),
                    row.schema()
            );

            if (dfColumn.isNativeSequence()) {
                List<Object> objects = row.getList(columnIndex);
                List<Item> items = new ArrayList<>();
                for (Object object : objects) {
                    Item item = ItemParser.convertValueToItem(
                        object,
                        ((ArrayType) dt).elementType(),
                        ExceptionMetadata.EMPTY_METADATA,
                        itemType == null ? null : itemType.getArrayContentFacet()
                    );
                    items.add(item);
                }
                return items;
            }
        }
        Item item = ItemParser.convertValueToItem(o, dt, ExceptionMetadata.EMPTY_METADATA, itemType);
        return Collections.singletonList(item);
    }
}
