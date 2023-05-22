package org.rumbledb.runtime.update.expression;

import org.apache.spark.api.java.JavaRDD;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.exceptions.*;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.HybridRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.update.PendingUpdateList;
import org.rumbledb.runtime.update.primitives.UpdatePrimitive;
import org.rumbledb.runtime.update.primitives.UpdatePrimitiveFactory;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AppendExpressionIterator extends HybridRuntimeIterator {

    private RuntimeIterator arrayIterator;
    private RuntimeIterator toAppendIterator;

    public AppendExpressionIterator(RuntimeIterator arrayIterator, RuntimeIterator toAppendIterator, ExecutionMode executionMode, ExceptionMetadata iteratorMetadata) {
        super(Arrays.asList(arrayIterator, toAppendIterator), executionMode, iteratorMetadata);

        this.arrayIterator = arrayIterator;
        this.toAppendIterator = toAppendIterator;
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
        Item content;

        try {
            target = this.arrayIterator.materializeExactlyOneItem(context);
            content = this.toAppendIterator.materializeExactlyOneItem(context);
        } catch (NoItemException | MoreThanOneItemException e) {
            throw new RuntimeException(e);
        }

        UpdatePrimitiveFactory factory = UpdatePrimitiveFactory.getInstance();
        UpdatePrimitive up;
        if (target.isArray()) {
            Item locator = ItemFactory.getInstance().createIntItem(target.getSize() + 1);
            up = factory.createInsertIntoArrayPrimitive(target, locator, Collections.singletonList(content));
        } else {
            throw new InvalidUpdateTargetException("Append expression target must be a single array", this.getMetadata());
        }

        pul.addUpdatePrimitive(up);
        return pul;
    }
}
