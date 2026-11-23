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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.spark.api.java.function.FlatMapFunction;

import org.rumbledb.api.Item;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.items.ItemFactory;

public class ObjectDescendantPairsClosure implements FlatMapFunction<Item, Item> {

    private static final long serialVersionUID = 1L;
    private ExceptionMetadata metadata;

    public ObjectDescendantPairsClosure(ExceptionMetadata metadata) {
        this.metadata = metadata;
    }

    public Iterator<Item> call(Item arg0) throws Exception {
        List<Item> results = new ArrayList<Item>();

        if (arg0.isArray()) {
            for (Item item : arg0.getItemMembers()) {
                Iterator<Item> innerResult = this.call(item);
                while (innerResult.hasNext()) {
                    results.add(innerResult.next());
                }
            }
        } else if (arg0.isObject()) {
            List<String> keys = arg0.getStringKeys();
            for (String key : keys) {
                Item value = arg0.getItemByKey(key);

                List<String> keyList = Collections.singletonList(key);
                List<Item> valueList = Collections.singletonList(value);

                Item result = ItemFactory.getInstance().createObjectItem(keyList, valueList, this.metadata, true);
                results.add(result);

                Iterator<Item> innerResult = this.call(value);
                while (innerResult.hasNext()) {
                    results.add(innerResult.next());
                }
            }
        } else {
            // for atomic types: do nothing
            return results.iterator();
        }

        return results.iterator();
    }
}
;
