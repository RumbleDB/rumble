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
package org.rumbledb.runtime.navigation;

import java.io.Serial;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.spark.api.java.function.FlatMapFunction;

import org.rumbledb.api.Item;

public class ArrayMembersClosure implements FlatMapFunction<Item, Item> {

    @Serial
    private static final long serialVersionUID = 1L;

    @Override
    public Iterator<Item> call(Item arg0) throws Exception {
        List<Item> results = new ArrayList<Item>();

        if (!(arg0.isArray())) {
            return results.iterator();
        }

        if (arg0.isArrayOfItems()) {
            for (Item item : arg0.getItemMembers()) {
                if (item != null) {
                    results.add(item);
                }
            }
        } else {
            for (List<Item> member : arg0.getSequenceMembers()) {
                results.addAll(member);
            }
        }

        return results.iterator();
    }
}
;
