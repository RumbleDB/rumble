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
package org.rumbledb.compiler;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.Accessors;

@Value
@Builder(access = AccessLevel.PACKAGE)
@Accessors(fluent = true)
public class VisitorConfig {

    /** Whether duplicate function signatures are tolerated during this pass. */
    boolean suppressErrorsForFunctionSignatureCollision;

    /** Whether unresolved function calls are tolerated during this pass. */
    boolean suppressErrorsForCallingMissingFunctions;

    /** Whether reading an unresolved execution mode is tolerated during this pass. */
    boolean suppressErrorsForAccessingUnsetExecutionModes;

    /** Whether unresolved variable-reference execution modes are treated as local. */
    boolean setUnsetExecutionModeOfVariableReferenceExpressionsToLocal;

    /**
     * The initial pass should collect all function declaration information to support hoisting.
     * User defined functions'(UDF) signatures should not collide with each other or built-in functions.
     * As Some functions may not be known yet, missing functions should not raise errors.
     * Since unknown functions have unset execution modes, errors should not be raised for accessing these.
     */
    static final VisitorConfig EXECUTION_MODE_INITIAL_PASS = VisitorConfig.builder()
            .suppressErrorsForCallingMissingFunctions(true)
            .suppressErrorsForAccessingUnsetExecutionModes(true)
            .build();

    /**
     * Intermediate passes should update the execution modes of expressions as more UDFs can be resolved.
     * As all UDFs should be known at this stage, missing functions should raise errors
     * As UDFs may still have unresolved execution modes, errors should not be raised for accessing these.
     */
    static final VisitorConfig EXECUTION_MODE_INTERMEDIATE_PASS = VisitorConfig.builder()
            .suppressErrorsForFunctionSignatureCollision(true)
            .suppressErrorsForAccessingUnsetExecutionModes(true)
            .build();

    /**
     * All expression execution mode and UDF information should be available in the final pass
     */
    static final VisitorConfig EXECUTION_MODE_FINAL_PASS = VisitorConfig.builder()
            .suppressErrorsForFunctionSignatureCollision(true)
            .setUnsetExecutionModeOfVariableReferenceExpressionsToLocal(true)
            .build();

    static final VisitorConfig RUNTIME_PLAN_GENERATION = VisitorConfig.builder().build();
}
