package org.rumbledb.runtime.update.expression;

import org.apache.commons.lang3.SerializationUtils;
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
import java.util.Collections;

public class InsertExpressionIterator extends HybridRuntimeIterator {

    private RuntimeIterator mainIterator;
    private RuntimeIterator toInsertIterator;
    private RuntimeIterator positionIterator;

    public InsertExpressionIterator(
            RuntimeIterator mainIterator,
            RuntimeIterator toInsertIterator,
            RuntimeIterator positionIterator,
            ExecutionMode executionMode,
            ExceptionMetadata iteratorMetadata
    ) {
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
        return this.positionIterator != null;
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
            content = SerializationUtils.clone(this.toInsertIterator.materializeExactlyOneItem(context));
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
            if (!content.isObject()) {
                throw new ObjectInsertContentIsNotObjectSeqException(
                        "Insert expression content is not an object",
                        this.getMetadata()
                );
            }
            if (main.getMutabilityLevel() == -1) {
                throw new ModifiesImmutableValueException("Attempt to modify immutable target", this.getMetadata());
            }
            if (main.getMutabilityLevel() != context.getCurrentMutabilityLevel()) {
                throw new TransformModifiesNonCopiedValueException(
                        "Attempt to modify currently immutable target",
                        this.getMetadata()
                );
            }
            up = factory.createInsertIntoObjectPrimitive(main, content);
        } else if (main.isArray()) {
            if (locator == null) {
                throw new CannotCastUpdateSelectorException("Insert expression selector is null", this.getMetadata());
            }
            if (!locator.isInt()) {
                throw new CannotCastUpdateSelectorException(
                        "Insert expression selector cannot be cast to Int type",
                        this.getMetadata()
                );
            }
            if (main.getMutabilityLevel() == -1) {
                throw new ModifiesImmutableValueException("Attempt to modify immutable target", this.getMetadata());
            }
            if (main.getMutabilityLevel() != context.getCurrentMutabilityLevel()) {
                throw new TransformModifiesNonCopiedValueException(
                        "Attempt to modify currently immutable target",
                        this.getMetadata()
                );
            }
            up = factory.createInsertIntoArrayPrimitive(main, locator, Collections.singletonList(content));
        } else {
            throw new InvalidUpdateTargetException(
                    "Insert expression target must be a single array or object",
                    this.getMetadata()
            );
        }

        pul.addUpdatePrimitive(up);
        return pul;
    }
}
