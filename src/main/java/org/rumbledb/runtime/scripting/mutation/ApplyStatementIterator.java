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
package org.rumbledb.runtime.scripting.mutation;

import java.io.Serial;
import java.util.Collections;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.runtime.AbstractAtMostOneItemRuntimePlan;
import org.rumbledb.runtime.plan.ItemRuntimePlan;
import org.rumbledb.runtime.plan.UpdatingRuntimePlan;
import org.rumbledb.runtime.update.PendingUpdateList;

public class ApplyStatementIterator extends AbstractAtMostOneItemRuntimePlan implements UpdatingRuntimePlan {
    @Serial
    private static final long serialVersionUID = 1L;

    private final ItemRuntimePlan exprIterator;

    public ApplyStatementIterator(ItemRuntimePlan exprIterator, RuntimeStaticContext staticContext) {
        super(Collections.singletonList(exprIterator), staticContext);
        this.exprIterator = exprIterator;
    }

    @Override
    public Item evaluateAtMostOne(DynamicContext context) {
        this.exprIterator.materialize(context);
        applyUpdates(context);
        return null;
    }

    private void applyUpdates(DynamicContext context) {
        // Immediately apply pul if applicable
        if (this.exprIterator.getRuntimeStaticContext().isUpdating()) {
            UpdatingRuntimePlan.get(this.exprIterator, context)
                    .applyUpdates(this.getRuntimeStaticContext().getMetadata());
        }
    }

    @Override
    public PendingUpdateList getPendingUpdateList(DynamicContext context) {
        return new PendingUpdateList();
    }
}
