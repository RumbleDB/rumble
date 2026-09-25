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

import lombok.EqualsAndHashCode;
import lombok.Getter;

import org.rumbledb.runtime.plan.ItemRuntimePlan;
import org.rumbledb.types.FunctionSignature;

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

    private FunctionIdentifier identifier;
    private final FunctionSignature signature;
    private final Class<? extends ItemRuntimePlan> functionIteratorClass;
    private final BuiltinFunctionExecutionMode builtinFunctionExecutionMode;

    BuiltinFunction(
            FunctionIdentifier identifier,
            FunctionSignature signature,
            Class<? extends ItemRuntimePlan> functionIteratorClass,
            BuiltinFunctionExecutionMode builtInFunctionExecutionMode) {
        this.identifier = identifier;
        this.signature = signature;
        this.functionIteratorClass = functionIteratorClass;
        this.builtinFunctionExecutionMode = builtInFunctionExecutionMode;
    }
}
