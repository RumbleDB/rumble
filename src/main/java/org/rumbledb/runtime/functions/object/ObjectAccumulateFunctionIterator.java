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
import java.util.LinkedHashMap;
import java.util.List;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AbstractAtMostOneItemRuntimePlan;
import org.rumbledb.runtime.plan.ItemRuntimePlan;

public class ObjectAccumulateFunctionIterator extends AbstractAtMostOneItemRuntimePlan {
    /**
     *
     */
    @Serial
    private static final long serialVersionUID = 1L;

    public ObjectAccumulateFunctionIterator(List<ItemRuntimePlan> arguments, RuntimeStaticContext staticContext) {
        super(arguments, staticContext);
    }

    @Override
    public Item evaluateAtMostOne(DynamicContext context) {
        // The values of each key keep the input order, so the input is collected rather than reduced on Spark.
        return accumulate(this.getChild(0).materialize(context));
    }

    private Item accumulate(List<Item> items) {
        LinkedHashMap<String, List<Item>> keyValuePairs = new LinkedHashMap<>();
        for (Item item : items) {
            if (item.isObject()) {
                for (String key : item.getStringKeys()) {
                    keyValuePairs
                            .computeIfAbsent(key, ignored -> new ArrayList<>())
                            .add(item.getItemByKey(key));
                }
            }
        }
        return ItemFactory.getInstance().createObjectItemFromValueLists(keyValuePairs, true);
    }
}
