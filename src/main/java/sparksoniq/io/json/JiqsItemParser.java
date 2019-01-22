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
package sparksoniq.io.json;

import org.json.JSONArray;
import org.json.JSONObject;
import sparksoniq.jsoniq.item.*;
import sparksoniq.jsoniq.item.metadata.ItemMetadata;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class JiqsItemParser {

    public static Item getItemFromObject(Object object, IteratorMetadata metadata) {
        if (object != null) {
            if (object instanceof String)
                return new StringItem(object.toString(), ItemMetadata.fromIteratorMetadata(metadata));
            if (object instanceof Integer)
                return new IntegerItem((int) object, ItemMetadata.fromIteratorMetadata(metadata));
            if (object instanceof Double)
                return new DoubleItem((double) object, ItemMetadata.fromIteratorMetadata(metadata));
            if (object instanceof BigDecimal)
                return new DecimalItem(new BigDecimal(object.toString()), ItemMetadata.fromIteratorMetadata(metadata));
            if (object instanceof Boolean)
                return new BooleanItem((boolean) object, ItemMetadata.fromIteratorMetadata(metadata));
            if (object instanceof JSONArray) {
                JSONArray curentArray = (JSONArray) object;
                List<Item> values = new ArrayList<>();
                int index = 0;
                while (index < curentArray.length())
                    values.add(getItemFromObject(curentArray.get(index++), metadata));
                return new ArrayItem(values, ItemMetadata.fromIteratorMetadata(metadata));
            }
            if (object instanceof JSONObject) {
                JSONObject currentObject = (JSONObject) object;
                List<String> keys = new ArrayList<>();
                List<Item> values = new ArrayList<>();
                Iterator<String> keyIterator = currentObject.keys();
                while (keyIterator.hasNext())
                    keys.add(keyIterator.next());
                keys.forEach(_key -> values.add(getItemFromObject(currentObject.get(_key), metadata)));
                return new ObjectItem(keys, values, ItemMetadata.fromIteratorMetadata(metadata));
            }

        }
        return new NullItem(ItemMetadata.fromIteratorMetadata(metadata));
    }

}
