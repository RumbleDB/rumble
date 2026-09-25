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
package org.rumbledb.items;

import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import org.rumbledb.config.RumbleConfiguration;
import org.rumbledb.context.BuiltinFunction;
import org.rumbledb.context.ConstructorFunctionResolver.ResolvedConstructor;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.FunctionIdentifier;
import org.rumbledb.context.Name;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.runtime.functions.BuiltinNamedFunctionReferenceMarkerIterator;
import org.rumbledb.runtime.functions.ConstructorFunctionIterator;
import org.rumbledb.runtime.plan.ItemRuntimePlan;
import org.rumbledb.runtime.primary.VariableReferenceIterator;
import org.rumbledb.types.SequenceType;

/**
 * Construction helpers for {@link FunctionItem} values used by the runtime.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FunctionItemFactory {

    /**
     * Constructor references have a real cast body, bound to the lookup site's static context.
     * Ordinary function-item invocation and partial application retain this immutable body locally.
     */
    public static FunctionItem createConstructorReference(
            ResolvedConstructor constructor, DynamicContext moduleContext, RuntimeStaticContext staticContext) {
        Name parameterName = Name.createVariableInNoNamespace("$p0");
        RuntimeStaticContext bodyContext = staticContext.toBuilder()
                .staticType(constructor.signature().getReturnType())
                .executionMode(ExecutionMode.LOCAL)
                .isUpdating(false)
                .isSequential(false)
                .build();
        ItemRuntimePlan parameter = new VariableReferenceIterator(
                parameterName,
                bodyContext.toBuilder()
                        .staticType(constructor.signature().getParameterTypes().get(0))
                        .build());
        ItemRuntimePlan body = new ConstructorFunctionIterator(constructor, List.of(parameter), bodyContext);
        return new FunctionItem(
                constructor.identifier(), List.of(parameterName), constructor.signature(), moduleContext, body);
    }

    /**
     * Builds a function item for a builtin named function reference, using synthetic parameter names
     * {@code $p0}, {@code $p1}, ... aligned with the catalogue signature order.
     */
    public static FunctionItem createBuiltinNamedReference(
            FunctionIdentifier identifier,
            DynamicContext moduleContext,
            RumbleConfiguration conf,
            ExceptionMetadata metadata,
            BuiltinFunction builtinFunction) {
        List<Name> paramNames = new ArrayList<>();
        int arity = builtinFunction.getSignature().getParameterTypes().size();
        for (int i = 0; i < arity; i++) {
            paramNames.add(Name.createVariableInNoNamespace("$p" + i));
        }
        SequenceType returnType = builtinFunction.getSignature().getReturnType();
        RuntimeStaticContext markerContext = RuntimeStaticContext.builder()
                .configuration(conf)
                .staticType(returnType)
                .executionMode(ExecutionMode.LOCAL)
                .metadata(metadata)
                .build();
        ItemRuntimePlan markerBody = new BuiltinNamedFunctionReferenceMarkerIterator(markerContext);
        return new FunctionItem(
                identifier, paramNames, builtinFunction.getSignature(), moduleContext, markerBody, true);
    }
}
