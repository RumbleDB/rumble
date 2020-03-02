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

package sparksoniq.semantics;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoSerializable;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.rumbledb.api.Item;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.SparksoniqRuntimeException;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.items.parsing.RowToItemMapper;
import sparksoniq.jsoniq.tuple.FlworTuple;
import sparksoniq.spark.SparkSessionManager;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DynamicContext implements Serializable, KryoSerializable {

    private static final long serialVersionUID = 1L;
    private Map<String, List<Item>> localVariableValues;
    private Map<String, Item> localVariableCounts;
    private Map<String, JavaRDD<Item>> rddVariableValues;
    private Map<String, Dataset<Row>> dataFrameVariableValues;
    private DynamicContext parent;

    public DynamicContext() {
        this.parent = null;
        this.localVariableCounts = new HashMap<>();
        this.localVariableValues = new HashMap<>();
        this.rddVariableValues = new HashMap<>();
        this.dataFrameVariableValues = new HashMap<>();
    }

    public DynamicContext(DynamicContext parent) {
        this.parent = parent;
        this.localVariableCounts = new HashMap<>();
        this.localVariableValues = new HashMap<>();
        this.rddVariableValues = new HashMap<>();
        this.dataFrameVariableValues = new HashMap<>();
    }

    public DynamicContext(
            DynamicContext parent,
            Map<String, List<Item>> localVariableValues,
            Map<String, JavaRDD<Item>> rddVariableValues,
            Map<String, Dataset<Row>> dataFrameVariableValues
    ) {
        this.parent = parent;
        this.localVariableCounts = new HashMap<>();
        this.localVariableValues = localVariableValues;
        this.rddVariableValues = rddVariableValues;
        this.dataFrameVariableValues = dataFrameVariableValues;

    }

    public void setBindingsFromTuple(FlworTuple tuple, ExceptionMetadata metadata) {
        for (String key : tuple.getLocalKeys()) {
            this.addVariableValue(key, tuple.getLocalValue(key, metadata));
        }
        for (String key : tuple.getRDDKeys()) {
            this.addVariableValue(key, tuple.getRDDValue(key, metadata));
        }
        for (String key : tuple.getDataFrameKeys()) {
            this.addVariableValue(key, tuple.getDataFrameValue(key, metadata));
        }
    }

    public Set<String> getLocalVariableNames() {
        return this.localVariableValues.keySet();
    }

    public Set<String> getRDDVariableNames() {
        return this.rddVariableValues.keySet();
    }

    public Set<String> getDataFrameVariableNames() {
        return this.dataFrameVariableValues.keySet();
    }

    public boolean contains(String varName) {
        return this.localVariableValues.containsKey(varName)
            || this.rddVariableValues.containsKey(varName)
            || this.dataFrameVariableValues.containsKey(varName);
    }

    public boolean isRDD(String varName, ExceptionMetadata metadata) {
        if (!contains(varName)) {
            throw new OurBadException(
                    "Runtime error retrieving variable " + varName + " value.",
                    metadata
            );
        }
        return this.rddVariableValues.containsKey(varName)
            || this.dataFrameVariableValues.containsKey(varName);
    }

    public boolean isDataFrame(String varName, ExceptionMetadata metadata) {
        if (!contains(varName)) {
            throw new OurBadException(
                    "Runtime error retrieving variable " + varName + " value.",
                    metadata
            );
        }
        return this.dataFrameVariableValues.containsKey(varName);
    }

    public void addVariableValue(String varName, List<Item> value) {
        this.localVariableValues.put(varName, value);
    }

    public void addVariableValue(String varName, JavaRDD<Item> value) {
        this.rddVariableValues.put(varName, value);
    }

    public void addVariableValue(String varName, Dataset<Row> value) {
        this.dataFrameVariableValues.put(varName, value);
    }

    public void addVariableCount(String varName, Item count) {
        this.localVariableCounts.put(varName, count);
    }

    public List<Item> getLocalVariableValue(String varName, ExceptionMetadata metadata) {
        if (this.localVariableValues.containsKey(varName)) {
            return this.localVariableValues.get(varName);
        }

        if (this.rddVariableValues.containsKey(varName)) {
            JavaRDD<Item> rdd = this.getRDDVariableValue(varName, metadata);
            return SparkSessionManager.collectRDDwithLimit(rdd);
        }

        if (this.parent != null) {
            return this.parent.getLocalVariableValue(varName, metadata);
        }

        if (this.localVariableCounts.containsKey(varName)) {
            throw new OurBadException(
                    "Runtime error retrieving variable " + varName + " value: only count available.",
                    metadata
            );
        }

        throw new SparksoniqRuntimeException(
                "Runtime error retrieving variable " + varName + " value",
                metadata
        );
    }

    public JavaRDD<Item> getRDDVariableValue(String varName, ExceptionMetadata metadata) {
        if (this.rddVariableValues.containsKey(varName)) {
            return this.rddVariableValues.get(varName);
        }

        if (this.dataFrameVariableValues.containsKey(varName)) {
            Dataset<Row> df = this.dataFrameVariableValues.get(varName);
            JavaRDD<Row> rowRDD = df.javaRDD();
            return rowRDD.map(new RowToItemMapper(metadata));
        }

        if (this.parent != null) {
            return this.parent.getRDDVariableValue(varName, metadata);
        }

        throw new OurBadException(
                "Runtime error retrieving variable " + varName + " value",
                metadata
        );
    }

    public Dataset<Row> getDataFrameVariableValue(String varName, ExceptionMetadata metadata) {
        if (this.dataFrameVariableValues.containsKey(varName)) {
            return this.dataFrameVariableValues.get(varName);
        }

        if (this.parent != null) {
            return this.parent.getDataFrameVariableValue(varName, metadata);
        }

        throw new OurBadException(
                "Runtime error retrieving variable " + varName + " value",
                metadata
        );
    }

    public Item getVariableCount(String varName) {
        if (this.localVariableCounts.containsKey(varName)) {
            return this.localVariableCounts.get(varName);
        }
        if (this.dataFrameVariableValues.containsKey(varName)) {
            return ItemFactory.getInstance()
                .createIntegerItem((int) this.dataFrameVariableValues.get(varName).count());
        }
        if (this.rddVariableValues.containsKey(varName)) {
            return ItemFactory.getInstance().createIntegerItem((int) this.rddVariableValues.get(varName).count());
        }
        if (this.localVariableValues.containsKey(varName)) {
            return ItemFactory.getInstance().createIntegerItem(this.localVariableValues.get(varName).size());
        }
        if (this.parent != null) {
            return this.parent.getVariableCount(varName);
        }
        throw new OurBadException("Runtime error retrieving variable " + varName + " value");
    }

    public void removeVariable(String varName) {
        this.localVariableValues.remove(varName);
        this.localVariableCounts.remove(varName);
        this.rddVariableValues.remove(varName);
        this.dataFrameVariableValues.remove(varName);

    }

    public void removeAllVariables() {
        this.localVariableValues.clear();
        this.localVariableCounts.clear();
        this.rddVariableValues.clear();
        this.dataFrameVariableValues.clear();
    }

    @Override
    public void write(Kryo kryo, Output output) {
        kryo.writeObject(output, this.parent);
        kryo.writeObject(output, this.localVariableValues);
        kryo.writeObject(output, this.rddVariableValues);
        kryo.writeObject(output, this.dataFrameVariableValues);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void read(Kryo kryo, Input input) {
        this.parent = kryo.readObjectOrNull(input, DynamicContext.class);
        this.localVariableValues = kryo.readObject(input, HashMap.class);
        this.rddVariableValues = kryo.readObject(input, HashMap.class);
        this.dataFrameVariableValues = kryo.readObject(input, HashMap.class);
    }

    public Item getPosition() {
        if (this.localVariableValues.containsKey("$position")) {
            return this.localVariableValues.get("$position").get(0);
        }
        if (this.parent != null) {
            return this.parent.getPosition();
        }
        return null;
    }

    public void setPosition(long position) {
        List<Item> list = new ArrayList<>();
        Item item;
        if (position < Integer.MAX_VALUE) {
            item = ItemFactory.getInstance().createIntegerItem((int) position);

        } else {
            item = ItemFactory.getInstance().createDecimalItem(new BigDecimal(position));
        }
        list.add(item);
        this.localVariableValues.put("$position", list);
    }

    public Item getLast() {
        if (this.localVariableValues.containsKey("$last")) {
            return this.localVariableValues.get("$last").get(0);
        }
        if (this.parent != null) {
            return this.parent.getLast();
        }
        return null;
    }

    public void setLast(long last) {
        List<Item> list = new ArrayList<>();
        Item item;
        if (last < Integer.MAX_VALUE) {
            item = ItemFactory.getInstance().createIntegerItem((int) last);
        } else {
            item = ItemFactory.getInstance().createDecimalItem(new BigDecimal(last));
        }
        list.add(item);
        this.localVariableValues.put("$last", list);
    }

    public enum VariableDependency {
        FULL,
        COUNT,
        SUM,
        AVG,
        MAX,
        MIN
    }

    private static VariableDependency mergeSingleVariableDependency(VariableDependency left, VariableDependency right) {
        if (left.equals(right)) {
            return left;
        }
        return VariableDependency.FULL;
    }

    public static void mergeVariableDependencies(
            Map<String, DynamicContext.VariableDependency> into,
            Map<String, DynamicContext.VariableDependency> from
    ) {
        for (String v : from.keySet()) {
            if (into.containsKey(v)) {
                into.put(v, DynamicContext.mergeSingleVariableDependency(into.get(v), from.get(v)));
            } else {
                into.put(v, from.get(v));
            }
        }
    }


}

