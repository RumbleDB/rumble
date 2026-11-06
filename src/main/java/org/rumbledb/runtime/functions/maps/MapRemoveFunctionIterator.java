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
import org.rumbledb.exceptions.MoreThanOneItemException;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AbstractAtMostOneItemRuntimePlan;
import org.rumbledb.runtime.plan.ItemRuntimePlan;

/**
 * W3C XPath/XQuery {@code map:remove}:
 * {@code map:remove($map as map(*), $keys as xs:anyAtomicType*) as map(*)}.
 *
 * Removes all entries whose key is the same-key as any supplied key (op:same-key).
 * This built-in is local execution only (consistent with other map/array accessors).
 */
public class MapRemoveFunctionIterator extends AbstractAtMostOneItemRuntimePlan {

    @Override
    public Item evaluateAtMostOne(DynamicContext context) {
        Item mapItem = null;
        try {
            mapItem = this.mapIterator.materializeAtMostOne(context);
        } catch (MoreThanOneItemException e) {
            throw new UnexpectedTypeException(
                    "map:remove expects exactly one map argument [err:XPTY0004].", getMetadata());
        }

        if (mapItem == null || !mapItem.isMap()) {
            throw new UnexpectedTypeException(
                    "Type error; first argument to map:remove must be a map [err:XPTY0004].", getMetadata());
        }

        List<Item> rawKeys = this.keysIterator.materialize(context);

        if (rawKeys.isEmpty()) {
            return mapItem;
        }

        List<Item> keysToRemove = new ArrayList<>();
        for (Item it : rawKeys) {
            List<Item> atomized = it.atomizedValue();
            for (Item a : atomized) {
                if (a == null || !a.isAtomic()) {
                    throw new UnexpectedTypeException(
                            "map:remove expects keys that atomize to atomic items [err:XPTY0004].", getMetadata());
                }
                keysToRemove.add(a);
            }
        }

        if (keysToRemove.isEmpty()) {
            return mapItem;
        }
        // Updating a mutable map in a side-effecting query gives a mutable map.
        boolean mutable = mapItem.getMutabilityLevel() != -1
                && this.getRuntimeStaticContext().isQuerySideEffecting();
        return ItemFactory.getInstance().createMapItemRemovingKeys(mapItem, keysToRemove, mutable);
    }

    @Serial
    private static final long serialVersionUID = 1L;

    private final ItemRuntimePlan mapIterator;
    private final ItemRuntimePlan keysIterator;

    public MapRemoveFunctionIterator(List<ItemRuntimePlan> arguments, RuntimeStaticContext staticContext) {
        super(arguments, staticContext);
        if (arguments.size() != 2) {
            throw new OurBadException("map:remove must have exactly two arguments.");
        }
        this.mapIterator = arguments.get(0);
        this.keysIterator = arguments.get(1);
    }
}
