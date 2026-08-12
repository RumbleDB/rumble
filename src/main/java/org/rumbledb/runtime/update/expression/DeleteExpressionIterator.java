package org.rumbledb.runtime.update.expression;

import org.rumbledb.runtime.plan.ItemRuntimePlan;

import java.io.Serial;
import java.util.Arrays;
import java.util.Collections;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.CannotCastUpdateSelectorException;
import org.rumbledb.exceptions.InvalidUpdateTargetException;
import org.rumbledb.exceptions.ModifiesImmutableValueException;
import org.rumbledb.exceptions.MoreThanOneItemException;
import org.rumbledb.exceptions.NoItemException;
import org.rumbledb.exceptions.TransformModifiesNonCopiedValueException;
import org.rumbledb.runtime.update.PendingUpdateList;
import org.rumbledb.runtime.update.primitives.UpdatePrimitive;
import org.rumbledb.runtime.update.primitives.UpdatePrimitiveFactory;

public class DeleteExpressionIterator extends UpdatingExpressionIterator {

    @Serial
    private static final long serialVersionUID = 1L;
    private final ItemRuntimePlan mainIterator;
    private final ItemRuntimePlan lookupIterator;

    public DeleteExpressionIterator(
            ItemRuntimePlan mainIterator,
            ItemRuntimePlan lookupIterator,
            RuntimeStaticContext staticContext
    ) {
        super(
            Arrays.asList(mainIterator, lookupIterator),
            staticContext.toBuilder().isUpdating(true).build()
        );
        this.mainIterator = mainIterator;
        this.lookupIterator = lookupIterator;
    }

    @Override
    public PendingUpdateList getPendingUpdateList(DynamicContext context) {
        PendingUpdateList pul = new PendingUpdateList();
        Item main;
        Item lookup;

        try {
            main = this.mainIterator.materializeExactlyOne(context);
            lookup = this.lookupIterator.materializeExactlyOne(context);
        } catch (NoItemException | MoreThanOneItemException e) {
            throw new RuntimeException(e);
        }

        UpdatePrimitiveFactory factory = UpdatePrimitiveFactory.getInstance();
        UpdatePrimitive up;
        if (main.isObject()) {
            if (!lookup.isString()) {
                throw new CannotCastUpdateSelectorException(
                        "Delete expression selection cannot be cast to String type",
                        this.getRuntimeStaticContext().getMetadata()
                );
            }
            if (context.getCurrentMutabilityLevel() == 0 && main.getMutabilityLevel() == -1) {
                throw new ModifiesImmutableValueException(
                        "Attempt to modify immutable target. Target mutability level: "
                            + main.getMutabilityLevel()
                            + ". Context mutability level: "
                            + context.getCurrentMutabilityLevel(),
                        this.getRuntimeStaticContext().getMetadata()
                );
            }
            if (main.getMutabilityLevel() != context.getCurrentMutabilityLevel()) {
                throw new TransformModifiesNonCopiedValueException(
                        "Attempt to modify currently immutable target",
                        this.getRuntimeStaticContext().getMetadata()
                );
            }
            up = factory.createDeleteFromObjectPrimitive(
                main,
                Collections.singletonList(lookup),
                this.getRuntimeStaticContext().getMetadata()
            );
        } else if (main.isArray()) {
            if (!lookup.isInt()) {
                throw new CannotCastUpdateSelectorException(
                        "Delete expression selection cannot be cast to Int type",
                        this.getRuntimeStaticContext().getMetadata()
                );
            }
            if (context.getCurrentMutabilityLevel() == 0 && main.getMutabilityLevel() == -1) {
                throw new ModifiesImmutableValueException(
                        "Attempt to modify immutable target. Target mutability level: "
                            + main.getMutabilityLevel()
                            + ". Context mutability level: "
                            + context.getCurrentMutabilityLevel(),
                        this.getRuntimeStaticContext().getMetadata()
                );
            }
            if (main.getMutabilityLevel() != context.getCurrentMutabilityLevel()) {
                throw new TransformModifiesNonCopiedValueException(
                        "Attempt to modify currently immutable target",
                        this.getRuntimeStaticContext().getMetadata()
                );
            }
            up = factory.createDeleteFromArrayPrimitive(main, lookup, this.getRuntimeStaticContext().getMetadata());
        } else {
            throw new InvalidUpdateTargetException(
                    "Delete expression target must be a single array or object",
                    this.getRuntimeStaticContext().getMetadata()
            );
        }

        pul.addUpdatePrimitive(up);
        return pul;
    }
}
