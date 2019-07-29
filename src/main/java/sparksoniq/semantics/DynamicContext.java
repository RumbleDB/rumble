/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
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
import sparksoniq.jsoniq.item.Item;
import sparksoniq.jsoniq.item.ItemFactory;
import sparksoniq.jsoniq.tuple.FlworTuple;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DynamicContext implements Serializable, KryoSerializable {

    private Map<String, List<Item>> _variableValues;
    private Set<String> _variableCounts;
    private DynamicContext _parent;

    public DynamicContext() {
        this._parent = null;
        this._variableValues = new HashMap<>();
        this._variableCounts = new HashSet<>();
    }

    public DynamicContext(DynamicContext parent) {
        this._parent = parent;
        this._variableValues = new HashMap<>();
        this._variableCounts = new HashSet<>();
    }

    public DynamicContext(FlworTuple tuple) {
        this();
        setBindingsFromTuple(tuple);
    }

    public DynamicContext(DynamicContext parent, FlworTuple tuple) {
        this._parent = parent;
        this._variableValues = new HashMap<>();
        this._variableCounts = new HashSet<>();
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
        List<Item> value = new ArrayList<Item>(1);
        value.add(count);
        this._variableValues.put(varName, value);
    }

    public List<Item> getVariableValue(String varName) {
        if (_variableValues.containsKey(varName) && !_variableCounts.contains(varName))
            return _variableValues.get(varName);
        else if (_parent != null)
            return _parent.getVariableValue(varName);
        else
            throw new SparksoniqRuntimeException("Runtime error retrieving variable " +
                    "" + varName + " value");
    }

    public List<Item> getVariableCount(String varName) {
        if (_variableValues.containsKey(varName))
        {
            if(_variableCounts.contains(varName))
            {
                return _variableValues.get(varName);
            }
            else
            {
                Item count = ItemFactory.getInstance().createIntegerItem(_variableValues.get(varName).size());
                List<Item> value = new ArrayList<Item>(1);
                value.add(count);
                return value;
            }
        }
        else if (_parent != null)
            return _parent.getVariableCount(varName);
        else
            throw new SparksoniqRuntimeException("Runtime error retrieving variable " +
                    "" + varName + " value");
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

    @Override
    public void read(Kryo kryo, Input input) {
        _parent = kryo.readObjectOrNull(input, DynamicContext.class);
        _variableValues = kryo.readObject(input, HashMap.class);
    }
}

