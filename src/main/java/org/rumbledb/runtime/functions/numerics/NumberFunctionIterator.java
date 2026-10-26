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
package org.rumbledb.runtime.functions.numerics;

import java.io.Serial;
import java.util.List;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AbstractAtMostOneItemRuntimePlan;
import org.rumbledb.runtime.plan.ItemRuntimePlan;
import org.rumbledb.runtime.typing.CastIterator;
import org.rumbledb.types.BuiltinTypesCatalogue;

public class NumberFunctionIterator extends AbstractAtMostOneItemRuntimePlan {

    @Serial
    private static final long serialVersionUID = 1L;

    public NumberFunctionIterator(List<ItemRuntimePlan> parameters, RuntimeStaticContext staticContext) {
        super(parameters, staticContext);
    }

    @Override
    public Item evaluateAtMostOne(DynamicContext context) {
        if (this.getChildren().size() == 0) {
            List<Item> items = context.getVariableValues().getLocalVariableValue(Name.CONTEXT_ITEM, getMetadata());
            // Atomization and cardinality errors are argument errors, not failed double conversions.
            List<Item> atomized = items.get(0).atomizedValue();
            if (atomized.size() > 1) {
                throw new UnexpectedTypeException("fn:number expects at most one atomic value.", getMetadata());
            }
            return castOrNaN(atomized.isEmpty() ? null : atomized.get(0));
        }

        return castOrNaN(this.getChild(0).materializeFirstOrNull(context));
    }

    private Item castOrNaN(Item anyItem) {
        if (anyItem == null) {
            return ItemFactory.getInstance().createDoubleItem(Double.NaN);
        }
        try {
            Item result = CastIterator.castItemToType(
                    anyItem, BuiltinTypesCatalogue.doubleItem, getMetadata(), this.staticContext);
            if (result != null) {
                return result;
            }
            return ItemFactory.getInstance().createDoubleItem(Double.NaN);
        } catch (Exception e) {
            return ItemFactory.getInstance().createDoubleItem(Double.NaN);
        }
    }
}
