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
package org.rumbledb.runtime.typing;

import java.util.ArrayList;
import java.util.List;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.exceptions.CannotAtomizeException;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.runtime.cursor.Cursor;
import org.rumbledb.runtime.plan.ItemRuntimePlan;

final class XmlSchemaCastSupport {

    private XmlSchemaCastSupport() {}

    /**
     * Materializes the atomized value of an operand, but only up to two items. This is used to check if the operand is
     * castable to a target type.
     *
     * This is different than RuntimePlan.materializeAtMostOne, where we materialize at most one Item, regardless of
     * whether it is atomic or not.
     * Here we materialize at most two atomic items, which may come from one or two Items, depending on whether the
     * operand is atomic or not.
     */
    static List<Item> materializeAtomizedAtMostTwo(
            ItemRuntimePlan operand, DynamicContext context, ExceptionMetadata metadata) {
        List<Item> result = new ArrayList<>(2);
        try (Cursor<Item> cursor = operand.getCursor(context)) {
            while (cursor.hasNext() && result.size() < 2) {
                Item item = cursor.next();
                if (item.isAtomic()) {
                    addAtomizedItem(result, item, context, metadata);
                } else {
                    try {
                        for (Item atomizedItem : item.atomizedValue()) {
                            addAtomizedItem(result, atomizedItem, context, metadata);
                            if (result.size() == 2) {
                                break;
                            }
                        }
                    } catch (CannotAtomizeException exception) {
                        CannotAtomizeException resultException = new CannotAtomizeException(
                                "Atomization in XML Schema cast failed for \"" + item.serialize() + "\".", metadata);
                        resultException.initCause(exception);
                        throw resultException;
                    }
                }
            }
        }
        return result;
    }

    private static void addAtomizedItem(
            List<Item> result, Item item, DynamicContext context, ExceptionMetadata metadata) {
        if (!item.getDynamicType().isResolved()) {
            item.getDynamicType().resolve(context, metadata);
        }
        result.add(item);
    }
}
