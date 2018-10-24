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
import sparksoniq.io.json.JiqsItemParser;
import sparksoniq.jsoniq.item.metadata.ItemMetadata;
import sparksoniq.semantics.types.ItemType;
import sparksoniq.semantics.types.ItemTypes;

import javax.naming.OperationNotSupportedException;

import org.json.JSONObject;

import java.util.*;

public class JSONObjectItem extends JsonItem{
	
	private JSONObject _JSONObject;

	static JiqsItemParser parser = new JiqsItemParser();
	
    @Override
    public List<String> getKeys() {
        List<String> keys = new LinkedList<String>();
    	Iterator<String> i = _JSONObject.keys();
        while(i.hasNext())
        	keys.add(i.next());
        return keys;
    }

    @Override
    public Collection<? extends Item> getValues() {
        List<Item> values = new LinkedList<Item>();
    	Iterator<String> i = _JSONObject.keys();
        while(i.hasNext())
        	values.add(parser.getItemFromObject(_JSONObject.get(i.next()), itemMetadata));
        return values;
    }

    public JSONObjectItem(JSONObject o, ItemMetadata itemMetadata){
        super(itemMetadata);
        _JSONObject = o;
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
        if(_JSONObject.has(s))
            return parser.getItemFromObject(_JSONObject.get(s), itemMetadata);
        else
            return null;
    }

    @Override
    public void putItemByKey(String s, Item value) throws OperationNotSupportedException {
    	throw new OperationNotSupportedException("Insertion into a native object not supported");
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
        int c = 0;
        for (String key: _JSONObject.keySet()) {
        	Item value = getItemByKey(key);
            boolean isStringValue = value instanceof StringItem;
            result += "\"" + key + "\"" + " : " +
                    (isStringValue? "\"" :"") +  value.serialize() + (isStringValue? "\"" :"")
                    + (c < _JSONObject.keySet().size() -1? ", " : " ");
            c++;
        }
        result += "}";
        return result;
    }

    @Override
    public void write(Kryo kryo, Output output) {
        kryo.writeObject(output, _JSONObject);
    }

    @Override
    public void read(Kryo kryo, Input input) {
        this._JSONObject = kryo.readObject(input, JSONObject.class);
    }


}
