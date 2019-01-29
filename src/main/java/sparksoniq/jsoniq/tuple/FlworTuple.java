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
 package sparksoniq.jsoniq.tuple;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoSerializable;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import sparksoniq.exceptions.SparksoniqRuntimeException;
import sparksoniq.jsoniq.item.Item;

import java.io.Serializable;
import java.util.*;

public class FlworTuple implements Serializable, KryoSerializable{

    public FlworTuple(int nb){
        variables = new LinkedHashMap<>(nb);
    }

    /**
     * Create a deep copy
     * @param toCopy original tuple
     */
    public FlworTuple(FlworTuple toCopy){
        variables = new LinkedHashMap<>(toCopy.getKeys().size());
        for(String key: toCopy.getKeys())
            this.putValue(key, toCopy.getValue(key), true);
    }

    /**
     * Create a tuple containing only the given key-value pair
     * @param newKey
     * @param value
     */
    public FlworTuple(String newKey, List<Item> value) {
        this(1);
        this.putValue(newKey, value, false);
    }

    /**
     * Create a deep copy containing new key-value pair
     * @param toCopy original tuple
     * @param newKey
     * @param value
     */
    public FlworTuple(FlworTuple toCopy, String newKey, List<Item> value){
        this(toCopy);
        this.putValue(newKey, value, false);
    }

    public boolean contains(String key){
        return variables.containsKey(key);
    }

    public void putValue(String key, List<Item> value, boolean overrideExistingValue){
        if(variables.containsKey(key) && overrideExistingValue) {
            String oldKey = key;
            List<Item> oldValue = variables.get(oldKey);
            while (variables.containsKey(oldKey))
                oldKey = "." + oldKey;
            variables.put(oldKey, oldValue);
        }
        variables.put(key, value);
    }

    public void putValue(String key, Item value, boolean overrideExistingValue){
        List<Item> itemList = new ArrayList<>(1);
        itemList.add(value);
        this.putValue(key, itemList, overrideExistingValue);
    }

    public List<Item> getValue(String key){
        if(contains(key))
            return variables.get(key);

        throw new SparksoniqRuntimeException("Undeclared FLOWR variable");
    }

    public Set<String> getKeys() {
        return this.variables.keySet();
    }

    @Override
    public void write(Kryo kryo, Output output) {
        kryo.writeObject(output, variables);
    }

    @Override
    public void read(Kryo kryo, Input input) {
        variables = kryo.readObject(input, LinkedHashMap.class);
    }

    private Map<String, List<Item>> variables;

}
