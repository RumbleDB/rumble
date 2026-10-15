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
package org.rumbledb.runtime.functions.sequences.general;

import java.util.Arrays;
import java.util.List;

import org.rumbledb.api.Item;
import org.rumbledb.config.RumbleConfiguration;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.context.NamedFunctions;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.runtime.plan.ItemRuntimePlan;
import org.rumbledb.runtime.primary.VariableReferenceIterator;
import org.rumbledb.types.SequenceType;

/**
 * The call of the folding function, shared by fn:fold-left and fn:fold-right.
 *
 * The call is built once per fold and evaluated for each item. Its two arguments refer to variables that are bound in
 * the dynamic context of each step, so the call itself is never modified.
 */
final class FoldFunctionCall {

    private static final Name ACCUMULATOR = Name.createVariableInNoNamespace("$fold-accumulator");
    private static final Name CURRENT_ITEM = Name.createVariableInNoNamespace("$fold-item");

    private final ItemRuntimePlan call;
    private final DynamicContext stepContext;

    /**
     * @param accumulatorFirst true if the accumulator is the first argument of the function, as in fn:fold-left
     */
    FoldFunctionCall(
            Item functionItem,
            boolean accumulatorFirst,
            RuntimeStaticContext callContext,
            RumbleConfiguration configuration,
            ExceptionMetadata metadata,
            DynamicContext context) {
        RuntimeStaticContext argumentContext = RuntimeStaticContext.builder()
                .configuration(configuration)
                .staticType(SequenceType.createSequenceType("item*"))
                .executionMode(ExecutionMode.LOCAL)
                .metadata(metadata)
                .build();
        ItemRuntimePlan accumulator = new VariableReferenceIterator(ACCUMULATOR, argumentContext);
        ItemRuntimePlan currentItem = new VariableReferenceIterator(CURRENT_ITEM, argumentContext);
        this.call = NamedFunctions.buildFunctionItemCallIterator(
                functionItem,
                callContext,
                ExecutionMode.LOCAL,
                accumulatorFirst ? Arrays.asList(accumulator, currentItem) : Arrays.asList(currentItem, accumulator),
                false);
        this.stepContext = new DynamicContext(context);
    }

    /**
     * Applies the function to the accumulator and the current item.
     */
    List<Item> apply(List<Item> accumulator, Item currentItem) {
        this.stepContext.getVariableValues().addVariableValue(ACCUMULATOR, accumulator);
        this.stepContext.getVariableValues().addVariableValue(CURRENT_ITEM, List.of(currentItem));
        return this.call.materialize(this.stepContext);
    }
}
