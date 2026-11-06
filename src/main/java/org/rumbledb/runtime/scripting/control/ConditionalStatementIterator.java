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
package org.rumbledb.runtime.scripting.control;

import java.io.Serial;
import java.util.List;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.runtime.AbstractAtMostOneItemRuntimePlan;
import org.rumbledb.runtime.EffectiveBooleanValue;
import org.rumbledb.runtime.plan.ItemRuntimePlan;

public class ConditionalStatementIterator extends AbstractAtMostOneItemRuntimePlan {
    @Serial
    private static final long serialVersionUID = 1L;

    public ConditionalStatementIterator(List<? extends ItemRuntimePlan> children, RuntimeStaticContext staticContext) {
        super(children, staticContext);
    }

    private ItemRuntimePlan selectApplicableIterator(DynamicContext dynamicContext) {
        ItemRuntimePlan condition = this.getChild(0);
        boolean effectiveBooleanValue = EffectiveBooleanValue.evaluate(condition, dynamicContext);
        if (effectiveBooleanValue) {
            return this.getChild(1);
        } else {
            return this.getChild(2);
        }
    }

    @Override
    public Item evaluateAtMostOne(DynamicContext dynamicContext) {
        ItemRuntimePlan selectedIterator = selectApplicableIterator(dynamicContext);
        DynamicContext childDynamicContext = new DynamicContext(dynamicContext);
        selectedIterator.materialize(childDynamicContext);
        return null;
    }
}
