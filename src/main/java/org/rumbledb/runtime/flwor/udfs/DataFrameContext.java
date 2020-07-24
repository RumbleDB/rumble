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
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.flwor.FlworDataFrameUtils;
import scala.collection.mutable.WrappedArray;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    private Map<String, List<String>> columnNamesByType;
    private List<Name> serializedVariableNames;
    private List<Name> countedVariableNames;
    private List<List<Item>> deserializedParams;
    private List<Item> longParams;
    private DynamicContext context;

    private transient Kryo kryo;
    private transient Output output;
    private transient Input input;

    /**
     * Builds a new data frame context.
     * 
     * @param context the parent dynamic context, which contains all variable values except those in input tuples.
     * @param columnNamesByType the names of the DataFrame column names applicable to the calling clause, organized by
     *        types (currently Long for non-materialized counts, or byte[] for serialized sequences).
     */
    public DataFrameContext(
            DynamicContext context,
            Map<String, List<String>> columnNamesByType
    ) {
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
        this.output = new Output(128, -1);
        this.input = new Input();
    }

    /**
     * Sets the context from parameters passed to a Spark SQL UDF.
     * 
     * @param wrappedParameters An array, the members of which are each the serialization of a sequence of items. The
     *        size of the array must match the number of DataFrame columns associated with the type byte[].
     * @param wrappedParametersLong An array, the members of which are each the overall count of a (non-materialized)
     *        sequence of items. The size of the array must match the number of DataFrame columns associated with the
     *        type Long.
     * 
     */
    public void setFromWrappedParameters(
            WrappedArray<byte[]> wrappedParameters,
            WrappedArray<Long> wrappedParametersLong
    ) {
        this.deserializedParams.clear();
        this.context.getVariableValues().removeAllVariables();

        FlworDataFrameUtils.deserializeWrappedParameters(
            wrappedParameters,
            this.deserializedParams,
            this.kryo,
            this.input
        );

        // Long parameters correspond to pre-computed counts, when a materialization of the
        // actual sequence was avoided upfront.
        Object[] longParams = (Object[]) wrappedParametersLong.array();
        for (Object longParam : longParams) {
            Item count = ItemFactory.getInstance().createLongItem(((Long) longParam).longValue());
            this.longParams.add(count);
        }

        FlworDataFrameUtils.prepareDynamicContext(
            this.context,
            this.serializedVariableNames,
            this.countedVariableNames,
            this.deserializedParams,
            this.longParams
        );
    }

    /**
     * Sets the context from a DataFrame row.
     * 
     * @param row An row, the column names and types of which must correspond to those passed in the constructor.
     * 
     */
    public void setFromRow(Row row) {
        this.deserializedParams.clear();
        this.context.getVariableValues().removeAllVariables();

        // Create dynamic context with deserialized data but only with dependencies
        for (Name field : this.serializedVariableNames) {
            int columnIndex = row.fieldIndex(field.getLocalName());
            List<Item> i = FlworDataFrameUtils.deserializeRowField(row, columnIndex, this.kryo, this.input); // rowColumns.get(columnIndex);
            this.deserializedParams.add(i);
        }
        for (Name field : this.countedVariableNames) {
            int columnIndex = row.fieldIndex(field.getLocalName());
            long count = FlworDataFrameUtils.getCountOfField(row, columnIndex);
            Item i = ItemFactory.getInstance().createLongItem(count);
            this.longParams.add(i);
            ++columnIndex;
        }

        FlworDataFrameUtils.prepareDynamicContext(
            this.context,
            this.serializedVariableNames,
            this.countedVariableNames,
            this.deserializedParams,
            this.longParams
        );
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
}
