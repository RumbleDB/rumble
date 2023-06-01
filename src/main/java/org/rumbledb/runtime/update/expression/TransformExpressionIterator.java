package org.rumbledb.runtime.update.expression;

import org.apache.spark.api.java.JavaRDD;
import org.apache.commons.lang3.SerializationUtils;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.runtime.HybridRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.update.PendingUpdateList;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TransformExpressionIterator extends HybridRuntimeIterator {

    private Map<Name, RuntimeIterator> copyDeclMap;
    private RuntimeIterator modifyIterator;
    private RuntimeIterator returnIterator;

    private int mutabilityLevel;

    public TransformExpressionIterator(
            Map<Name, RuntimeIterator> copyDeclMap,
            RuntimeIterator modifyIterator,
            RuntimeIterator returnIterator,
            ExecutionMode executionMode,
            int mutabilityLevel,
            ExceptionMetadata iteratorMetadata
    ) {
        super(null, executionMode, iteratorMetadata);
        this.children.addAll(copyDeclMap.values());
        this.children.add(modifyIterator);
        this.children.add(returnIterator);

        this.copyDeclMap = copyDeclMap;
        this.modifyIterator = modifyIterator;
        this.returnIterator = returnIterator;
        this.mutabilityLevel = mutabilityLevel;
        this.isUpdating = false;
    }

    @Override
    protected JavaRDD<Item> getRDDAux(DynamicContext context) {
        PendingUpdateList pul = getPendingUpdateList(context);
        pul.applyUpdates(this.getMetadata());
        return returnIterator.getRDD(context);
    }

    @Override
    protected void openLocal() {
        PendingUpdateList pul = getPendingUpdateList(this.currentDynamicContextForLocalExecution);
        pul.applyUpdates(this.getMetadata());
        returnIterator.open(this.currentDynamicContextForLocalExecution);
    }

    @Override
    protected void closeLocal() {
        returnIterator.close();
        for (Name copyVar : copyDeclMap.keySet()) {
            this.currentDynamicContextForLocalExecution.getVariableValues().removeVariable(copyVar);
        }
    }

    @Override
    protected void resetLocal() {
        PendingUpdateList pul = getPendingUpdateList(this.currentDynamicContextForLocalExecution);
        pul.applyUpdates(this.getMetadata());
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
        bindCopyDeclarations(context);
        DynamicContext newCtx = new DynamicContext(context);
        newCtx.setCurrentMutabilityLevel(this.mutabilityLevel);
        return modifyIterator.getPendingUpdateList(newCtx);
    }

    private void bindCopyDeclarations(DynamicContext context) {
        for (Name copyVar : copyDeclMap.keySet()) {
            RuntimeIterator copyIterator = copyDeclMap.get(copyVar);
            List<Item> toCopy = copyIterator.materialize(context);
            List<Item> copy = new ArrayList<>();
            Item temp;
            for (Item item : toCopy) {
                temp = SerializationUtils.clone(item);
                temp.setMutabilityLevel(this.mutabilityLevel);
                copy.add(temp);
            }
            context.getVariableValues().addVariableValue(copyVar, copy);
        }
    }

}
