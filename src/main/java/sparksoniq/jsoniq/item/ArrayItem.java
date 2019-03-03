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
import sparksoniq.semantics.types.ItemType;
import sparksoniq.semantics.types.ItemTypes;

import javax.naming.OperationNotSupportedException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ArrayItem extends JsonItem {

    public List<Item> getItems() {
        return _arrayItems;
    }

    public ArrayItem(List<Item> arrayItems){
        super();
        this._arrayItems = arrayItems;
    }

    @Override
    public Item getItemAt(int i) {
        return _arrayItems.get(i);
    }

    @Override
    public Item getItemByKey(String s) throws OperationNotSupportedException {
        throw new OperationNotSupportedException("Array items are not objects");
    }

    @Override
    public void putItem(Item value) { this._arrayItems.add(value); }

    @Override
    public void putItemByKey(String s, Item value) throws OperationNotSupportedException {
        throw new OperationNotSupportedException("Array items are not objects");
    }

    @Override
    public List<String> getKeys() throws OperationNotSupportedException {
        throw new OperationNotSupportedException("Array items are not objects");
    }

    @Override
    public Collection<? extends Item> getValues() throws OperationNotSupportedException {
        throw new OperationNotSupportedException("Array items are not objects");
    }


    @Override
    public  boolean isArray(){ return true; }

    @Override
    public int getSize() {
        return this._arrayItems.size();
    }

    @Override public boolean isTypeOf(ItemType type) {
        if(type.getType().equals(ItemTypes.ArrayItem) || super.isTypeOf(type))
            return true;
        return false;
    }

    @Override
    public String serialize() {
        String result = "[ ";
        for (Item item : this._arrayItems)
            result += item.serialize() + (_arrayItems.indexOf(item) < _arrayItems.size() -1 ? ", " : " ");
        result += "]";
        return result;
    }

    @Override
    public void write(Kryo kryo, Output output) {
        kryo.writeObject(output, this._arrayItems);
    }

    @Override
    public void read(Kryo kryo, Input input) {
        this._arrayItems = kryo.readObject(input, ArrayList.class);
    }

    private List<Item> _arrayItems;
}
