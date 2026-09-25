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
package org.rumbledb.runtime.update.expression;

import java.io.Serial;
import java.util.Arrays;
import java.util.Collections;

import org.apache.commons.lang3.SerializationUtils;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.InvalidUpdateTargetException;
import org.rumbledb.exceptions.ModifiesImmutableValueException;
import org.rumbledb.exceptions.MoreThanOneItemException;
import org.rumbledb.exceptions.NoItemException;
import org.rumbledb.exceptions.TransformModifiesNonCopiedValueException;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.plan.ItemRuntimePlan;
import org.rumbledb.runtime.update.PendingUpdateList;
import org.rumbledb.runtime.update.primitives.UpdatePrimitive;
import org.rumbledb.runtime.update.primitives.UpdatePrimitiveFactory;

public class AppendExpressionIterator extends UpdatingExpressionIterator {

    @Serial
    private static final long serialVersionUID = 1L;

    private final ItemRuntimePlan arrayIterator;
    private final ItemRuntimePlan toAppendIterator;

    public AppendExpressionIterator(
            ItemRuntimePlan arrayIterator, ItemRuntimePlan toAppendIterator, RuntimeStaticContext staticContext) {
        super(
                Arrays.asList(arrayIterator, toAppendIterator),
                staticContext.toBuilder().isUpdating(true).build());

        this.arrayIterator = arrayIterator;
        this.toAppendIterator = toAppendIterator;
    }

    @Override
    public PendingUpdateList getPendingUpdateList(DynamicContext context) {
        PendingUpdateList pul = new PendingUpdateList();
        Item target;
        Item content;

        try {
            target = this.arrayIterator.materializeExactlyOne(context);
            content = SerializationUtils.clone(this.toAppendIterator.materializeExactlyOne(context));
        } catch (NoItemException | MoreThanOneItemException e) {
            throw new RuntimeException(e);
        }

        UpdatePrimitiveFactory factory = UpdatePrimitiveFactory.getInstance();
        UpdatePrimitive up;
        if (target.isArray()) {
            Item locator = ItemFactory.getInstance().createIntItem(target.getSize() + 1);
            if (context.getCurrentMutabilityLevel() == 0 && target.getMutabilityLevel() == -1) {
                throw new ModifiesImmutableValueException(
                        "Attempt to modify immutable target",
                        this.getRuntimeStaticContext().getMetadata());
            }
            if (target.getMutabilityLevel() != context.getCurrentMutabilityLevel()) {
                throw new TransformModifiesNonCopiedValueException(
                        "Attempt to modify currently immutable target",
                        this.getRuntimeStaticContext().getMetadata());
            }
            up = factory.createInsertIntoArrayPrimitive(
                    target,
                    locator,
                    Collections.singletonList(content),
                    this.getRuntimeStaticContext().getMetadata());
        } else {
            throw new InvalidUpdateTargetException(
                    "Append expression target must be a single array",
                    this.getRuntimeStaticContext().getMetadata());
        }

        pul.addUpdatePrimitive(up);
        return pul;
    }
}
