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
package org.rumbledb.compiler.typing;

import java.util.Optional;

import org.rumbledb.expressions.primary.FunctionCallExpression;
import org.rumbledb.types.SequenceType;

/**
 * Infers the return type of a call of a builtin function more precisely than its signature does. A rule is attached
 * to the function's entry in the catalogue. One written for a single function is a static method of its iterator; one
 * shared by several functions is in {@link ReturnTypeRules}.
 */
@FunctionalInterface
public interface ReturnTypeRule {

    /** The rule of a function whose signature is as precise as its return type can be. */
    ReturnTypeRule NONE = (call, context) -> Optional.empty();

    /**
     * Infers the return type of a call whose arguments are typed, or returns empty to keep the signature's.
     *
     * @param call a call of the function the rule is attached to, with as many arguments as its signature
     * @param context the effective argument types (including implicit context inputs) and typing services; use
     *        these types rather than indexing the source arguments when a rule also serves a context overload
     */
    Optional<SequenceType> infer(FunctionCallExpression call, ReturnTypeContext context);
}
