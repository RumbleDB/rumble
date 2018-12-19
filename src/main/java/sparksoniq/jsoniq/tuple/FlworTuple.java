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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoSerializable;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import sparksoniq.exceptions.SparksoniqRuntimeException;
import sparksoniq.jsoniq.item.Item;

public class FlworTuple implements KryoSerializable{

    public FlworTuple(){
        variables = new LinkedHashMap<>();
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
        List<Item> itemList = new ArrayList<>();
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
