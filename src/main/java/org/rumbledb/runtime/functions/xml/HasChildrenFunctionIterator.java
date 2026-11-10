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
package org.rumbledb.runtime.functions.xml;

import java.io.Serial;
import java.util.List;
import java.util.Optional;

import org.rumbledb.api.Item;
import org.rumbledb.compiler.typing.ReturnTypeContext;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.expressions.primary.FunctionCallExpression;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AbstractAtMostOneItemRuntimePlan;
import org.rumbledb.runtime.plan.ItemRuntimePlan;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.SequenceType;

public class HasChildrenFunctionIterator extends AbstractAtMostOneItemRuntimePlan {
    @Serial
    private static final long serialVersionUID = 1L;

    public HasChildrenFunctionIterator(List<ItemRuntimePlan> arguments, RuntimeStaticContext staticContext) {
        super(arguments, staticContext);
    }

    @Override
    public Item evaluateAtMostOne(DynamicContext context) {
        Item node = getContextNode(context);
        if (node == null) {
            return ItemFactory.getInstance().createBooleanItem(false);
        }
        if (!node.isNode()) {
            throw new UnexpectedTypeException("The argument to fn:has-children must be a node", getMetadata());
        }
        return ItemFactory.getInstance().createBooleanItem(!node.children().isEmpty());
    }

    private Item getContextNode(DynamicContext context) {
        if (!this.getChildren().isEmpty()) {
            return this.getChild(0).materializeFirstOrNull(context);
        }
        return context.getVariableValues()
                .getLocalVariableValue(Name.CONTEXT_ITEM, getMetadata())
                .get(0);
    }

    /**
     * Checks the context item of the form without arguments, its implicit argument, as the form with one checks its
     * explicit argument.
     */
    public static Optional<SequenceType> inferReturnType(FunctionCallExpression call, ReturnTypeContext context) {
        SequenceType contextType = call.getStaticContext().getContextItemStaticType();
        if (contextType != null && !contextType.getItemType().isSubtypeOf(BuiltinTypesCatalogue.nodeItem)) {
            context.errors()
                    .report(
                            "fn:has-children() expects a node as its context item, but it is " + contextType + ".",
                            call,
                            contextType.getItemType().mayOverlapWith(BuiltinTypesCatalogue.nodeItem));
        }
        return Optional.empty();
    }
}
