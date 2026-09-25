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

public class ObjectDescendantClosure implements FlatMapFunction<Item, Item> {

    @Serial
    private static final long serialVersionUID = 1L;

    @Override
    public Iterator<Item> call(Item arg0) throws Exception {
        List<Item> results = new ArrayList<Item>();
        List<Item> innerValues;

        if (arg0.isArray()) {
            innerValues = arg0.getItemMembers();
        } else if (arg0.isObject()) {
            results.add(arg0);
            innerValues = arg0.getItemValues();
        } else {
            // for atomic types: do nothing
            return results.iterator();
        }

        for (Item item : innerValues) {
            Iterator<Item> innerResult = this.call(item);
            while (innerResult.hasNext()) {
                results.add(innerResult.next());
            }
        }
        return results.iterator();
    }
}
;
