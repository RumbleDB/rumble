package org.rumbledb.runtime.functions.sequences.general;

import org.apache.spark.api.java.JavaRDD;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.items.structured.JSoundDataFrame;
import org.rumbledb.runtime.HybridRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;

import java.util.Collections;
import java.util.List;

/**
 * A local sequence iterator whose contents can be swapped out with {@link #setItems(List)} between
 * open/close cycles, so a single instance can be reused as an argument to repeated function calls
 * instead of rebuilding an argument iterator for every call.
 */
public class MaterializedItemsRuntimeIterator extends HybridRuntimeIterator {
    private static final long serialVersionUID = 1L;

    private List<Item> items = Collections.emptyList();
    private int index;

    public MaterializedItemsRuntimeIterator(RuntimeStaticContext staticContext) {
        super(null, staticContext);
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    @Override
    protected void openLocal() {
        this.index = 0;
        this.hasNext = !this.items.isEmpty();
    }

    @Override
    protected boolean hasNextLocal() {
        return this.hasNext;
    }

    @Override
    protected Item nextLocal() {
        if (!this.hasNext) {
            throw new IteratorFlowException(RuntimeIterator.FLOW_EXCEPTION_MESSAGE, getMetadata());
        }
        Item result = this.items.get(this.index++);
        if (this.index >= this.items.size()) {
            this.hasNext = false;
        }
        return result;
    }

    @Override
    protected void resetLocal() {
        this.index = 0;
        this.hasNext = !this.items.isEmpty();
    }

    @Override
    protected void closeLocal() {
        this.index = 0;
    }

    @Override
    protected boolean implementsDataFrames() {
        return false;
    }

    @Override
    public JavaRDD<Item> getRDDAux(DynamicContext context) {
        throw new OurBadException(
                "MaterializedItemsRuntimeIterator is currently supported only in local execution mode."
        );
    }

    @Override
    public JSoundDataFrame getDataFrame(DynamicContext dynamicContext) {
        throw new OurBadException(
                "MaterializedItemsRuntimeIterator is currently supported only in local execution mode."
        );
    }
}
