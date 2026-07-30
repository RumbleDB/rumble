package org.rumbledb.runtime.update.expression;

import org.apache.spark.api.java.JavaRDD;
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

public class InsertSearchIntoCollectionIterator extends UpdatingExpressionIterator {

    @Serial
    private static final long serialVersionUID = 1L;
    private final org.rumbledb.runtime.plan.RuntimePlan<org.rumbledb.api.Item> targetIterator;
    private final org.rumbledb.runtime.plan.RuntimePlan<org.rumbledb.api.Item> contentIterator;
    private final boolean isBefore;

    public InsertSearchIntoCollectionIterator(
            org.rumbledb.runtime.plan.RuntimePlan<org.rumbledb.api.Item> targetIterator,
            org.rumbledb.runtime.plan.RuntimePlan<org.rumbledb.api.Item> contentIterator,
            boolean isBefore,
            RuntimeStaticContext staticContext
    ) {
        super(
            Arrays.asList(targetIterator, contentIterator),
            staticContext.toBuilder().isUpdating(true).build()
        );
        this.targetIterator = targetIterator;
        this.contentIterator = contentIterator;
        this.isBefore = isBefore;


    }

    @Override
    protected JavaRDD<Item> getRDDAux(DynamicContext context) {
        return null;
    }

    @Override
    public PendingUpdateList getPendingUpdateList(DynamicContext context) {
        PendingUpdateList pul = new PendingUpdateList();
        Dataset<Row> contentDF = null;
        try {
            contentDF = this.contentIterator.getDataFrame(context).getDataFrame();
        } catch (CannotInferSchemaOnNonStructuredDataException e) {
            e.setMetadata(getMetadata());
            throw e;
        }
        Item target = null;
        try {
            target = this.targetIterator.materializeExactlyOne(context);
        } catch (MoreThanOneItemException e) {
            throw new InvalidUpdateTargetException(
                    "More than one target item cannot be used for insertion.",
                    this.getRuntimeStaticContext().getMetadata()
            );
        } catch (NoItemException e) {
            throw new InvalidUpdateTargetException(
                    "One target item must be provided for search based insertion. Please check if the target expression provided resolves to a valid target in the collection.",
                    this.getRuntimeStaticContext().getMetadata()
            );
        }

        UpdatePrimitiveFactory factory = UpdatePrimitiveFactory.getInstance();
        UpdatePrimitive up = null;
        if (this.isBefore) {
            up = factory.createInsertBeforeIntoCollectionPrimitive(
                target,
                contentDF,
                this.getRuntimeStaticContext().getMetadata()
            );
        } else {
            up = factory.createInsertAfterIntoCollectionPrimitive(
                target,
                contentDF,
                this.getRuntimeStaticContext().getMetadata()
            );
        }

        pul.addUpdatePrimitive(up);
        return pul;
    }

}
