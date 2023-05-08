package org.rumbledb.runtime.update.expression;

import org.apache.spark.api.java.JavaRDD;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.MoreThanOneItemException;
import org.rumbledb.exceptions.NoItemException;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.runtime.HybridRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.update.PendingUpdateList;
import org.rumbledb.runtime.update.primitives.UpdatePrimitive;
import org.rumbledb.runtime.update.primitives.UpdatePrimitiveFactory;

import java.util.Arrays;
import java.util.Collections;

public class DeleteExpressionIterator extends HybridRuntimeIterator {

    private RuntimeIterator mainIterator;
    private RuntimeIterator lookupIterator;

    public DeleteExpressionIterator(RuntimeIterator mainIterator, RuntimeIterator lookupIterator, ExecutionMode executionMode, ExceptionMetadata iteratorMetadata) {
        super(Arrays.asList(mainIterator, lookupIterator), executionMode, iteratorMetadata);
        this.mainIterator = mainIterator;
        this.lookupIterator = lookupIterator;
        this.isUpdating = true;
    }

    @Override
    protected JavaRDD<Item> getRDDAux(DynamicContext context) {
        return null;
    }

    @Override
    protected void openLocal() {

    }

    @Override
    protected void closeLocal() {

    }

    @Override
    protected void resetLocal() {

    }

    @Override
    protected boolean hasNextLocal() {
        return false;
    }

    @Override
    protected Item nextLocal() {
        return null;
    }

    @Override
    public PendingUpdateList getPendingUpdateList(DynamicContext context) {
        PendingUpdateList pul = new PendingUpdateList();
        Item main;
        Item lookup;

        try {
            main = this.mainIterator.materializeExactlyOneItem(context);
            lookup = this.lookupIterator.materializeExactlyOneItem(context);
        } catch (NoItemException | MoreThanOneItemException e) {
            throw new RuntimeException(e);
        }

        UpdatePrimitiveFactory factory = UpdatePrimitiveFactory.getInstance();
        UpdatePrimitive up;
        if (main.isObject()) {
            up = factory.createDeleteFromObjectPrimitive(main, Collections.singletonList(lookup));
        } else if (main.isArray()) {
            up = factory.createDeleteFromArrayPrimitive(main, lookup);
        } else {
            throw new OurBadException("Delete iterator cannot handle main items that are not objects or arrays");
        }

        pul.addUpdatePrimitive(up);
        return pul;
    }
}
