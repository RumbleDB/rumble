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
import sparksoniq.exceptions.SparksoniqRuntimeException;
import sparksoniq.jsoniq.item.ItemFactory;
import sparksoniq.jsoniq.tuple.FlworTuple;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.rumbledb.api.Item;

public class DynamicContext implements Serializable, KryoSerializable {

    private static final long serialVersionUID = 1L;

    public enum VariableDependency {
        FULL,
        COUNT,
        SUM,
        AVG,
        MAX,
        MIN
    }

    private Map<String, List<Item>> _variableValues;
    private Map<String, Item> _variableCounts;
    private DynamicContext _parent;

    public DynamicContext() {
        this._parent = null;
        this._variableValues = new HashMap<>();
        this._variableCounts = new HashMap<>();
    }

    public DynamicContext(DynamicContext parent) {
        this._parent = parent;
        this._variableValues = new HashMap<>();
        this._variableCounts = new HashMap<>();
    }

    public DynamicContext(FlworTuple tuple) {
        this();
        setBindingsFromTuple(tuple);
    }

    public DynamicContext(DynamicContext parent, FlworTuple tuple) {
        this._parent = parent;
        this._variableValues = new HashMap<>();
        this._variableCounts = new HashMap<>();
        setBindingsFromTuple(tuple);
    }

    public void setBindingsFromTuple(FlworTuple tuple) {
        for (String key : tuple.getKeys())
            if (!key.startsWith("."))
                this.addVariableValue(key, tuple.getValue(key));
    }

    public void addVariableValue(String varName, List<Item> value) {
        this._variableValues.put(varName, value);
    }

    public void addVariableCount(String varName, Item count) {
        this._variableCounts.put(varName, count);
    }

    public List<Item> getVariableValue(String varName) {
        if (_variableValues.containsKey(varName))
            return _variableValues.get(varName);

        if (_parent != null)
            return _parent.getVariableValue(varName);

        if (_variableCounts.containsKey(varName))
            throw new SparksoniqRuntimeException(
                    "Runtime error retrieving variable " + varName + " value: only count available."
            );

        throw new SparksoniqRuntimeException("Runtime error retrieving variable " + varName + " value");
    }

    public Item getVariableCount(String varName) {
        if (_variableCounts.containsKey(varName)) {
            return _variableCounts.get(varName);
        }
        if (_variableValues.containsKey(varName)) {
            Item count = ItemFactory.getInstance().createIntegerItem(_variableValues.get(varName).size());
            return count;
        }
        if (_parent != null) {
            return _parent.getVariableCount(varName);
        }
        throw new SparksoniqRuntimeException("Runtime error retrieving variable " + varName + " value");
    }

    public void removeVariable(String varName) {
        this._variableValues.remove(varName);
        this._variableCounts.remove(varName);
    }

    public void removeAllVariables() {
        this._variableValues.clear();
        this._variableCounts.clear();
    }

    @Override
    public void write(Kryo kryo, Output output) {
        kryo.writeObject(output, _parent);
        kryo.writeObject(output, _variableValues);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void read(Kryo kryo, Input input) {
        _parent = kryo.readObjectOrNull(input, DynamicContext.class);
        _variableValues = kryo.readObject(input, HashMap.class);
    }

    public Item getPosition() {
        if (_variableValues.containsKey("$position")) {
            return _variableValues.get("$position").get(0);
        }
        if (_parent != null)
            return _parent.getPosition();
        return null;
    }

    public void setPosition(long position) {
        List<Item> list = new ArrayList<Item>();
        Item item = null;
        if (position < Integer.MAX_VALUE) {
            item = ItemFactory.getInstance().createIntegerItem((int) position);
        } else {
            item = ItemFactory.getInstance().createDecimalItem(new BigDecimal(position));
        }
        list.add(item);
        _variableValues.put("$position", list);
    }

    public Item getLast() {
        if (_variableValues.containsKey("$last")) {
            return _variableValues.get("$last").get(0);
        }
        if (_parent != null)
            return _parent.getLast();
        return null;
    }

    public void setLast(long last) {
        List<Item> list = new ArrayList<Item>();
        Item item = null;
        if (last < Integer.MAX_VALUE) {
            item = ItemFactory.getInstance().createIntegerItem((int) last);
        } else {
            item = ItemFactory.getInstance().createDecimalItem(new BigDecimal(last));
        }
        list.add(item);
        _variableValues.put("$last", list);
    }
}

