package org.rumbledb.runtime.update.expression;

import java.io.Serial;
import java.util.Arrays;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.CannotCastUpdateSelectorException;
import org.rumbledb.exceptions.InvalidUpdateTargetException;
import org.rumbledb.exceptions.ModifiesImmutableValueException;
import org.rumbledb.exceptions.MoreThanOneItemException;
import org.rumbledb.exceptions.NoItemException;
import org.rumbledb.exceptions.TransformModifiesNonCopiedValueException;
import org.rumbledb.exceptions.UpdateTargetIsEmptySeqException;
import org.rumbledb.runtime.plan.ItemRuntimePlan;
import org.rumbledb.runtime.update.PendingUpdateList;
import org.rumbledb.runtime.update.primitives.UpdatePrimitive;
import org.rumbledb.runtime.update.primitives.UpdatePrimitiveFactory;

public class RenameExpressionIterator extends UpdatingExpressionIterator {

    @Serial
    private static final long serialVersionUID = 1L;

    private final ItemRuntimePlan mainIterator;
    private final ItemRuntimePlan locatorIterator;
    private final ItemRuntimePlan nameIterator;

    public RenameExpressionIterator(
            ItemRuntimePlan mainIterator,
            ItemRuntimePlan locatorIterator,
            ItemRuntimePlan nameIterator,
            RuntimeStaticContext staticContext) {
        super(
                Arrays.asList(mainIterator, locatorIterator, nameIterator),
                staticContext.toBuilder().isUpdating(true).build());

        this.mainIterator = mainIterator;
        this.locatorIterator = locatorIterator;
        this.nameIterator = nameIterator;
    }

    @Override
    public PendingUpdateList getPendingUpdateList(DynamicContext context) {
        PendingUpdateList pul = new PendingUpdateList();
        Item target;
        Item locator;
        Item content;

        try {
            target = this.mainIterator.materializeExactlyOne(context);
            locator = this.locatorIterator.materializeExactlyOne(context);
            content = this.nameIterator.materializeExactlyOne(context);
        } catch (NoItemException e) {
            throw new UpdateTargetIsEmptySeqException(
                    "Target of rename expression is empty",
                    this.getRuntimeStaticContext().getMetadata());
        } catch (MoreThanOneItemException e) {
            throw new RuntimeException(e);
        }

        UpdatePrimitiveFactory factory = UpdatePrimitiveFactory.getInstance();
        UpdatePrimitive up;
        if (target.isObject()) {
            if (!locator.isString()) {
                throw new CannotCastUpdateSelectorException(
                        "Rename expression selection cannot be cast to String type",
                        this.getRuntimeStaticContext().getMetadata());
            }
            if (context.getCurrentMutabilityLevel() == 0 && target.getMutabilityLevel() == -1) {
                throw new ModifiesImmutableValueException(
                        "Attempt to modify immutable target",
                        this.getRuntimeStaticContext().getMetadata());
            }
            if (target.getMutabilityLevel() != context.getCurrentMutabilityLevel()) {
                throw new TransformModifiesNonCopiedValueException(
                        "Attempt to modify currently immutable target",
                        this.getRuntimeStaticContext().getMetadata());
            }
            up = factory.createRenameInObjectPrimitive(
                    target, locator, content, this.getRuntimeStaticContext().getMetadata());
        } else {
            throw new InvalidUpdateTargetException(
                    "Rename expression target must be a single object",
                    this.getRuntimeStaticContext().getMetadata());
        }

        pul.addUpdatePrimitive(up);
        return pul;
    }
}
