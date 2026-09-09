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
package org.rumbledb.runtime.functions;

import java.io.Serial;
import java.util.List;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.FunctionIdentifier;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.runtime.AbstractAtMostOneItemRuntimePlan;
import org.rumbledb.runtime.plan.ItemRuntimePlan;
import org.rumbledb.runtime.typing.CastIterator;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.ItemType;
import org.rumbledb.types.SequenceType;

public class ConstructorFunctionIterator extends AbstractAtMostOneItemRuntimePlan {

    @Serial
    private static final long serialVersionUID = 1L;

    private final ItemRuntimePlan argumentIterator;
    private final SequenceType targetSequenceType;

    public ConstructorFunctionIterator(
            FunctionIdentifier identifier, List<ItemRuntimePlan> arguments, RuntimeStaticContext staticContext) {
        super(arguments, staticContext);
        ItemType targetType = BuiltinTypesCatalogue.getItemTypeByName(identifier.getName());
        this.argumentIterator = arguments.get(0);
        this.targetSequenceType = new SequenceType(targetType, SequenceType.Arity.OneOrZero);
    }

    @Override
    public Item evaluateAtMostOne(DynamicContext dynamicContext) {
        ItemRuntimePlan castPlan = new CastIterator(
                this.argumentIterator,
                this.targetSequenceType,
                this.staticContext.toBuilder()
                        .staticType(this.targetSequenceType)
                        .build());
        return castPlan.materializeFirstOrNull(dynamicContext);
    }
}
