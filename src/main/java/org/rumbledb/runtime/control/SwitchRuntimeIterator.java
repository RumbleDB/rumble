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
package org.rumbledb.runtime.control;

import java.io.Serial;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.apache.spark.api.java.JavaRDD;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.CannotAtomizeException;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.MoreThanOneItemException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.items.structured.HomogeneousItemDataFrame;
import org.rumbledb.runtime.cursor.AbstractLocalCursor;
import org.rumbledb.runtime.cursor.Cursor;
import org.rumbledb.runtime.dataframe.ItemRuntimeDataFrameFactory;
import org.rumbledb.runtime.misc.AtomicDeepEqual;
import org.rumbledb.runtime.plan.DataFrameRuntimePlan;
import org.rumbledb.runtime.plan.ItemRuntimePlan;
import org.rumbledb.runtime.plan.LocalRuntimePlan;
import org.rumbledb.runtime.plan.RDDRuntimePlan;

public class SwitchRuntimeIterator extends ItemRuntimePlan
        implements LocalRuntimePlan<Item>, RDDRuntimePlan<Item>, DataFrameRuntimePlan<Item> {

    @Serial
    private static final long serialVersionUID = 1L;

    private final ItemRuntimePlan testField;
    private final Map<ItemRuntimePlan, ItemRuntimePlan> cases;
    private final ItemRuntimePlan defaultReturn;

    public SwitchRuntimeIterator(
            ItemRuntimePlan test,
            Map<ItemRuntimePlan, ItemRuntimePlan> cases,
            ItemRuntimePlan defaultReturn,
            RuntimeStaticContext staticContext) {
        super(
                Stream.concat(
                                Stream.concat(Stream.of(test), cases.keySet().stream()),
                                Stream.concat(cases.values().stream(), Stream.of(defaultReturn)))
                        .toList(),
                staticContext);
        this.testField = test;
        this.cases = cases;
        this.defaultReturn = defaultReturn;
    }

    @Override
    public Cursor<Item> createNativeCursor(DynamicContext context) {
        return new SwitchLocalCursor(this.testField, this.cases, this.defaultReturn, context, getMetadata());
    }

    // Nodes atomize to their typed value and arrays to the atomized values of their members; objects, maps and other
    // function items cannot be atomized (FOTY0013).
    public static Item atomizeOperand(Item item, String role, ExceptionMetadata metadata) {
        if (item == null || item.isAtomic()) {
            return item;
        }
        List<Item> typedValue;
        try {
            typedValue = item.atomizedValue();
        } catch (CannotAtomizeException e) {
            throw new CannotAtomizeException(role + " cannot be atomized: " + e.getJSONiqErrorMessage(), metadata);
        }
        if (typedValue.size() > 1) {
            throw new UnexpectedTypeException(role + " must atomize to at most one item.", metadata);
        }
        return typedValue.isEmpty() ? null : typedValue.get(0);
    }

    private static Item materializeOperand(
            ItemRuntimePlan plan, DynamicContext context, String role, ExceptionMetadata metadata) {
        try {
            return plan.materializeAtMostOne(context);
        } catch (MoreThanOneItemException e) {
            throw new UnexpectedTypeException(role + " must contain at most one item.", metadata);
        }
    }

    private static final class SwitchLocalCursor extends AbstractLocalCursor<Item> {
        private final ItemRuntimePlan testPlan;
        private final Map<ItemRuntimePlan, ItemRuntimePlan> cases;
        private final ItemRuntimePlan defaultPlan;
        private final DynamicContext context;
        private final ExceptionMetadata metadata;
        private Cursor<Item> selected;

        private SwitchLocalCursor(
                ItemRuntimePlan testPlan,
                Map<ItemRuntimePlan, ItemRuntimePlan> cases,
                ItemRuntimePlan defaultPlan,
                DynamicContext context,
                ExceptionMetadata metadata) {
            super(metadata);
            this.testPlan = testPlan;
            this.cases = cases;
            this.defaultPlan = defaultPlan;
            this.context = context;
            this.metadata = metadata;
        }

        @Override
        protected void openLocal() {
            this.selected = selectApplicablePlan().getCursor(this.context);
        }

        @Override
        protected boolean hasNextLocal() {
            return this.selected.hasNext();
        }

        @Override
        protected Item nextLocal() {
            return this.selected.next();
        }

        @Override
        protected void closeLocal() {
            if (this.selected != null) {
                this.selected.close();
                this.selected = null;
            }
        }

        private ItemRuntimePlan selectApplicablePlan() {
            Item testValue = materializeOperand(this.testPlan, this.context, "Switch condition", this.metadata);
            testValue = atomizeOperand(testValue, "Switch condition", this.metadata);
            for (ItemRuntimePlan caseKey : this.cases.keySet()) {
                Item caseValue = materializeOperand(caseKey, this.context, "Switch case", this.metadata);
                caseValue = atomizeOperand(caseValue, "Switch case", this.metadata);
                if (testValue == null) {
                    if (caseValue == null) {
                        return this.cases.get(caseKey);
                    }
                    continue;
                }
                if (caseValue == null) {
                    continue;
                }
                if (AtomicDeepEqual.deepEqual(testValue, caseValue)) {
                    return this.cases.get(caseKey);
                }
            }
            return this.defaultPlan;
        }
    }

    private ItemRuntimePlan selectApplicableIterator(DynamicContext dynamicContext) {
        Item testValue = materializeOperand(this.testField, dynamicContext, "Switch condition", getMetadata());

        testValue = atomizeOperand(testValue, "Switch condition", getMetadata());

        for (ItemRuntimePlan caseKey : this.cases.keySet()) {
            Item caseValue = materializeOperand(caseKey, dynamicContext, "Switch case", getMetadata());

            caseValue = atomizeOperand(caseValue, "Switch case", getMetadata());

            // both are empty sequences
            if (testValue == null) {
                if (caseValue == null) {
                    return this.cases.get(caseKey);
                } else {
                    continue;
                }
            }
            if (caseValue == null) {
                continue;
            }
            if (AtomicDeepEqual.deepEqual(testValue, caseValue)) {
                return this.cases.get(caseKey);
            }
        }

        return this.defaultReturn;
    }

    @Override
    public JavaRDD<Item> createNativeRDD(DynamicContext dynamicContext) {
        ItemRuntimePlan iterator = selectApplicableIterator(dynamicContext);

        return iterator.getRDD(dynamicContext);
    }

    @Override
    public HomogeneousItemDataFrame createNativeDataFrame(DynamicContext dynamicContext) {
        ItemRuntimePlan iterator = selectApplicableIterator(dynamicContext);

        return ItemRuntimeDataFrameFactory.INSTANCE.fromPlan(iterator, dynamicContext);
    }
}
