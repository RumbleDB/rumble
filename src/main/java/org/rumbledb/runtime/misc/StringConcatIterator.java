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
package org.rumbledb.runtime.misc;

import java.io.Serial;
import java.util.Arrays;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.CannotAtomizeException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AbstractAtMostOneItemRuntimePlan;
import org.rumbledb.runtime.cursor.Cursor;
import org.rumbledb.runtime.plan.ItemRuntimePlan;

public class StringConcatIterator extends AbstractAtMostOneItemRuntimePlan {

    @Serial
    private static final long serialVersionUID = 1L;

    private final ItemRuntimePlan leftIterator;
    private final ItemRuntimePlan rightIterator;

    public StringConcatIterator(
            ItemRuntimePlan leftIterator, ItemRuntimePlan rightIterator, RuntimeStaticContext staticContext) {
        super(Arrays.asList(leftIterator, rightIterator), staticContext);
        this.leftIterator = leftIterator;
        this.rightIterator = rightIterator;
    }

    @Override
    public Item evaluateAtMostOne(DynamicContext dynamicContext) {
        String leftString = atomizeOperandToString(this.leftIterator, dynamicContext, "left");
        String rightString = atomizeOperandToString(this.rightIterator, dynamicContext, "right");
        return ItemFactory.getInstance().createStringItem(leftString.concat(rightString));
    }

    private String atomizeOperandToString(ItemRuntimePlan iterator, DynamicContext dynamicContext, String side) {
        Item singleAtomic = null;
        try (Cursor<Item> cursor = iterator.getCursor(dynamicContext)) {
            while (cursor.hasNext()) {
                Item item = cursor.next();
                if (item.isFunction()) {
                    throw new CannotAtomizeException(
                            "Cannot atomize a function item in string concatenation expression.", getMetadata());
                }
                if (item.isAtomic()) {
                    singleAtomic = recordAtomic(singleAtomic, item, side);
                } else {
                    for (Item atomicItem : item.atomizedValue()) {
                        singleAtomic = recordAtomic(singleAtomic, atomicItem, side);
                    }
                }
            }
        }
        return singleAtomic == null ? "" : singleAtomic.getStringValue();
    }

    private Item recordAtomic(Item current, Item next, String side) {
        if (current != null) {
            throw new UnexpectedTypeException(
                    "String concatenation expression requires at most one item in its " + side + " input sequence.",
                    getMetadata());
        }
        return next;
    }
}
