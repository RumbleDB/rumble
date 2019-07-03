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
import sparksoniq.jsoniq.item.Item;
import sparksoniq.jsoniq.item.ItemFactory;
import sparksoniq.jsoniq.item.metadata.ItemMetadata;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class JiqsItemParser implements Serializable {

    public static Item getItemFromObject(Object object, IteratorMetadata metadata) {
        if (object != null) {
            if (object instanceof String)
                return ItemFactory.getInstance().createStringItem(object.toString());
            if (object instanceof Integer)
                return ItemFactory.getInstance().createIntegerItem((int) object);
            if (object instanceof Double)
                return ItemFactory.getInstance().createDoubleItem((double) object);
            if (object instanceof BigDecimal)
                return ItemFactory.getInstance().createDecimalItem(new BigDecimal(object.toString()));
            if (object instanceof Boolean)
                return ItemFactory.getInstance().createBooleanItem((boolean) object);
            if (object instanceof JSONArray) {
                JSONArray curentArray = (JSONArray) object;
                int numberOfValues = curentArray.length();
                List<Item> values = new ArrayList<>(numberOfValues);
                int index = 0;
                while (index < numberOfValues)
                    values.add(getItemFromObject(curentArray.get(index++), metadata));
                return ItemFactory.getInstance().createArrayItem(values);
            }
            if (object instanceof JSONObject) {
                JSONObject currentObject = (JSONObject) object;
                int numberOfValues = currentObject.length();
                List<String> keys = new ArrayList<>(numberOfValues);
                List<Item> values = new ArrayList<>(numberOfValues);
                Iterator<String> keyIterator = currentObject.keys();
                while (keyIterator.hasNext())
                    keys.add(keyIterator.next());
                keys.forEach(_key -> values.add(getItemFromObject(currentObject.get(_key), metadata)));
                return ItemFactory.getInstance().createObjectItem(keys, values, ItemMetadata.fromIteratorMetadata(metadata));
            }

        }
        return ItemFactory.getInstance().createNullItem();
    }

}
