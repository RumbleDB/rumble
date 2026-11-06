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
package org.rumbledb.runtime.functions.maps;

import java.io.Serial;
import java.util.List;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AbstractAtMostOneItemRuntimePlan;
import org.rumbledb.runtime.plan.ItemRuntimePlan;

/**
 * XPath/XQuery map:contains($map, $key) implementation.
 */
public class MapContainsFunctionIterator extends AbstractAtMostOneItemRuntimePlan {

    @Serial
    private static final long serialVersionUID = 1L;

    private final ItemRuntimePlan mapIterator;
    private final ItemRuntimePlan keyIterator;

    public MapContainsFunctionIterator(List<ItemRuntimePlan> arguments, RuntimeStaticContext staticContext) {
        super(arguments, staticContext);
        this.mapIterator = arguments.get(0);
        this.keyIterator = arguments.get(1);
    }

    @Override
    public Item evaluateAtMostOne(DynamicContext context) {
        Item map = this.mapIterator.materializeFirstOrNull(context);
        Item key = this.keyIterator.materializeFirstOrNull(context);
        return evaluate(map, key);
    }

    private static Item evaluate(Item map, Item key) {
        boolean contains = map.getSequenceByKey(key) != null;
        return ItemFactory.getInstance().createBooleanItem(contains);
    }
}
