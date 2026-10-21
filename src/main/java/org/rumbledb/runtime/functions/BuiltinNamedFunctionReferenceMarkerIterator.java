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
import java.util.ArrayList;
import java.util.List;

import org.rumbledb.api.Item;
import org.rumbledb.context.BuiltinFunctionCatalogue;
import org.rumbledb.context.BuiltinFunctionExecutionModes;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.FunctionIdentifier;
import org.rumbledb.context.Name;
import org.rumbledb.context.NamedFunctions;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.runtime.cursor.Cursor;
import org.rumbledb.runtime.plan.ItemRuntimePlan;
import org.rumbledb.runtime.plan.LocalRuntimePlan;
import org.rumbledb.runtime.plan.NativeQueryRuntimePlan;
import org.rumbledb.runtime.primary.VariableReferenceIterator;

/**
 * Retains a builtin reference's original identifier, parameters and static context, including through partial
 * application. Each invocation builds its own plan with the execution modes of the bound parameters.
 */
public class BuiltinNamedFunctionReferenceMarkerIterator extends ItemRuntimePlan
        implements LocalRuntimePlan<Item>, NativeQueryRuntimePlan {

    @Serial
    private static final long serialVersionUID = 1L;

    private final FunctionIdentifier identifier;
    private final List<Name> parameterNames;

    public BuiltinNamedFunctionReferenceMarkerIterator(
            FunctionIdentifier identifier, List<Name> parameterNames, RuntimeStaticContext staticContext) {
        super(List.of(), staticContext);
        this.identifier = identifier;
        this.parameterNames = List.copyOf(parameterNames);
    }

    public Name getFirstParameterName() {
        return this.parameterNames.isEmpty() ? null : this.parameterNames.get(0);
    }

    public ExecutionMode executionMode(ExecutionMode firstArgumentMode) {
        return BuiltinFunctionExecutionModes.resolve(
                BuiltinFunctionCatalogue.getBuiltinFunction(this.identifier, this.staticContext.getQueryLanguage()),
                firstArgumentMode,
                this.staticContext.getConfiguration());
    }

    public ItemRuntimePlan buildCallPlan(DynamicContext context) {
        List<ItemRuntimePlan> parameters = new ArrayList<>();
        var signature = BuiltinFunctionCatalogue.getBuiltinFunction(
                        this.identifier, this.staticContext.getQueryLanguage())
                .getSignature();
        for (int i = 0; i < this.parameterNames.size(); i++) {
            Name name = this.parameterNames.get(i);
            ExecutionMode mode = context.getVariableValues().isDataFrame(name, getMetadata())
                    ? ExecutionMode.DATAFRAME
                    : context.getVariableValues().isRDD(name, getMetadata()) ? ExecutionMode.RDD : ExecutionMode.LOCAL;
            parameters.add(new VariableReferenceIterator(
                    name,
                    this.staticContext.toBuilder()
                            .staticType(signature.getParameterTypes().get(i))
                            .executionMode(mode)
                            .build()));
        }
        ExecutionMode firstMode = parameters.isEmpty()
                ? ExecutionMode.LOCAL
                : parameters.get(0).getRuntimeStaticContext().getExecutionMode();
        return NamedFunctions.getBuiltInFunctionIterator(
                this.identifier,
                parameters,
                this.staticContext.toBuilder()
                        .executionMode(executionMode(firstMode))
                        .build(),
                true);
    }

    @Override
    public Cursor<Item> createNativeCursor(DynamicContext context) {
        throw new OurBadException("A builtin reference must be invoked through FunctionCall.", getMetadata());
    }
}
