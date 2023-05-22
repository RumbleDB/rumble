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

import java.util.Arrays;
import java.util.List;

public class ReplaceExpressionIterator extends HybridRuntimeIterator {

    private RuntimeIterator mainIterator;
    private RuntimeIterator locatorIterator;
    private RuntimeIterator replacerIterator;

    public ReplaceExpressionIterator(RuntimeIterator mainIterator, RuntimeIterator locatorIterator, RuntimeIterator replacerIterator, ExecutionMode executionMode, ExceptionMetadata iteratorMetadata) {
        super(Arrays.asList(mainIterator, locatorIterator, replacerIterator), executionMode, iteratorMetadata);

        this.mainIterator = mainIterator;
        this.locatorIterator = locatorIterator;
        this.replacerIterator = replacerIterator;
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
        Item target;
        Item locator;
        Item content;

        try {
            target = this.mainIterator.materializeExactlyOneItem(context);
            locator = this.locatorIterator.materializeExactlyOneItem(context);
            content = this.replacerIterator.materializeExactlyOneItem(context);
        } catch (NoItemException e) {
            throw new UpdateTargetIsEmptySeqException("Target of replace expression is empty", this.getMetadata());
        } catch (MoreThanOneItemException e) {
            throw new RuntimeException(e);
        }

        UpdatePrimitiveFactory factory = UpdatePrimitiveFactory.getInstance();
        UpdatePrimitive up;
        if (target.isObject()) {
            if (!locator.isString()) {
                throw new CannotCastUpdateSelectorException("Replace expression selection cannot be cast to String type", this.getMetadata());
            }
            up = factory.createReplaceInObjectPrimitive(target, locator, content, this.getMetadata());
        } else if (target.isArray()) {
            if (!locator.isInt()) {
                throw new CannotCastUpdateSelectorException("Replace expression selection cannot be cast to Int type", this.getMetadata());
            }
            up = factory.createReplaceInArrayPrimitive(target, locator, content, this.getMetadata());
        } else {
            throw new InvalidUpdateTargetException("Replace expression target must be a single array or object", this.getMetadata());
        }

        pul.addUpdatePrimitive(up);
        return pul;
    }
}
