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

package org.rumbledb.context;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoSerializable;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.rumbledb.api.Item;
import org.rumbledb.config.RumbleRuntimeConfiguration;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.RumbleException;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.items.parsing.RowToItemMapper;
import org.rumbledb.runtime.functions.base.KnownFunctions;
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
    private Map<Name, List<Item>> localVariableValues;
    private Map<Name, Item> localVariableCounts;
    private Map<Name, JavaRDD<Item>> rddVariableValues;
    private Map<Name, Dataset<Row>> dataFrameVariableValues;
    private DynamicContext parent;
    private RumbleRuntimeConfiguration conf;
    private KnownFunctions knownFunctions;

    public DynamicContext() {
        this.parent = null;
        this.localVariableCounts = null;
        this.localVariableValues = null;
        this.rddVariableValues = null;
        this.dataFrameVariableValues = null;
        this.conf = null;
        this.knownFunctions = null;
    }

    public DynamicContext(RumbleRuntimeConfiguration conf) {
        this.parent = null;
        this.localVariableCounts = new HashMap<>();
        this.localVariableValues = new HashMap<>();
        this.rddVariableValues = new HashMap<>();
        this.dataFrameVariableValues = new HashMap<>();
        this.conf = conf;
        this.knownFunctions = new KnownFunctions();
    }

    public DynamicContext(DynamicContext parent) {
        if (parent == null) {
            throw new OurBadException("Dynamic context defined with null parent");
        }
        this.parent = parent;
        this.localVariableCounts = new HashMap<>();
        this.localVariableValues = new HashMap<>();
        this.rddVariableValues = new HashMap<>();
        this.dataFrameVariableValues = new HashMap<>();
        this.knownFunctions = null;
    }

    public DynamicContext(
            DynamicContext parent,
            Map<Name, List<Item>> localVariableValues,
            Map<Name, JavaRDD<Item>> rddVariableValues,
            Map<Name, Dataset<Row>> dataFrameVariableValues
    ) {
        if (parent == null) {
            throw new OurBadException("Dynamic context defined with null parent");
        }
        this.parent = parent;
        this.localVariableCounts = new HashMap<>();
        this.localVariableValues = localVariableValues;
        this.rddVariableValues = rddVariableValues;
        this.dataFrameVariableValues = dataFrameVariableValues;
        this.knownFunctions = null;

    }

    public RumbleRuntimeConfiguration getRumbleRuntimeConfiguration() {
        if (this.conf != null) {
            return this.conf;
        }
        if (this.parent != null) {
            return this.parent.getRumbleRuntimeConfiguration();
        }
        return null;
    }

    public void setBindingsFromTuple(FlworTuple tuple, ExceptionMetadata metadata) {
        for (Name key : tuple.getLocalKeys()) {
            this.addVariableValue(key, tuple.getLocalValue(key, metadata));
        }
        for (Name key : tuple.getRDDKeys()) {
            this.addVariableValue(key, tuple.getRDDValue(key, metadata));
        }
        for (Name key : tuple.getDataFrameKeys()) {
            this.addVariableValue(key, tuple.getDataFrameValue(key, metadata));
        }
    }

    public Set<Name> getLocalVariableNames() {
        return this.localVariableValues.keySet();
    }

    public Set<Name> getRDDVariableNames() {
        return this.rddVariableValues.keySet();
    }

    public Set<Name> getDataFrameVariableNames() {
        return this.dataFrameVariableValues.keySet();
    }

    public boolean contains(Name varName) {
        boolean localContains = this.localVariableValues.containsKey(varName)
            || this.rddVariableValues.containsKey(varName)
            || this.dataFrameVariableValues.containsKey(varName);
        if (localContains) {
            return true;
        }
        if (this.parent != null) {
            return this.parent.contains(varName);
        }
        return false;
    }

    public boolean isRDD(Name varName, ExceptionMetadata metadata) {
        if (!contains(varName)) {
            throw new OurBadException(
                    "Runtime error retrieving variable " + varName + " value.",
                    metadata
            );
        }
        return this.rddVariableValues.containsKey(varName)
            || this.dataFrameVariableValues.containsKey(varName);
    }

    public boolean isDataFrame(Name varName, ExceptionMetadata metadata) {
        if (!contains(varName)) {
            throw new OurBadException(
                    "Runtime error retrieving variable " + varName + " value.",
                    metadata
            );
        }
        return this.dataFrameVariableValues.containsKey(varName);
    }

    public void addVariableValue(Name varName, List<Item> value) {
        this.localVariableValues.put(varName, value);
    }

    public void addVariableValue(Name varName, JavaRDD<Item> value) {
        this.rddVariableValues.put(varName, value);
    }

    public void addVariableValue(Name varName, Dataset<Row> value) {
        this.dataFrameVariableValues.put(varName, value);
    }

    public void addVariableCount(Name varName, Item count) {
        this.localVariableCounts.put(varName, count);
    }

    public List<Item> getLocalVariableValue(Name varName, ExceptionMetadata metadata) {
        if (this.localVariableValues.containsKey(varName)) {
            return this.localVariableValues.get(varName);
        }

        if (this.rddVariableValues.containsKey(varName)) {
            JavaRDD<Item> rdd = this.getRDDVariableValue(varName, metadata);
            return SparkSessionManager.collectRDDwithLimit(rdd, metadata);
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

        throw new RumbleException(
                "Runtime error retrieving variable " + varName + " value",
                metadata
        );
    }

    public JavaRDD<Item> getRDDVariableValue(Name varName, ExceptionMetadata metadata) {
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

    public Dataset<Row> getDataFrameVariableValue(Name varName, ExceptionMetadata metadata) {
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

    public Item getVariableCount(Name varName) {
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

    public void removeVariable(Name varName) {
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
        kryo.writeObjectOrNull(output, this.parent, DynamicContext.class);
        kryo.writeObject(output, this.localVariableValues);
        // kryo.writeObject(output, this.rddVariableValues);
        // kryo.writeObject(output, this.dataFrameVariableValues);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void read(Kryo kryo, Input input) {
        this.parent = kryo.readObjectOrNull(input, DynamicContext.class);
        this.localVariableValues = kryo.readObject(input, HashMap.class);
        // this.rddVariableValues = kryo.readObject(input, HashMap.class);
        // this.dataFrameVariableValues = kryo.readObject(input, HashMap.class);
    }

    public Item getPosition() {
        if (this.localVariableValues.containsKey(Name.CONTEXT_POSITION)) {
            return this.localVariableValues.get(Name.CONTEXT_POSITION).get(0);
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
        this.localVariableValues.put(Name.CONTEXT_POSITION, list);
    }

    public Item getLast() {
        if (this.localVariableValues.containsKey(Name.CONTEXT_COUNT)) {
            return this.localVariableValues.get(Name.CONTEXT_COUNT).get(0);
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
        this.localVariableValues.put(Name.CONTEXT_COUNT, list);
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
            Map<Name, DynamicContext.VariableDependency> into,
            Map<Name, DynamicContext.VariableDependency> from
    ) {
        for (Name v : from.keySet()) {
            if (into.containsKey(v)) {
                into.put(v, DynamicContext.mergeSingleVariableDependency(into.get(v), from.get(v)));
            } else {
                into.put(v, from.get(v));
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("  Local:\n");
        for (Name name : this.localVariableValues.keySet()) {
            sb.append("    " + name + " (" + this.localVariableValues.get(name).size() + " items)\n");
            if (this.localVariableValues.get(name).size() == 1) {
                sb.append("      " + this.localVariableValues.get(name).get(0).serialize() + "\n");
            }
        }
        sb.append("  Counts:\n");
        for (Name name : this.localVariableCounts.keySet()) {
            sb.append("    " + name + " (" + this.localVariableCounts.get(name) + " items)\n");
        }
        sb.append("  RDD:\n");
        for (Name name : this.rddVariableValues.keySet()) {
            sb.append("    " + name + " (" + this.rddVariableValues.get(name).count() + " items)\n");
        }
        sb.append("  Data Frames:\n");
        for (Name name : this.dataFrameVariableValues.keySet()) {
            sb.append("    " + name + " (" + this.dataFrameVariableValues.get(name).count() + " items)\n");
        }
        if (this.knownFunctions != null) {
            sb.append("  Known functions:\n");
            sb.append(this.knownFunctions + "\n");
        }
        if (this.parent != null) {
            sb.append("Parent context:\n");
            sb.append(this.parent.toString());
        }
        return sb.toString();
    }

    public void importModuleContext(DynamicContext moduleContext, String targetNamespace) {
        for (Name name : moduleContext.localVariableValues.keySet()) {
            if (name.getNamespace().contentEquals(targetNamespace)) {
                List<Item> items = moduleContext.localVariableValues.get(name);
                this.localVariableValues.put(name, items);
            }
        }
        for (Name name : moduleContext.localVariableCounts.keySet()) {
            if (name.getNamespace().contentEquals(targetNamespace)) {
                Item item = moduleContext.localVariableCounts.get(name);
                this.localVariableCounts.put(name, item);
            }
        }
        for (Name name : moduleContext.rddVariableValues.keySet()) {
            if (name.getNamespace().contentEquals(targetNamespace)) {
                JavaRDD<Item> items = moduleContext.rddVariableValues.get(name);
                this.rddVariableValues.put(name, items);
            }
        }
        for (Name name : moduleContext.dataFrameVariableValues.keySet()) {
            if (name.getNamespace().contentEquals(targetNamespace)) {
                Dataset<Row> items = moduleContext.dataFrameVariableValues.get(name);
                this.dataFrameVariableValues.put(name, items);
            }
        }
    }

    public void setKnownFunctions(
            KnownFunctions knownFunctions
    ) {
        if (this.parent != null) {
            throw new OurBadException("Known function scan only be stored in the module context.");
        }
        this.knownFunctions = knownFunctions;
    }

    public KnownFunctions getKnownFunctions() {
        if (this.knownFunctions != null) {
            return this.knownFunctions;
        }
        if (this.parent != null) {
            return this.parent.getKnownFunctions();
        }
        throw new OurBadException("Known functions are not set up properly in dynamic context.");
    }

    public DynamicContext getModuleContext() {
        if (this.parent != null) {
            return this.parent.getModuleContext();
        }
        return this;
    }

}

