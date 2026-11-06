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
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.runtime.update.PendingUpdateList;

/**
 * Capability for a plan that produces pending updates.
 */
public interface UpdatingRuntimePlan {

    PendingUpdateList getPendingUpdateList(DynamicContext context);

    static PendingUpdateList get(RuntimePlan<?> plan, DynamicContext context) {
        if (plan instanceof UpdatingRuntimePlan updatingPlan) {
            return updatingPlan.getPendingUpdateList(context);
        }
        throw new OurBadException(
                "The runtime plan " + plan.getClass().getCanonicalName() + " does not support pending updates.",
                plan.getRuntimeStaticContext().getMetadata());
    }
}
