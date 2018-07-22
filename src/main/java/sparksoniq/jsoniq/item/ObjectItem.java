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
 package sparksoniq.jsoniq.item;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import sparksoniq.exceptions.DuplicateObjectKeyException;
import sparksoniq.jsoniq.item.metadata.ItemMetadata;
import sparksoniq.semantics.types.ItemType;
import sparksoniq.semantics.types.ItemTypes;

import javax.naming.OperationNotSupportedException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class ObjectItem extends JsonItem{

    @Override
    public List<String> getKeys() {
        return _keys;
    }

    @Override
    public Collection<? extends Item> getValues() {
        return _values;
    }

    public ObjectItem(List<String> keys, List<Item> values, ItemMetadata itemMetadata){
        super(itemMetadata);
        checkForDuplicateKeys(keys);
        this._keys = keys;
        this._values = values;
    }

    private void checkForDuplicateKeys(List<String> keys) {
        HashMap<String, Integer> frequencies = new HashMap<>();
        for(String key : keys) {
            if(frequencies.containsKey(key))
                throw new DuplicateObjectKeyException(key, getItemMetadata().getExpressionMetadata());

            else
                frequencies.put(key, 1);
        }
    }

    @Override
    public List<Item> getItems() throws OperationNotSupportedException {
        throw new OperationNotSupportedException("Objects are not arrays");
    }

    @Override
    public Item getItemAt(int i) throws OperationNotSupportedException {
        throw new OperationNotSupportedException("Objects are not arrays");
    }

    @Override
    public void putItem(Item value) throws OperationNotSupportedException {
        throw new OperationNotSupportedException("Objects are not arrays");
    }

    @Override
    public Item getItemByKey(String s) {
        if(_keys.contains(s))
            return _values.get(_keys.indexOf(s));
        else
            return null;
    }

    @Override
    public void putItemByKey(String s, Item value) {
        _values.replaceAll(item ->{
            if(_values.indexOf(item) ==_keys.indexOf(s))
                return value;
            else
                return item;
        });
    }

    @Override
    public  boolean isObject(){ return true; }

    @Override public boolean isTypeOf(ItemType type) {
        if(type.getType().equals(ItemTypes.ObjectItem) || super.isTypeOf(type))
            return true;
        return false;
    }

    @Override
    public String serialize() {
        String result = "{ ";
        for (Item value : this._values) {
            boolean isStringValue = value instanceof StringItem;
            result += "\"" + _keys.get(_values.indexOf(value)) + "\"" + " : " +
                    (isStringValue? "\"" :"") +  value.serialize() + (isStringValue? "\"" :"")
                    + (_values.indexOf(value) < _values.size() -1? ", " : " ");
        }
        result += "}";
        return result;
    }

    @Override
    public void write(Kryo kryo, Output output) {
        kryo.writeObject(output, _keys);
        kryo.writeObject(output, _values);
    }

    @Override
    public void read(Kryo kryo, Input input) {
        this._keys = kryo.readObject(input, ArrayList.class);
        this._values = kryo.readObject(input, ArrayList.class);
    }


    private List<Item> _values;
    private List<String> _keys;

}
