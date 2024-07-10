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
import org.apache.spark.sql.Row;
import org.rumbledb.api.Item;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.JobWithinAJobException;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.RumbleException;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.items.parsing.RowToItemMapper;
import org.rumbledb.items.structured.JSoundDataFrame;
import sparksoniq.jsoniq.tuple.FlworTuple;
import sparksoniq.spark.SparkSessionManager;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class VariableValues implements Serializable, KryoSerializable {

    private static final long serialVersionUID = 1L;
    private Map<Name, List<Item>> localVariableValues;
    private Map<Name, Item> localVariableCounts;
    private Map<Name, JavaRDD<Item>> rddVariableValues;
    private Map<Name, JSoundDataFrame> dataFrameVariableValues;
    private boolean nestedQuery;
    private VariableValues parent;

    public VariableValues() {
        this.parent = null;
        this.localVariableCounts = new HashMap<>();
        this.localVariableValues = new HashMap<>();
        this.rddVariableValues = new HashMap<>();
        this.dataFrameVariableValues = new HashMap<>();
        this.nestedQuery = false;
    }

    public VariableValues(VariableValues parent) {
        if (parent == null) {
            throw new OurBadException("Variable values defined with null parent");
        }
        this.parent = parent;
        this.localVariableCounts = new HashMap<>();
        this.localVariableValues = new HashMap<>();
        this.rddVariableValues = new HashMap<>();
        this.dataFrameVariableValues = new HashMap<>();
        this.nestedQuery = false;
    }

    public VariableValues(
            VariableValues parent,
            Map<Name, List<Item>> localVariableValues,
            Map<Name, JavaRDD<Item>> rddVariableValues,
            Map<Name, JSoundDataFrame> dataFrameVariableValues,
            GlobalVariables globalVariables
    ) {
        if (parent == null) {
            throw new OurBadException("Variable values defined with null parent");
        }
        this.parent = parent;
        this.localVariableCounts = new HashMap<>();
        this.localVariableValues = localVariableValues;
        this.rddVariableValues = rddVariableValues;
        this.dataFrameVariableValues = dataFrameVariableValues;
        removeGlobalVariablesFromCopiedValues(globalVariables);
        this.nestedQuery = false;
    }

    private void removeGlobalVariablesFromCopiedValues(GlobalVariables globalVariables) {
        globalVariables.getGlobalVariables().forEach(globalVariable -> {
            if (containsLocally(this, globalVariable)) {
                removeVariable(globalVariable);
            }
        });
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

    public boolean isParallelAccessAllowed() {
        return this.nestedQuery;
    }

    public void setParallelAccess(boolean b) {
        this.nestedQuery = b;
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

    public void addVariableValue(Name varName, JSoundDataFrame value) {
        this.dataFrameVariableValues.put(varName, value);
    }

    public void addVariableCount(Name varName, Item count) {
        this.localVariableCounts.put(varName, count);
    }

    public List<Item> getLocalVariableValue(Name varName, ExceptionMetadata metadata) {
        if (this.localVariableValues.containsKey(varName) && this.localVariableValues.get(varName) == null) {
            // Referencing an uninitialized local variable is illegal
            throw new RumbleException(
                    "Runtime error retrieving variable " + varName + " value",
                    metadata
            );
        }
        if (this.localVariableValues.containsKey(varName)) {
            return this.localVariableValues.get(varName);
        }

        if (this.rddVariableValues.containsKey(varName)) {
            if (this.nestedQuery) {
                throw new JobWithinAJobException(metadata);
            }
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
            if (this.nestedQuery) {
                throw new JobWithinAJobException(metadata);
            }
            return this.rddVariableValues.get(varName);
        }

        if (this.dataFrameVariableValues.containsKey(varName)) {
            if (this.nestedQuery) {
                throw new JobWithinAJobException(metadata);
            }
            JSoundDataFrame df = this.dataFrameVariableValues.get(varName);
            JavaRDD<Row> rowRDD = df.javaRDD();
            return rowRDD.map(new RowToItemMapper(metadata, df.getItemType()));
        }

        if (this.parent != null) {
            return this.parent.getRDDVariableValue(varName, metadata);
        }

        throw new OurBadException(
                "Runtime error retrieving variable " + varName + " value",
                metadata
        );
    }

    public JSoundDataFrame getDataFrameVariableValue(Name varName, ExceptionMetadata metadata) {
        if (this.dataFrameVariableValues.containsKey(varName)) {
            if (this.nestedQuery) {
                throw new JobWithinAJobException(metadata);
            }
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

    public Item getVariableCount(Name varName, ExceptionMetadata metadata) {
        if (this.localVariableCounts.containsKey(varName)) {
            return this.localVariableCounts.get(varName);
        }
        if (this.dataFrameVariableValues.containsKey(varName)) {
            if (this.nestedQuery) {
                throw new JobWithinAJobException(metadata);
            }
            return ItemFactory.getInstance()
                .createLongItem(this.dataFrameVariableValues.get(varName).count());
        }
        if (this.rddVariableValues.containsKey(varName)) {
            if (this.nestedQuery) {
                throw new JobWithinAJobException(metadata);
            }
            return ItemFactory.getInstance().createLongItem(this.rddVariableValues.get(varName).count());
        }
        if (this.localVariableValues.containsKey(varName)) {
            return ItemFactory.getInstance().createIntItem(this.localVariableValues.get(varName).size());
        }
        if (this.parent != null) {
            return this.parent.getVariableCount(varName, metadata);
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
        kryo.writeObjectOrNull(output, this.parent, VariableValues.class);
        kryo.writeObject(output, this.localVariableValues);
    }

    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();
        this.nestedQuery = true;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void read(Kryo kryo, Input input) {
        this.parent = kryo.readObjectOrNull(input, VariableValues.class);
        this.localVariableValues = kryo.readObject(input, HashMap.class);
        this.nestedQuery = true;
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
        List<Item> list = Arrays.asList(ItemFactory.getInstance().createLongItem(position));
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
        List<Item> list = Arrays.asList(ItemFactory.getInstance().createLongItem(last));
        this.localVariableValues.put(Name.CONTEXT_COUNT, list);
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
            sb.append("    " + name + " (RDD)\n");
        }
        sb.append("  Data Frames:\n");
        for (Name name : this.dataFrameVariableValues.keySet()) {
            sb.append("    " + name + " (DF)\n");
        }
        if (this.parent != null) {
            sb.append("Parent variable values:\n");
            sb.append(this.parent.toString());
        }
        return sb.toString();
    }

    public void importModuleValues(VariableValues moduleValues) {
        for (Name name : moduleValues.localVariableValues.keySet()) {
            List<Item> items = moduleValues.localVariableValues.get(name);
            this.localVariableValues.put(name, items);
        }
        for (Name name : moduleValues.localVariableCounts.keySet()) {
            Item item = moduleValues.localVariableCounts.get(name);
            this.localVariableCounts.put(name, item);
        }
        for (Name name : moduleValues.rddVariableValues.keySet()) {
            JavaRDD<Item> items = moduleValues.rddVariableValues.get(name);
            this.rddVariableValues.put(name, items);
        }
        for (Name name : moduleValues.dataFrameVariableValues.keySet()) {
            JSoundDataFrame items = moduleValues.dataFrameVariableValues.get(name);
            this.dataFrameVariableValues.put(name, items);
        }
    }

    private VariableValues findNodeWithVariableDeclaration(Name varName) {
        VariableValues nodeWithVariableDecl = this;
        // Invariant: there is a node among the current node or its parents that contains the variable
        while (nodeWithVariableDecl != null && !containsLocally(nodeWithVariableDecl, varName)) {
            nodeWithVariableDecl = nodeWithVariableDecl.parent;
        }
        if (nodeWithVariableDecl == null) {
            throw new OurBadException("Changing undeclared variable value is not supported.");
        }
        return nodeWithVariableDecl;
    }

    public boolean containsLocally(VariableValues variableValues, Name varName) {
        return variableValues.localVariableValues.containsKey(varName)
            || variableValues.rddVariableValues.containsKey(varName)
            || variableValues.dataFrameVariableValues.containsKey(varName);
    }

    public void changeVariableValue(Name varName, List<Item> value) {
        VariableValues nodeWithVariableDecl = findNodeWithVariableDeclaration(varName);
        nodeWithVariableDecl.localVariableValues.put(varName, value);
    }

    public void changeVariableValue(Name varName, JavaRDD<Item> value) {
        VariableValues nodeWithVariableDecl = findNodeWithVariableDeclaration(varName);
        nodeWithVariableDecl.rddVariableValues.put(varName, value);
    }

    public void changeVariableValue(Name varName, JSoundDataFrame value) {
        VariableValues nodeWithVariableDecl = findNodeWithVariableDeclaration(varName);
        nodeWithVariableDecl.dataFrameVariableValues.put(varName, value);
    }
}

