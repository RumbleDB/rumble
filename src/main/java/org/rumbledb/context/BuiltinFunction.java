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
package org.rumbledb.context;

import java.util.List;
import java.util.Objects;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import org.rumbledb.compiler.typing.ReturnTypeRule;
import org.rumbledb.runtime.plan.ItemRuntimePlan;
import org.rumbledb.types.FunctionSignature;
import org.rumbledb.types.SequenceType;

@Getter
@EqualsAndHashCode
public class BuiltinFunction {

    /**
     * Special enum type to specify diverse range of execution modes for built-in functions
     * These are only used to generate the corresponding (standard) execution modes while generating iterators
     */
    public enum BuiltinFunctionExecutionMode {
        UNSET,
        LOCAL,
        RDD,
        DATAFRAME,
        INHERIT_FROM_FIRST_ARGUMENT,
        INHERIT_FROM_FIRST_ARGUMENT_BUT_DATAFRAME_FALLSBACK_TO_LOCAL
    }

    /** How an omitted final argument is obtained from the context item. */
    public enum ContextConversion {
        /** Supply the item, then apply the parameter's ordinary function conversion. */
        NONE,
        /** Supply fn:string(.) rather than atomizing the context item. */
        STRING_VALUE
    }

    /** The omitted parameter's contract and any conversion before ordinary function conversion. */
    public record ContextArgument(SequenceType parameterType, ContextConversion conversion) {
        public ContextArgument {
            Objects.requireNonNull(parameterType);
            Objects.requireNonNull(conversion);
        }
    }

    private final FunctionIdentifier identifier;
    private final ContextArgument contextArgument;
    private final FunctionSignature signature;
    private final Class<? extends ItemRuntimePlan> functionIteratorClass;
    private final BuiltinFunctionExecutionMode builtinFunctionExecutionMode;

    /** Infers the return type of a call more precisely than the signature does. */
    @EqualsAndHashCode.Exclude
    private final ReturnTypeRule returnTypeRule;

    BuiltinFunction(
            FunctionIdentifier identifier,
            FunctionSignature signature,
            Class<? extends ItemRuntimePlan> functionIteratorClass,
            BuiltinFunctionExecutionMode builtInFunctionExecutionMode) {
        this(identifier, signature, functionIteratorClass, builtInFunctionExecutionMode, ReturnTypeRule.NONE, null);
    }

    private BuiltinFunction(
            FunctionIdentifier identifier,
            FunctionSignature signature,
            Class<? extends ItemRuntimePlan> functionIteratorClass,
            BuiltinFunctionExecutionMode executionMode,
            ReturnTypeRule returnTypeRule,
            ContextArgument contextArgument) {
        this.identifier = identifier;
        this.signature = signature;
        this.functionIteratorClass = functionIteratorClass;
        this.builtinFunctionExecutionMode = executionMode;
        this.returnTypeRule = returnTypeRule;
        this.contextArgument = contextArgument;
    }

    /** This function, with a rule that infers the return type of its calls more precisely than its signature. */
    BuiltinFunction withReturnTypeRule(ReturnTypeRule rule) {
        return new BuiltinFunction(
                this.identifier,
                this.signature,
                this.functionIteratorClass,
                this.builtinFunctionExecutionMode,
                rule,
                this.contextArgument);
    }

    BuiltinFunction withLastArgumentFromContext() {
        return withLastArgumentFromContext(ContextConversion.NONE);
    }

    /** Derives a context overload, preserving its return type and execution mode. */
    BuiltinFunction withLastArgumentFromContext(ContextConversion conversion) {
        List<SequenceType> parameters = this.signature.getParameterTypes();
        if (parameters.isEmpty() || this.contextArgument != null) {
            throw new IllegalStateException("A context default requires an explicit final parameter.");
        }
        int arity = parameters.size() - 1;
        return new BuiltinFunction(
                new FunctionIdentifier(this.identifier.getName(), arity),
                new FunctionSignature(
                        List.copyOf(parameters.subList(0, arity)),
                        this.signature.getReturnType(),
                        this.signature.isUpdating()),
                this.functionIteratorClass,
                this.builtinFunctionExecutionMode,
                this.returnTypeRule,
                new ContextArgument(parameters.get(arity), conversion));
    }

    BuiltinFunction withReturnType(String returnType) {
        return new BuiltinFunction(
                this.identifier,
                new FunctionSignature(
                        this.signature.getParameterTypes(),
                        SequenceType.createSequenceType(returnType),
                        this.signature.isUpdating()),
                this.functionIteratorClass,
                this.builtinFunctionExecutionMode,
                this.returnTypeRule,
                this.contextArgument);
    }

    BuiltinFunction withExecutionMode(BuiltinFunctionExecutionMode executionMode) {
        return new BuiltinFunction(
                this.identifier,
                this.signature,
                this.functionIteratorClass,
                executionMode,
                this.returnTypeRule,
                this.contextArgument);
    }
}
