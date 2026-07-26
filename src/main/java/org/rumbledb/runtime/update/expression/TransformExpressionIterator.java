package org.rumbledb.runtime.update.expression;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.apache.spark.api.java.JavaRDD;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.runtime.HybridRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.cursor.AbstractLocalCursor;
import org.rumbledb.runtime.cursor.LocalCursor;
import org.rumbledb.runtime.cursor.LocalCursorUtils;
import org.rumbledb.runtime.update.PendingUpdateList;

public class TransformExpressionIterator extends HybridRuntimeIterator {

    @Serial
    private static final long serialVersionUID = 1L;
    private final Map<Name, RuntimeIterator> copyDeclMap;
    private final RuntimeIterator modifyIterator;
    private final RuntimeIterator returnIterator;
    private final boolean mutable;
    private final int mutabilityLevel;

    public TransformExpressionIterator(
            Map<Name, RuntimeIterator> copyDeclMap,
            RuntimeIterator modifyIterator,
            RuntimeIterator returnIterator,
            RuntimeStaticContext staticContext,
            int mutabilityLevel,
            boolean resultMutable
    ) {
        super(
            Stream.concat(
                copyDeclMap.values().stream(),
                Stream.of(modifyIterator, returnIterator)
            ).toList(),
            staticContext.toBuilder().isUpdating(false).build()
        );

        this.copyDeclMap = copyDeclMap;
        this.modifyIterator = modifyIterator;
        this.returnIterator = returnIterator;
        this.mutabilityLevel = mutabilityLevel;
        this.mutable = resultMutable;
    }

    @Override
    public LocalCursor<Item> createLocalCursor(DynamicContext context) {
        return new TransformLocalCursor(this, context);
    }

    @Override
    protected JavaRDD<Item> getRDDAux(DynamicContext context) {
        PendingUpdateList pul = getPendingUpdateList(context);
        pul.applyUpdates(this.getMetadata());
        return this.returnIterator.getRDD(context);
    }

    @Override
    public PendingUpdateList getPendingUpdateList(DynamicContext context) {
        bindCopyDeclarations(context);
        DynamicContext newCtx = new DynamicContext(context);
        newCtx.setCurrentMutabilityLevel(this.mutabilityLevel);
        return this.modifyIterator.getPendingUpdateList(newCtx);
    }

    private void bindCopyDeclarations(DynamicContext context) {
        for (Name copyVar : this.copyDeclMap.keySet()) {
            RuntimeIterator copyIterator = this.copyDeclMap.get(copyVar);
            List<Item> toCopy = LocalCursorUtils.materialize(copyIterator, context);
            List<Item> copy = new ArrayList<>();
            Item temp;
            for (Item item : toCopy) {
                temp = item.copy(true);
                temp.setMutabilityLevel(this.mutabilityLevel);
                // Ensure transform updates apply to the copied item, not the backing collection.
                temp.setCollection(null);
                copy.add(temp);
            }
            context.getVariableValues().addVariableValue(copyVar, copy);
        }
    }

    private static final class TransformLocalCursor extends AbstractLocalCursor<Item> {

        private final TransformExpressionIterator plan;
        private final DynamicContext context;
        private LocalCursor<Item> returnCursor;

        private TransformLocalCursor(TransformExpressionIterator plan, DynamicContext context) {
            super(plan.getMetadata());
            this.plan = plan;
            this.context = context;
        }

        @Override
        protected void openLocal() {
            PendingUpdateList updates = this.plan.getPendingUpdateList(this.context);
            updates.applyUpdates(this.plan.getMetadata());
            this.returnCursor = this.plan.returnIterator.createLocalCursor(this.context);
            this.returnCursor.open();
        }

        @Override
        protected boolean hasNextLocal() {
            return this.returnCursor.hasNext();
        }

        @Override
        protected Item nextLocal() {
            Item result = this.returnCursor.next();
            if (this.plan.mutable) {
                result.setMutabilityLevel(this.context.getCurrentMutabilityLevel());
            }
            return result;
        }

        @Override
        protected void closeLocal() {
            if (this.returnCursor != null) {
                this.returnCursor.close();
                this.returnCursor = null;
            }
            for (Name copyVariable : this.plan.copyDeclMap.keySet()) {
                this.context.getVariableValues().removeVariable(copyVariable);
            }
        }
    }
}
