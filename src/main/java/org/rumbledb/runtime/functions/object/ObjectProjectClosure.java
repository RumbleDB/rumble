/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributor acknowledgements are maintained in the CONTRIBUTORS file at the project root.
 */
package org.rumbledb.runtime.functions.object;

import java.io.Serial;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.spark.api.java.function.FlatMapFunction;

import org.rumbledb.api.Item;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.items.ObjectItem;
import org.rumbledb.items.StringItem;

public class ObjectProjectClosure implements FlatMapFunction<Item, Item> {

    @Serial
    private static final long serialVersionUID = 1L;

    private final List<Item> projectionKeys;
    private final ExceptionMetadata itemMetadata;

    public ObjectProjectClosure(List<Item> projectionKeys, ExceptionMetadata itemMetadata) {
        this.projectionKeys = projectionKeys;
        this.itemMetadata = itemMetadata;
    }

    @Override
    public Iterator<Item> call(Item arg0) throws Exception {
        List<Item> results = new ArrayList<>();
        List<String> keys = new ArrayList<>();
        List<Item> values = new ArrayList<>();

        if (!arg0.isObject()) {
            results.add(arg0);
            return results.iterator();
        }

        for (String key : arg0.getStringKeys()) {
            if (this.projectionKeys.contains(new StringItem(key))) {
                keys.add(key);
                values.add(arg0.getItemByKey(key));
            }
        }

        results.add(new ObjectItem(keys, values, this.itemMetadata));
        return results.iterator();
    }
}
;
