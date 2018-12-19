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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.spark.api.java.function.FlatMapFunction;
import org.json.JSONObject;

import sparksoniq.jsoniq.item.Item;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;

public class StringToItemMapper implements FlatMapFunction<Iterator<String>, Item> {
    private final IteratorMetadata metadata;

    public StringToItemMapper(IteratorMetadata metadata) {
        this.metadata = metadata;
    }

    @Override
    public Iterator<Item> call(Iterator<String> stringIterator) throws Exception {
        JiqsItemParser parser = new JiqsItemParser();
        List<JSONObject> objects = parser.parseJsonLinesFromIterator(stringIterator);
        List<Item> items = new ArrayList<>();
        objects.forEach(obj -> items.add(parser.getItemFromObject(obj, metadata)));
        return items.iterator();
    }
}
