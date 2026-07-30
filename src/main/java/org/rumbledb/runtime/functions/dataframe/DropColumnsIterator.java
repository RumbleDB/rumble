package org.rumbledb.runtime.functions.dataframe;

import org.rumbledb.runtime.HybridRuntimeIterator;

import org.apache.spark.api.java.JavaRDD;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.InvalidSelectorException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.items.structured.HomogeneousItemDataFrame;
import org.rumbledb.runtime.plan.DataFrameRuntimePlan;
import org.rumbledb.runtime.cursor.EmptyLocalCursor;
import org.rumbledb.runtime.cursor.Cursor;

import java.io.Serial;
import java.util.List;

public class DropColumnsIterator extends HybridRuntimeIterator
        implements
            DataFrameRuntimePlan<Item> {

    @Override
    public Cursor<Item> createNativeCursor(DynamicContext context) {
        return new EmptyLocalCursor<>(this.getRuntimeStaticContext().getMetadata());
    }

    @Serial
    private static final long serialVersionUID = 1L;

    public DropColumnsIterator(
            List<org.rumbledb.runtime.plan.RuntimePlan<org.rumbledb.api.Item>> children,
            RuntimeStaticContext staticContext
    ) {
        super(children, staticContext);
    }

    @Override
    public JavaRDD<Item> getRDDAux(DynamicContext context) {
        return null;
    }

    @Override
    public HomogeneousItemDataFrame getNativeDataFrame(DynamicContext context) {
        HomogeneousItemDataFrame dataFrame = org.rumbledb.runtime.dataframe.ItemRuntimeDataFrameFactory.INSTANCE
            .fromPlan(this.getChild(0), context);
        List<Item> columnsToDropItems = this.getChild(1).materialize(context);
        if (columnsToDropItems.isEmpty()) {
            throw new InvalidSelectorException(
                    "Invalid drop-columns parameter; drop-columns can't be performed without string columns to be removed.",
                    getMetadata()
            );
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
