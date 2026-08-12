package org.rumbledb.runtime.update.expression;

import java.io.Serial;
import java.util.Arrays;
import java.util.Collections;

import org.apache.commons.lang3.SerializationUtils;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.CannotCastUpdateSelectorException;
import org.rumbledb.exceptions.InvalidUpdateTargetException;
import org.rumbledb.exceptions.ModifiesImmutableValueException;
import org.rumbledb.exceptions.MoreThanOneItemException;
import org.rumbledb.exceptions.NoItemException;
import org.rumbledb.exceptions.ObjectInsertContentIsNotObjectSeqException;
import org.rumbledb.exceptions.TransformModifiesNonCopiedValueException;
import org.rumbledb.exceptions.UpdateTargetIsEmptySeqException;
import org.rumbledb.runtime.plan.ItemRuntimePlan;
import org.rumbledb.runtime.update.PendingUpdateList;
import org.rumbledb.runtime.update.primitives.UpdatePrimitive;
import org.rumbledb.runtime.update.primitives.UpdatePrimitiveFactory;

public class InsertExpressionIterator extends UpdatingExpressionIterator {

    @Serial
    private static final long serialVersionUID = 1L;

    private final ItemRuntimePlan mainIterator;
    private final ItemRuntimePlan toInsertIterator;
    private final ItemRuntimePlan positionIterator;

    public InsertExpressionIterator(
            ItemRuntimePlan mainIterator,
            ItemRuntimePlan toInsertIterator,
            ItemRuntimePlan positionIterator,
            RuntimeStaticContext staticContext) {
        super(
                positionIterator == null
                        ? Arrays.asList(mainIterator, toInsertIterator)
                        : Arrays.asList(mainIterator, toInsertIterator, positionIterator),
                staticContext.toBuilder().isUpdating(true).build());

        this.mainIterator = mainIterator;
        this.toInsertIterator = toInsertIterator;
        this.positionIterator = positionIterator;
    }

    public boolean hasPositionIterator() {
        return this.positionIterator != null;
    }

    @Override
    public PendingUpdateList getPendingUpdateList(DynamicContext context) {
        PendingUpdateList pul = new PendingUpdateList();
        Item main;
        Item content;
        Item locator = null;

        try {
            main = this.mainIterator.materializeExactlyOne(context);
            content = SerializationUtils.clone(this.toInsertIterator.materializeExactlyOne(context));
            if (this.hasPositionIterator()) {
                locator = this.positionIterator.materializeExactlyOne(context);
            }
        } catch (NoItemException e) {
            throw new UpdateTargetIsEmptySeqException(
                    "Target of insert expression is empty",
                    this.getRuntimeStaticContext().getMetadata());
        } catch (MoreThanOneItemException e) {
            throw new RuntimeException(e);
        }

        UpdatePrimitiveFactory factory = UpdatePrimitiveFactory.getInstance();
        UpdatePrimitive up;
        if (main.isObject()) {
            if (!content.isObject()) {
                throw new ObjectInsertContentIsNotObjectSeqException(
                        "Insert expression content is not an object",
                        this.getRuntimeStaticContext().getMetadata());
            }
            if (context.getCurrentMutabilityLevel() == 0 && main.getMutabilityLevel() == -1) {
                throw new ModifiesImmutableValueException(
                        "Attempt to modify immutable target",
                        this.getRuntimeStaticContext().getMetadata());
            }
            if (main.getMutabilityLevel() != context.getCurrentMutabilityLevel()) {
                throw new TransformModifiesNonCopiedValueException(
                        "Attempt to modify currently immutable target",
                        this.getRuntimeStaticContext().getMetadata());
            }
            up = factory.createInsertIntoObjectPrimitive(
                    main, content, this.getRuntimeStaticContext().getMetadata());
        } else if (main.isArray()) {
            if (locator == null) {
                throw new CannotCastUpdateSelectorException(
                        "Insert expression selector is null",
                        this.getRuntimeStaticContext().getMetadata());
            }
            if (!locator.isInt()) {
                throw new CannotCastUpdateSelectorException(
                        "Insert expression selector cannot be cast to Int type",
                        this.getRuntimeStaticContext().getMetadata());
            }
            if (context.getCurrentMutabilityLevel() == 0 && main.getMutabilityLevel() == -1) {
                throw new ModifiesImmutableValueException(
                        "Attempt to modify immutable target",
                        this.getRuntimeStaticContext().getMetadata());
            }
            if (main.getMutabilityLevel() != context.getCurrentMutabilityLevel()) {
                throw new TransformModifiesNonCopiedValueException(
                        "Attempt to modify currently immutable target",
                        this.getRuntimeStaticContext().getMetadata());
            }
            up = factory.createInsertIntoArrayPrimitive(
                    main,
                    locator,
                    Collections.singletonList(content),
                    this.getRuntimeStaticContext().getMetadata());
        } else {
            throw new InvalidUpdateTargetException(
                    "Insert expression target must be a single array or object",
                    this.getRuntimeStaticContext().getMetadata());
        }

        pul.addUpdatePrimitive(up);
        return pul;
    }
}
