package org.rumbledb.runtime.update.expression;

import org.apache.spark.api.java.JavaRDD;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.MoreThanOneItemException;
import org.rumbledb.exceptions.NoItemException;
import org.rumbledb.exceptions.OurBadException;
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

public class RenameExpressionIterator extends HybridRuntimeIterator {

    private RuntimeIterator mainIterator;
    private RuntimeIterator locatorIterator;
    private RuntimeIterator nameIterator;
    private PendingUpdateList pul;

    public RenameExpressionIterator(RuntimeIterator mainIterator, RuntimeIterator locatorIterator, RuntimeIterator nameIterator, ExecutionMode executionMode, ExceptionMetadata iteratorMetadata) {
        super(Arrays.asList(mainIterator, locatorIterator, nameIterator), executionMode, iteratorMetadata);

        this.mainIterator = mainIterator;
        this.locatorIterator = locatorIterator;
        this.nameIterator = nameIterator;
        this.pul = null;
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
        if (this.pul == null) {
            PendingUpdateList pul = new PendingUpdateList();
            Item target;
            Item locator;
            Item content;

            try {
                target = this.mainIterator.materializeExactlyOneItem(context);
                locator = this.locatorIterator.materializeExactlyOneItem(context);
                content = this.nameIterator.materializeExactlyOneItem(context);
            } catch (NoItemException | MoreThanOneItemException e) {
                throw new RuntimeException(e);
            }

            UpdatePrimitiveFactory factory = UpdatePrimitiveFactory.getInstance();
            UpdatePrimitive up;
            if (target.isObject()) {
                up = factory.createRenameInObjectPrimitive(target, locator, content);
            } else {
                throw new OurBadException("Rename iterator cannot handle target items that are not objects");
            }

            pul.addUpdatePrimitive(up);
            this.pul = pul;
        }

        return this.pul;
    }
}
