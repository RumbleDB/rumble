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
package org.rumbledb.runtime.typing;

import java.io.Serial;
import java.util.Collections;
import java.util.List;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.CastableException;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.UnknownCastTypeException;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AbstractAtMostOneItemRuntimePlan;
import org.rumbledb.runtime.plan.ItemRuntimePlan;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.ItemType;
import org.rumbledb.types.SequenceType;
import org.rumbledb.types.SequenceType.Arity;

public class CastableIterator extends AbstractAtMostOneItemRuntimePlan {
    @Serial
    private static final long serialVersionUID = 1L;

    private final ItemRuntimePlan child;
    private final SequenceType sequenceType;

    public CastableIterator(ItemRuntimePlan child, SequenceType sequenceType, RuntimeStaticContext staticContext) {
        super(Collections.singletonList(child), staticContext);
        this.child = child;
        this.sequenceType = sequenceType;
    }

    @Override
    public Item evaluateAtMostOne(DynamicContext dynamicContext) {
        ItemRuntimePlan child = this.child;
        SequenceType sequenceType = this.sequenceType;
        RuntimeStaticContext staticContext = this.staticContext;
        ExceptionMetadata metadata = getMetadata();
        if (!sequenceType.isResolved()) {
            sequenceType.resolve(dynamicContext, metadata);
        }
        ItemType targetItemType = sequenceType.getItemType();
        boolean validCastTarget = targetItemType.isAtomicItemType()
                || (targetItemType.isUnionType()
                        && targetItemType.getTypes().stream().allMatch(ItemType::isAtomicItemType));
        if (!validCastTarget) {
            throw new UnknownCastTypeException(
                    "The type "
                            + targetItemType.getIdentifierString()
                            + " is not atomic. Castable can only be used with atomic types.",
                    metadata);
        }
        // Invalid targets and failures evaluating or atomizing the operand are errors,
        // not a false castability result. Only conversion failures are caught below.
        if (targetItemType.equals(BuiltinTypesCatalogue.NOTATIONItem)
                || targetItemType.equals(BuiltinTypesCatalogue.atomicItem)) {
            throw new CastableException("Invalid target type for castable expression: " + targetItemType, metadata);
        }
        List<Item> atomized = CastAtomization.materializeAtomizedAtMostTwo(child, dynamicContext, metadata);
        if (atomized.size() > 1) {
            return ItemFactory.getInstance().createBooleanItem(false);
        }
        if (atomized.isEmpty()) {
            return ItemFactory.getInstance()
                    .createBooleanItem(sequenceType.getArity().equals(Arity.OneOrZero));
        }
        try {
            Item res =
                    CastIterator.castItemToType(atomized.get(0), sequenceType.getItemType(), metadata, staticContext);
            return ItemFactory.getInstance().createBooleanItem(res != null);
        } catch (Exception e) {
            return ItemFactory.getInstance().createBooleanItem(false);
        }
    }
}
