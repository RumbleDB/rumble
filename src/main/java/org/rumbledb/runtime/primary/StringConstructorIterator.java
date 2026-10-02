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
package org.rumbledb.runtime.primary;

import java.io.Serial;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.CannotAtomizeException;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AbstractAtMostOneItemRuntimePlan;
import org.rumbledb.runtime.plan.ItemRuntimePlan;

public class StringConstructorIterator extends AbstractAtMostOneItemRuntimePlan {

    @Serial
    private static final long serialVersionUID = 1L;

    private final List<Boolean> isInterpolated;

    public StringConstructorIterator(
            List<ItemRuntimePlan> children, List<Boolean> isInterpolated, RuntimeStaticContext staticContext) {
        super(children, staticContext);
        this.isInterpolated = isInterpolated == null ? Collections.emptyList() : new ArrayList<>(isInterpolated);
    }

    @Override
    public Item evaluateAtMostOne(DynamicContext context) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < this.getChildren().size(); i++) {
            ItemRuntimePlan child = this.getChild(i);
            if (child == null) {
                continue;
            }
            if (!this.isInterpolated.get(i)) {
                Item item = child.materializeFirstOrNull(context);
                if (item != null) {
                    sb.append(item.getStringValue());
                }
            } else {
                List<Item> items = child.materialize(context);
                boolean first = true;
                for (Item item : items) {
                    List<Item> atomized;
                    try {
                        atomized = item.atomizedValue();
                    } catch (CannotAtomizeException e) {
                        throw new CannotAtomizeException(e.getMessage(), getMetadata());
                    } catch (UnsupportedOperationException e) {
                        throw new CannotAtomizeException(
                                "The item cannot be atomized: " + item.getDynamicType(), getMetadata());
                    }
                    for (Item atomicItem : atomized) {
                        if (!first) {
                            sb.append(' ');
                        }
                        sb.append(atomicItem.getStringValue());
                        first = false;
                    }
                }
            }
        }
        return ItemFactory.getInstance().createStringItem(sb.toString());
    }
}
