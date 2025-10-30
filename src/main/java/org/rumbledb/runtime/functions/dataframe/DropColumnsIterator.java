package org.rumbledb.runtime.functions.dataframe;

import org.apache.spark.api.java.JavaRDD;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.InvalidSelectorException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.items.structured.JSoundDataFrame;
import org.rumbledb.runtime.HybridRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;

import java.util.List;

public class DropColumnsIterator extends HybridRuntimeIterator {
    private static final long serialVersionUID = 1L;

    public DropColumnsIterator(List<RuntimeIterator> children, RuntimeStaticContext staticContext) {
        super(children, staticContext);
    }

    @Override
    public JavaRDD<Item> getRDDAux(DynamicContext context) {
        return null;
    }

    @Override
    public void openLocal() {

    }

    @Override
    public void closeLocal() {

    }

    @Override
    public void resetLocal() {

    }

    @Override
    public boolean hasNextLocal() {
        return false;
    }

    @Override
    public Item nextLocal() {
        return null;
    }

    public JSoundDataFrame getDataFrame(DynamicContext context) {
        JSoundDataFrame dataFrame = this.children.get(0).getDataFrame(context);
        List<Item> columnsToDropItems = this.children.get(1).materialize(context);
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
        return new JSoundDataFrame(dataFrame.getDataFrame().drop(columnsToDrop), dataFrame.getItemType());
    }
}
