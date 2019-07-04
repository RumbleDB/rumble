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

package sparksoniq.io.json;

import org.json.JSONArray;
import org.json.JSONObject;

import com.jsoniter.JsonIterator;
import com.jsoniter.ValueType;
import com.jsoniter.any.Any;

import sparksoniq.exceptions.SparksoniqRuntimeException;
import sparksoniq.jsoniq.item.Item;
import sparksoniq.jsoniq.item.ItemFactory;
import sparksoniq.jsoniq.item.metadata.ItemMetadata;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class JiqsItemParser implements Serializable {

    public static Item getItemFromObject(JsonIterator object, IteratorMetadata metadata) {
        try {
            if (object.whatIsNext().equals(ValueType.STRING))
                return ItemFactory.getInstance().createStringItem(object.readString());
            if (object.whatIsNext().equals(ValueType.NUMBER))
            {
                String number = object.readNumberAsString();
                if(number.contains("E") || number.contains("e"))
                {
                    return ItemFactory.getInstance().createDoubleItem(Double.parseDouble(number));
                }
                if(number.contains("."))
                {
                    return ItemFactory.getInstance().createDecimalItem(new BigDecimal(number));
                }
                return ItemFactory.getInstance().createIntegerItem(Integer.parseInt(number));
            }
            if (object.whatIsNext().equals(ValueType.BOOLEAN))
                return ItemFactory.getInstance().createBooleanItem(object.readBoolean());
            if (object.whatIsNext().equals(ValueType.ARRAY)) {
                List<Item> values = new ArrayList<Item>();
                while(object.readArray())
                {
                    values.add(getItemFromObject(object, metadata));
                }
                return ItemFactory.getInstance().createArrayItem(values);
            }
            if (object.whatIsNext().equals(ValueType.OBJECT)) {
                List<String> keys = new ArrayList<String>();
                List<Item> values = new ArrayList<Item>();
                String s = null;
                while((s = object.readObject()) != null)
                {
                    keys.add(s);
                    values.add(getItemFromObject(object, metadata));
                }
                return ItemFactory.getInstance().createObjectItem(keys, values, ItemMetadata.fromIteratorMetadata(metadata));
            }
            if (object.whatIsNext().equals(ValueType.NULL)) {
                object.readNull();
                return ItemFactory.getInstance().createNullItem();
            }
            throw new SparksoniqRuntimeException("Invalid value found while parsing. JSON is not well-formed!");
        } catch (IOException e)
        {
            throw new SparksoniqRuntimeException("IO error while parsing. JSON is not well-formed!");
        }
    }
}
