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
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.runtime.HybridRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.cursor.AbstractLocalCursor;
import org.rumbledb.runtime.cursor.Cursor;
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
    public Cursor<Item> createNativeCursor(DynamicContext context) {
        return new TransformLocalCursor(
                this.copyDeclMap,
                this.modifyIterator,
                this.returnIterator,
                this.mutabilityLevel,
                this.mutable,
                context,
                getMetadata()
        );
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
            List<Item> toCopy = copyIterator.materialize(context);
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

        private final Map<Name, RuntimeIterator> copyDeclarations;
        private final RuntimeIterator modifyPlan;
        private final RuntimeIterator returnPlan;
        private final int mutabilityLevel;
        private final boolean resultMutable;
        private final DynamicContext context;
        private final ExceptionMetadata metadata;
        private Cursor<Item> returnCursor;

        private TransformLocalCursor(
                Map<Name, RuntimeIterator> copyDeclarations,
                RuntimeIterator modifyPlan,
                RuntimeIterator returnPlan,
                int mutabilityLevel,
                boolean resultMutable,
                DynamicContext context,
                ExceptionMetadata metadata
        ) {
            super(metadata);
            this.copyDeclarations = copyDeclarations;
            this.modifyPlan = modifyPlan;
            this.returnPlan = returnPlan;
            this.mutabilityLevel = mutabilityLevel;
            this.resultMutable = resultMutable;
            this.context = context;
            this.metadata = metadata;
        }

        @Override
        protected void openLocal() {
            PendingUpdateList updates = getPendingUpdateList();
            updates.applyUpdates(this.metadata);
            this.returnCursor = this.returnPlan.getCursor(this.context);
        }

        @Override
        protected boolean hasNextLocal() {
            return this.returnCursor.hasNext();
        }

        @Override
        protected Item nextLocal() {
            Item result = this.returnCursor.next();
            if (this.resultMutable) {
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
            for (Name copyVariable : this.copyDeclarations.keySet()) {
                this.context.getVariableValues().removeVariable(copyVariable);
            }
        }

        private PendingUpdateList getPendingUpdateList() {
            bindCopyDeclarations();
            DynamicContext modifyContext = new DynamicContext(this.context);
            modifyContext.setCurrentMutabilityLevel(this.mutabilityLevel);
            return this.modifyPlan.getPendingUpdateList(modifyContext);
        }

        private void bindCopyDeclarations() {
            for (Map.Entry<Name, RuntimeIterator> declaration : this.copyDeclarations.entrySet()) {
                List<Item> copy = new ArrayList<>();
                for (Item item : declaration.getValue().materialize(this.context)) {
                    Item copiedItem = item.copy(true);
                    copiedItem.setMutabilityLevel(this.mutabilityLevel);
                    copiedItem.setCollection(null);
                    copy.add(copiedItem);
                }
                this.context.getVariableValues().addVariableValue(declaration.getKey(), copy);
            }
        }
    }
}
