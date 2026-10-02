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
 * W3C XPath/XQuery {@code map:entry}:
 * <ul>
 * <li>atomizes a single key into exactly one atomic value</li>
 * <li>materializes the value into a general sequence (possibly empty)</li>
 * <li>returns a map containing a single key/value binding</li>
 * </ul>
 */
public class MapEntryFunctionIterator extends AbstractAtMostOneItemRuntimePlan {

    @Serial
    private static final long serialVersionUID = 1L;

    private final ItemRuntimePlan keyIterator;
    private final ItemRuntimePlan valueIterator;

    public MapEntryFunctionIterator(List<ItemRuntimePlan> arguments, RuntimeStaticContext staticContext) {
        super(arguments, staticContext);
        this.keyIterator = arguments.get(0);
        this.valueIterator = arguments.get(1);
    }

    @Override
    public Item evaluateAtMostOne(DynamicContext dynamicContext) {
        Item key = this.keyIterator.materializeFirstOrNull(dynamicContext);

        List<Item> valueSequence = this.valueIterator.materialize(dynamicContext);

        return createEntry(key, valueSequence);
    }

    private Item createEntry(Item key, List<Item> valueSequence) {
        return ItemFactory.getInstance()
                .createMapItem(
                        key, valueSequence, this.getRuntimeStaticContext().isQuerySideEffecting());
    }
}
