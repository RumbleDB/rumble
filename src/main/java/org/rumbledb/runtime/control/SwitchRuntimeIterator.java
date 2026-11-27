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
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.apache.spark.api.java.JavaRDD;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.CannotAtomizeException;
import org.rumbledb.exceptions.ExceptionMetadata;
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
        return new SwitchLocalCursor(context);
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

    private static Item materializeAtomizedOperand(
            ItemRuntimePlan plan, DynamicContext context, String role, ExceptionMetadata metadata) {
        Item result = null;
        try (Cursor<Item> cursor = plan.getCursor(context)) {
            while (cursor.hasNext()) {
                Item value = atomizeOperand(cursor.next(), role, metadata);
                if (value == null) {
                    continue;
                }
                if (result != null) {
                    throw new UnexpectedTypeException(role + " must atomize to at most one item.", metadata);
                }
                result = value;
            }
        }
        return result;
    }

    private final class SwitchLocalCursor extends AbstractLocalCursor<Item> {
        private final DynamicContext context;
        private Cursor<Item> selected;

        private SwitchLocalCursor(DynamicContext context) {
            super(SwitchRuntimeIterator.this.getMetadata());
            this.context = context;
        }

        @Override
        protected void openLocal() {
            this.selected = selectApplicableIterator(this.context).getCursor(this.context);
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
    }

    private ItemRuntimePlan selectApplicableIterator(DynamicContext dynamicContext) {
        ExceptionMetadata metadata = getMetadata();
        String collation = this.staticContext.getDefaultCollation();
        ZoneOffset timezone = dynamicContext.getImplicitTimezone();
        Item testValue = materializeAtomizedOperand(this.testField, dynamicContext, "Switch condition", metadata);
        for (Map.Entry<ItemRuntimePlan, ItemRuntimePlan> entry : this.cases.entrySet()) {
            Item caseValue = materializeAtomizedOperand(entry.getKey(), dynamicContext, "Switch case", metadata);
            boolean matches = testValue == null || caseValue == null
                    ? testValue == caseValue
                    : AtomicDeepEqual.deepEqual(testValue, caseValue, collation, metadata, timezone);
            if (matches) {
                return entry.getValue();
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
