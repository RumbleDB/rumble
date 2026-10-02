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
package org.rumbledb.runtime.functions.dataframe;

import java.io.Serial;
import java.util.List;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.InvalidSelectorException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.items.structured.HomogeneousItemDataFrame;
import org.rumbledb.runtime.cursor.AtMostOneLocalCursor;
import org.rumbledb.runtime.cursor.Cursor;
import org.rumbledb.runtime.dataframe.ItemRuntimeDataFrameFactory;
import org.rumbledb.runtime.plan.DataFrameRuntimePlan;
import org.rumbledb.runtime.plan.ItemRuntimePlan;
import org.rumbledb.runtime.plan.LocalRuntimePlan;

public class DropColumnsIterator extends ItemRuntimePlan implements LocalRuntimePlan<Item>, DataFrameRuntimePlan<Item> {

    @Override
    public Cursor<Item> createNativeCursor(DynamicContext context) {
        return new AtMostOneLocalCursor<>(null, this.getRuntimeStaticContext().getMetadata());
    }

    @Serial
    private static final long serialVersionUID = 1L;

    public DropColumnsIterator(List<ItemRuntimePlan> children, RuntimeStaticContext staticContext) {
        super(children, staticContext);
    }

    @Override
    public HomogeneousItemDataFrame createNativeDataFrame(DynamicContext context) {
        HomogeneousItemDataFrame dataFrame = ItemRuntimeDataFrameFactory.INSTANCE.fromPlan(this.getChild(0), context);
        List<Item> columnsToDropItems = this.getChild(1).materialize(context);
        if (columnsToDropItems.isEmpty()) {
            throw new InvalidSelectorException(
                    "Invalid drop-columns parameter; drop-columns can't be performed without string columns to be removed.",
                    getMetadata());
        }
        String[] columnsToDrop = new String[columnsToDropItems.size()];
        int i = 0;
        for (Item columnItem : columnsToDropItems) {
            if (!columnItem.isString()) {
                throw new UnexpectedTypeException("drop-columns invoked with non-string columns", getMetadata());
            }
            columnsToDrop[i] = columnItem.getStringValue();
            ++i;
        }
        return new HomogeneousItemDataFrame(dataFrame.getDataFrame().drop(columnsToDrop), dataFrame.getItemType());
    }
}
