package org.rumbledb.runtime.update.expression;

import org.rumbledb.runtime.plan.ItemRuntimePlan;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.CannotInferSchemaOnNonStructuredDataException;
import org.rumbledb.exceptions.InvalidUpdateTargetException;
import org.rumbledb.exceptions.MoreThanOneItemException;
import org.rumbledb.exceptions.NoItemException;
import org.rumbledb.runtime.update.PendingUpdateList;
import org.rumbledb.runtime.update.primitives.UpdatePrimitive;
import org.rumbledb.runtime.update.primitives.UpdatePrimitiveFactory;

import java.io.Serial;
import java.util.Arrays;

public class EditCollectionIterator extends UpdatingExpressionIterator {

    @Serial
    private static final long serialVersionUID = 1L;
    private final ItemRuntimePlan targetIterator;
    private final ItemRuntimePlan contentIterator;

    public EditCollectionIterator(
            ItemRuntimePlan targetIterator,
            ItemRuntimePlan contentIterator,
            RuntimeStaticContext staticContext
    ) {
        super(
            Arrays.asList(targetIterator, contentIterator),
            staticContext.toBuilder().isUpdating(true).build()
        );
        this.targetIterator = targetIterator;
        this.contentIterator = contentIterator;

    }

    @Override
    public PendingUpdateList getPendingUpdateList(DynamicContext context) {
        Item targetItem = null;
        try {
            targetItem = this.targetIterator.materializeExactlyOne(context);
        } catch (MoreThanOneItemException e) {
            throw new InvalidUpdateTargetException(
                    "More than one target item cannot be Edited.",
                    this.getRuntimeStaticContext().getMetadata()
            );
        } catch (NoItemException e) {
            throw new InvalidUpdateTargetException(
                    "One target item must be provided for Edit.",
                    this.getRuntimeStaticContext().getMetadata()
            );
        }

        Dataset<Row> contentDF = null;
        try {
            contentDF = this.contentIterator.getDataFrame(context).getDataFrame();
        } catch (CannotInferSchemaOnNonStructuredDataException e) {
            e.setMetadata(getMetadata());
            throw e;
        }

        long contentCount = contentDF.count();

        if (contentCount != 1) {
            throw new InvalidUpdateTargetException(
                    "Exactly one content must be specified for edit, but " + contentCount + " found",
                    this.getRuntimeStaticContext().getMetadata()
            );
        }

        PendingUpdateList pul = new PendingUpdateList();
        UpdatePrimitiveFactory factory = UpdatePrimitiveFactory.getInstance();
        UpdatePrimitive up = factory.createEditTuplePrimitive(
            targetItem,
            contentDF,
            this.getRuntimeStaticContext().getMetadata()
        );
        pul.addUpdatePrimitive(up);
        return pul;
    }

}
