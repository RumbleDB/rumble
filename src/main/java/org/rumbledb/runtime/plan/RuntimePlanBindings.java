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
import org.rumbledb.context.Name;
import org.rumbledb.runtime.dataframe.ItemRuntimeDataFrameFactory;

/**
 * Binds a plan result using the representation selected by its runtime static context.
 */
public final class RuntimePlanBindings {

    private RuntimePlanBindings() {}

    public static void bind(
            ItemRuntimePlan plan, DynamicContext targetContext, Name variable, DynamicContext executionContext) {
        if (plan.getRuntimeStaticContext().getExecutionMode().isDataFrame()) {
            targetContext
                    .getVariableValues()
                    .addVariableValue(variable, ItemRuntimeDataFrameFactory.INSTANCE.fromPlan(plan, executionContext));
        } else if (plan.getRuntimeStaticContext().getExecutionMode().isRDDOrDataFrame()) {
            targetContext.getVariableValues().addVariableValue(variable, plan.getRDD(executionContext));
        } else {
            targetContext.getVariableValues().addVariableValue(variable, plan.materialize(executionContext));
        }
    }
}
