package org.rumbledb.runtime.update.expression;

import org.apache.spark.api.java.JavaRDD;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.runtime.HybridRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.update.PendingUpdateList;

import java.util.List;

public class TransformExpressionIterator extends HybridRuntimeIterator {

    private List<RuntimeIterator> copyDeclIterators;
    private RuntimeIterator modifyIterator;
    private RuntimeIterator returnIterator;

    public TransformExpressionIterator(List<RuntimeIterator> copyDeclIterators, RuntimeIterator modifyIterator, RuntimeIterator returnIterator, ExecutionMode executionMode, ExceptionMetadata iteratorMetadata) {
        super(null, executionMode, iteratorMetadata);
        this.children.addAll(copyDeclIterators);
        this.children.add(modifyIterator);
        this.children.add(returnIterator);

        this.copyDeclIterators = copyDeclIterators;
        this.modifyIterator = modifyIterator;
        this.returnIterator = returnIterator;
        this.isUpdating = true;
    }

    @Override
    protected JavaRDD<Item> getRDDAux(DynamicContext context) {
        return returnIterator.getRDD(context);
    }

    @Override
    protected void openLocal() {
        returnIterator.open(this.currentDynamicContextForLocalExecution);
    }

    @Override
    protected void closeLocal() {
        returnIterator.close();
    }

    @Override
    protected void resetLocal() {
        returnIterator.reset(this.currentDynamicContextForLocalExecution);
    }

    @Override
    protected boolean hasNextLocal() {
        return returnIterator.hasNext();
    }

    @Override
    protected Item nextLocal() {
        return returnIterator.next();
    }

    @Override
    public PendingUpdateList getPendingUpdateList(DynamicContext context) {
        return modifyIterator.getPendingUpdateList(context);
    }

}
