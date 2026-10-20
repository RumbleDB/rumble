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
import java.util.ArrayList;
import java.util.List;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AbstractAtMostOneItemRuntimePlan;
import org.rumbledb.runtime.plan.ItemRuntimePlan;

/**
 * W3C XPath/XQuery {@code map:put}:
 * <ul>
 * <li>requires exactly one map argument</li>
 * <li>atomizes the key and requires exactly one atomic value</li>
 * <li>returns a new map with the entry added or replaced (key equivalence via op:same-key)</li>
 * </ul>
 *
 * This built-in is local execution only.
 */
public class MapPutFunctionIterator extends AbstractAtMostOneItemRuntimePlan {

    @Serial
    private static final long serialVersionUID = 1L;

    private final ItemRuntimePlan mapIterator;
    private final ItemRuntimePlan keyIterator;
    private final ItemRuntimePlan valueIterator;

    public MapPutFunctionIterator(List<ItemRuntimePlan> arguments, RuntimeStaticContext staticContext) {
        super(arguments, staticContext);
        if (arguments.size() != 3) {
            throw new OurBadException("map:put must have exactly three arguments.");
        }
        this.mapIterator = arguments.get(0);
        this.keyIterator = arguments.get(1);
        this.valueIterator = arguments.get(2);
    }

    @Override
    public Item evaluateAtMostOne(DynamicContext context) {
        List<Item> maps = this.mapIterator.materialize(context);
        List<Item> rawKey = this.keyIterator.materialize(context);
        List<Item> valueSequence = this.valueIterator.materialize(context);
        if (maps.size() != 1) {
            throw new UnexpectedTypeException(
                    "map:put expects exactly one map argument [err:XPTY0004].", getMetadata());
        }
        Item mapItem = maps.get(0);
        if (mapItem == null || !mapItem.isMap()) {
            throw new UnexpectedTypeException(
                    "Type error; first argument to map:put must be a map [err:XPTY0004].", getMetadata());
        }
        // 2) Atomize $key and require that it atomizes to exactly one atomic value.
        List<Item> atomized = new ArrayList<>();
        for (Item it : rawKey) {
            atomized.addAll(it.atomizedValue());
        }
        if (atomized.size() != 1 || !atomized.get(0).isAtomic()) {
            throw new UnexpectedTypeException(
                    "Map key must atomize to a single atomic value [err:XPTY0004].", getMetadata());
        }
        Item key = atomized.get(0);
        Item result = ItemFactory.getInstance().createMapItemAddingKey(mapItem, key, valueSequence);
        // Retain the mutable-result contract for side-effecting queries. Object-shaped puts
        // have always returned an immutable object, including when the input was mutable.
        if (mapItem.getMutabilityLevel() != -1
                && this.getRuntimeStaticContext().isQuerySideEffecting()
                && !(mapItem.isObject() && key.isString() && valueSequence.size() == 1)) {
            return ItemFactory.getInstance()
                    .createMapItem(result.getItemKeys(), result.getSequenceValues(), getMetadata(), true);
        }
        return result;
    }
}
