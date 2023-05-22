package org.rumbledb.runtime.update.expression;

import org.apache.spark.api.java.JavaRDD;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.exceptions.*;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.runtime.HybridRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.update.PendingUpdateList;
import org.rumbledb.runtime.update.primitives.UpdatePrimitive;
import org.rumbledb.runtime.update.primitives.UpdatePrimitiveFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class InsertExpressionIterator extends HybridRuntimeIterator {

    private RuntimeIterator mainIterator;
    private RuntimeIterator toInsertIterator;
    private RuntimeIterator positionIterator;

    public InsertExpressionIterator(RuntimeIterator mainIterator, RuntimeIterator toInsertIterator, RuntimeIterator positionIterator, ExecutionMode executionMode, ExceptionMetadata iteratorMetadata) {
        super(
                positionIterator == null
                        ? Arrays.asList(mainIterator, toInsertIterator)
                        : Arrays.asList(mainIterator, toInsertIterator, positionIterator),
                executionMode,
                iteratorMetadata
        );

        this.mainIterator = mainIterator;
        this.toInsertIterator = toInsertIterator;
        this.positionIterator = positionIterator;
        this.isUpdating = true;
    }

    public boolean hasPositionIterator() {
        return positionIterator != null;
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
        Item content;
        Item locator = null;

        try {
            main = this.mainIterator.materializeExactlyOneItem(context);
            content = this.toInsertIterator.materializeExactlyOneItem(context);
            if (this.hasPositionIterator()) {
                locator = this.positionIterator.materializeExactlyOneItem(context);
            }
        } catch (NoItemException e) {
            throw new UpdateTargetIsEmptySeqException("Target of insert expression is empty", this.getMetadata());
        } catch (MoreThanOneItemException e) {
            throw new RuntimeException(e);
        }

        UpdatePrimitiveFactory factory = UpdatePrimitiveFactory.getInstance();
        UpdatePrimitive up;
        if (main.isObject()) {
            up = factory.createInsertIntoObjectPrimitive(main, content);
        } else if (main.isArray()) {
            up = factory.createInsertIntoArrayPrimitive(main, locator, Collections.singletonList(content));
        } else {
            throw new OurBadException("Insert iterator cannot handle main items that are not objects or arrays");
        }

        pul.addUpdatePrimitive(up);
        return pul;
    }
}
