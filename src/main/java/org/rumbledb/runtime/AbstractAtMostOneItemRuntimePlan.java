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
package org.rumbledb.runtime;

import java.io.Serial;
import java.util.List;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.runtime.cursor.AtMostOneLocalCursor;
import org.rumbledb.runtime.cursor.Cursor;
import org.rumbledb.runtime.plan.AtMostOneLocalRuntimePlan;
import org.rumbledb.runtime.plan.ItemRuntimePlan;

/**
 * Item plan that evaluates at most one item and exposes that evaluation as a native cursor.
 */
public abstract class AbstractAtMostOneItemRuntimePlan extends ItemRuntimePlan
        implements AtMostOneLocalRuntimePlan<Item> {

    @Serial
    private static final long serialVersionUID = 1L;

    protected AbstractAtMostOneItemRuntimePlan(
            List<? extends ItemRuntimePlan> children, RuntimeStaticContext staticContext) {
        super(children, staticContext);
    }

    @Override
    public final Cursor<Item> createNativeCursor(DynamicContext context) {
        return new AtMostOneLocalCursor<>(this.evaluateAtMostOne(context), this.getMetadata());
    }

    @Override
    public abstract Item evaluateAtMostOne(DynamicContext context);
}
