package org.rumbledb.runtime.update.expression;

import java.io.Serial;
import java.util.Arrays;
import java.util.Collections;

import org.apache.commons.lang3.SerializationUtils;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.InvalidUpdateTargetException;
import org.rumbledb.exceptions.ModifiesImmutableValueException;
import org.rumbledb.exceptions.MoreThanOneItemException;
import org.rumbledb.exceptions.NoItemException;
import org.rumbledb.exceptions.TransformModifiesNonCopiedValueException;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.update.PendingUpdateList;
import org.rumbledb.runtime.update.primitives.UpdatePrimitive;
import org.rumbledb.runtime.update.primitives.UpdatePrimitiveFactory;


public class AppendExpressionIterator extends UpdatingExpressionIterator {

    @Serial
    private static final long serialVersionUID = 1L;
    private final org.rumbledb.runtime.plan.RuntimePlan<org.rumbledb.api.Item> arrayIterator;
    private final org.rumbledb.runtime.plan.RuntimePlan<org.rumbledb.api.Item> toAppendIterator;

    public AppendExpressionIterator(
            org.rumbledb.runtime.plan.RuntimePlan<org.rumbledb.api.Item> arrayIterator,
            org.rumbledb.runtime.plan.RuntimePlan<org.rumbledb.api.Item> toAppendIterator,
            RuntimeStaticContext staticContext
    ) {
        super(
            Arrays.asList(arrayIterator, toAppendIterator),
            staticContext.toBuilder().isUpdating(true).build()
        );

        this.arrayIterator = arrayIterator;
        this.toAppendIterator = toAppendIterator;
    }

    @Override
    public PendingUpdateList getPendingUpdateList(DynamicContext context) {
        PendingUpdateList pul = new PendingUpdateList();
        Item target;
        Item content;

        try {
            target = this.arrayIterator.materializeExactlyOne(context);
            content = SerializationUtils.clone(this.toAppendIterator.materializeExactlyOne(context));
        } catch (NoItemException | MoreThanOneItemException e) {
            throw new RuntimeException(e);
        }

        UpdatePrimitiveFactory factory = UpdatePrimitiveFactory.getInstance();
        UpdatePrimitive up;
        if (target.isArray()) {
            Item locator = ItemFactory.getInstance().createIntItem(target.getSize() + 1);
            if (context.getCurrentMutabilityLevel() == 0 && target.getMutabilityLevel() == -1) {
                throw new ModifiesImmutableValueException(
                        "Attempt to modify immutable target",
                        this.getRuntimeStaticContext().getMetadata()
                );
            }
            if (target.getMutabilityLevel() != context.getCurrentMutabilityLevel()) {
                throw new TransformModifiesNonCopiedValueException(
                        "Attempt to modify currently immutable target",
                        this.getRuntimeStaticContext().getMetadata()
                );
            }
            up = factory.createInsertIntoArrayPrimitive(
                target,
                locator,
                Collections.singletonList(content),
                this.getRuntimeStaticContext().getMetadata()
            );
        } else {
            throw new InvalidUpdateTargetException(
                    "Append expression target must be a single array",
                    this.getRuntimeStaticContext().getMetadata()
            );
        }

        pul.addUpdatePrimitive(up);
        return pul;
    }
}
