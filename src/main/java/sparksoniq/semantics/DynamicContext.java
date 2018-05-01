/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Author: Stefan Irimescu
 *
 */
package sparksoniq.semantics;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoSerializable;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import sparksoniq.exceptions.SparksoniqRuntimeException;
import sparksoniq.jsoniq.item.Item;
import sparksoniq.spark.tuple.FlworTuple;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DynamicContext implements KryoSerializable {

    public DynamicContext(DynamicContext parent, FlworTuple tuple) {
        this(parent);
        generateDynamicContextFromTupleRdd(tuple);
    }

    public DynamicContext(FlworTuple tuple) {
        this();
        generateDynamicContextFromTupleRdd(tuple);
    }

    public DynamicContext() {
        this._parent = null;
        this._variableValues = new HashMap<>();
    }

    public DynamicContext(DynamicContext parent) {
        this._parent = parent;
        this._variableValues = new HashMap<>();
    }


    public List<Item> getVariableValue(String varName) {
        if (_variableValues.containsKey(varName))
            return _variableValues.get(varName);
        else if (_parent != null)
            return _parent.getVariableValue(varName);
        else
            throw new SparksoniqRuntimeException("Runtime error retrieving variable " +
                    "" + varName + " value");
    }

    public void addVariableValue(String varName, List<Item> value) {
        this._variableValues.put(varName, value);
    }

    private void generateDynamicContextFromTupleRdd(FlworTuple tuple) {
        for (String key : tuple.getKeys())
            if (!key.startsWith("."))
                this.addVariableValue(key, tuple.getValue(key));
    }

    public void removeVariable(String varName) {
        this._variableValues.remove(varName);
    }

    @Override
    public void write(Kryo kryo, Output output) {
        HashMap<String, List<Item>> mergedVariables = new HashMap<>(_variableValues);
        DynamicContext parent = this._parent;
        while(parent != null) {
            mergedVariables.putAll(parent._variableValues);
            parent = parent._parent;
        }
        kryo.writeObject(output, new ArrayList<>(mergedVariables.keySet()));
        kryo.writeObject(output, new ArrayList<>(mergedVariables.values()));
        output.close();
    }

    @Override
    public void read(Kryo kryo, Input input) {
        _parent = null;
        _variableValues = new HashMap<>();
        ArrayList<String> keys = kryo.readObject(input, ArrayList.class);
        ArrayList<ArrayList<Item>> values = kryo.readObject(input, ArrayList.class);
        for(String key : keys) {
            _variableValues.put(key, values.get(keys.indexOf(key)));
        }
        input.close();
    }

    private HashMap<String, List<Item>> _variableValues;
    private DynamicContext _parent;
}

