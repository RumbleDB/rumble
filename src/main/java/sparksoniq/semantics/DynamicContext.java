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
import sparksoniq.exceptions.OurBadException;
import sparksoniq.exceptions.SparksoniqRuntimeException;
import sparksoniq.io.json.RowToItemMapper;
import sparksoniq.jsoniq.item.ItemFactory;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
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
    private Map<String, List<Item>> _localVariableValues;
    private Map<String, Item> _localVariableCounts;
    private Map<String, JavaRDD<Item>> _rddVariableValues;
    private Map<String, Dataset<Row>> _dataFrameVariableValues;
    private DynamicContext _parent;

    public DynamicContext() {
        this._parent = null;
        this._localVariableCounts = new HashMap<>();
        this._localVariableValues = new HashMap<>();
        this._rddVariableValues = new HashMap<>();
        this._dataFrameVariableValues = new HashMap<>();
    }

    public DynamicContext(DynamicContext parent) {
        this._parent = parent;
        this._localVariableCounts = new HashMap<>();
        this._localVariableValues = new HashMap<>();
        this._rddVariableValues = new HashMap<>();
        this._dataFrameVariableValues = new HashMap<>();
    }

    public DynamicContext(
            DynamicContext parent,
            Map<String, List<Item>> localVariableValues,
            Map<String, JavaRDD<Item>> rddVariableValues,
            Map<String, Dataset<Row>> dataFrameVariableValues
    ) {
        this._parent = parent;
        this._localVariableCounts = new HashMap<>();
        this._localVariableValues = localVariableValues;
        this._rddVariableValues = rddVariableValues;
        this._dataFrameVariableValues = dataFrameVariableValues;

    }

    public void setBindingsFromTuple(FlworTuple tuple, IteratorMetadata metadata) {
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
        return _localVariableValues.keySet();
    }

    public Set<String> getRDDVariableNames() {
        return _rddVariableValues.keySet();
    }

    public Set<String> getDataFrameVariableNames() {
        return _dataFrameVariableValues.keySet();
    }

    public boolean contains(String varName) {
        return _localVariableValues.containsKey(varName)
            || _rddVariableValues.containsKey(varName)
            || _dataFrameVariableValues.containsKey(varName);
    }

    public boolean isRDD(String varName, IteratorMetadata metadata) {
        if (!contains(varName)) {
            throw new OurBadException(
                    "Runtime error retrieving variable " + varName + " value.",
                    metadata
            );
        }
        return _rddVariableValues.containsKey(varName)
            || _dataFrameVariableValues.containsKey(varName);
    }

    public boolean isDataFrame(String varName, IteratorMetadata metadata) {
        if (!contains(varName)) {
            throw new OurBadException(
                    "Runtime error retrieving variable " + varName + " value.",
                    metadata
            );
        }
        return _dataFrameVariableValues.containsKey(varName);
    }

    public void addVariableValue(String varName, List<Item> value) {
        this._localVariableValues.put(varName, value);
    }

    public void addVariableValue(String varName, JavaRDD<Item> value) {
        this._rddVariableValues.put(varName, value);
    }

    public void addVariableValue(String varName, Dataset<Row> value) {
        this._dataFrameVariableValues.put(varName, value);
    }

    public void addVariableCount(String varName, Item count) {
        this._localVariableCounts.put(varName, count);
    }

    public List<Item> getLocalVariableValue(String varName, IteratorMetadata metadata) {
        if (_localVariableValues.containsKey(varName)) {
            return _localVariableValues.get(varName);
        }

        if (_rddVariableValues.containsKey(varName)) {
            JavaRDD<Item> rdd = this.getRDDVariableValue(varName, metadata);
            return SparkSessionManager.collectRDDwithLimit(rdd);
        }

        if (_parent != null) {
            return _parent.getLocalVariableValue(varName, metadata);
        }

        if (_localVariableCounts.containsKey(varName)) {
            throw new OurBadException(
                    "Runtime error retrieving variable " + varName + " value: only count available.",
                    metadata
            );
        }

        throw new SparksoniqRuntimeException(
                "Runtime error retrieving variable " + varName + " value",
                metadata.getExpressionMetadata()
        );
    }

    public JavaRDD<Item> getRDDVariableValue(String varName, IteratorMetadata metadata) {
        if (_rddVariableValues.containsKey(varName)) {
            return _rddVariableValues.get(varName);
        }

        if (_dataFrameVariableValues.containsKey(varName)) {
            Dataset<Row> df = _dataFrameVariableValues.get(varName);
            JavaRDD<Row> rowRDD = df.javaRDD();
            return rowRDD.map(new RowToItemMapper(metadata));
        }

        if (_parent != null) {
            return _parent.getRDDVariableValue(varName, metadata);
        }

        throw new OurBadException(
                "Runtime error retrieving variable " + varName + " value",
                metadata
        );
    }

    public Dataset<Row> getDataFrameVariableValue(String varName, IteratorMetadata metadata) {
        if (_dataFrameVariableValues.containsKey(varName)) {
            return _dataFrameVariableValues.get(varName);
        }

        if (_parent != null) {
            return _parent.getDataFrameVariableValue(varName, metadata);
        }

        throw new OurBadException(
                "Runtime error retrieving variable " + varName + " value",
                metadata
        );
    }

    public Item getVariableCount(String varName) {
        if (_localVariableCounts.containsKey(varName)) {
            return _localVariableCounts.get(varName);
        }
        if (_dataFrameVariableValues.containsKey(varName)) {
            return ItemFactory.getInstance().createIntegerItem((int) _dataFrameVariableValues.get(varName).count());
        }
        if (_rddVariableValues.containsKey(varName)) {
            return ItemFactory.getInstance().createIntegerItem((int) _rddVariableValues.get(varName).count());
        }
        if (_localVariableValues.containsKey(varName)) {
            return ItemFactory.getInstance().createIntegerItem(_localVariableValues.get(varName).size());
        }
        if (_parent != null) {
            return _parent.getVariableCount(varName);
        }
        throw new OurBadException("Runtime error retrieving variable " + varName + " value");
    }

    public void removeVariable(String varName) {
        this._localVariableValues.remove(varName);
        this._localVariableCounts.remove(varName);
        this._rddVariableValues.remove(varName);
        this._dataFrameVariableValues.remove(varName);

    }

    public void removeAllVariables() {
        this._localVariableValues.clear();
        this._localVariableCounts.clear();
        this._rddVariableValues.clear();
        this._dataFrameVariableValues.clear();
    }

    @Override
    public void write(Kryo kryo, Output output) {
        kryo.writeObject(output, _parent);
        kryo.writeObject(output, _localVariableValues);
        kryo.writeObject(output, _rddVariableValues);
        kryo.writeObject(output, _dataFrameVariableValues);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void read(Kryo kryo, Input input) {
        _parent = kryo.readObjectOrNull(input, DynamicContext.class);
        _localVariableValues = kryo.readObject(input, HashMap.class);
        _rddVariableValues = kryo.readObject(input, HashMap.class);
        _dataFrameVariableValues = kryo.readObject(input, HashMap.class);
    }

    public Item getPosition() {
        if (_localVariableValues.containsKey("$position")) {
            return _localVariableValues.get("$position").get(0);
        }
        if (_parent != null) {
            return _parent.getPosition();
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
        _localVariableValues.put("$position", list);
    }

    public Item getLast() {
        if (_localVariableValues.containsKey("$last")) {
            return _localVariableValues.get("$last").get(0);
        }
        if (_parent != null) {
            return _parent.getLast();
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
        _localVariableValues.put("$last", list);
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

