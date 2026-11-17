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
package org.rumbledb.runtime.plan;

import org.rumbledb.context.DynamicContext;

/**
 * Native local execution capability for a plan that produces at most one value.
 *
 * @param <T> the value type
 */
public interface AtMostOneLocalRuntimePlan<T> extends LocalRuntimePlan<T> {

    /**
     * Evaluates the plan without allocating a cursor.
     *
     * @param context the dynamic context for the evaluation
     * @return the value, or {@code null} for the empty sequence
     */
    T evaluateAtMostOne(DynamicContext context);
}
