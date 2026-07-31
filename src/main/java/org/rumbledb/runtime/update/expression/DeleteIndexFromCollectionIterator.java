package org.rumbledb.runtime.update.expression;

import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.InvalidUpdateTargetException;
import org.rumbledb.exceptions.MoreThanOneItemException;
import org.rumbledb.exceptions.NoItemException;
import org.rumbledb.runtime.update.PendingUpdateList;
import org.rumbledb.runtime.update.primitives.Collection;
import org.rumbledb.runtime.update.primitives.Mode;
import org.rumbledb.runtime.update.primitives.UpdatePrimitive;
import org.rumbledb.runtime.update.primitives.UpdatePrimitiveFactory;
import sparksoniq.spark.SparkSessionManager;

import java.io.Serial;
import java.util.Arrays;
import java.util.List;

public class DeleteIndexFromCollectionIterator extends UpdatingExpressionIterator {

    @Serial
    private static final long serialVersionUID = 1L;
    private final org.rumbledb.runtime.plan.RuntimePlan<org.rumbledb.api.Item> targetIterator;
    private final org.rumbledb.runtime.plan.RuntimePlan<org.rumbledb.api.Item> numDeleteIterator;
    private final boolean isFirst;
    private final Mode mode;

    public DeleteIndexFromCollectionIterator(
            org.rumbledb.runtime.plan.RuntimePlan<org.rumbledb.api.Item> targetIterator,
            boolean isFirst,
            Mode mode,
            RuntimeStaticContext staticContext
    ) {
        super(Arrays.asList(targetIterator), staticContext.toBuilder().isUpdating(true).build());
        this.targetIterator = targetIterator;
        this.numDeleteIterator = null;
        this.isFirst = isFirst;
        this.mode = mode;
    }

    public DeleteIndexFromCollectionIterator(
            org.rumbledb.runtime.plan.RuntimePlan<org.rumbledb.api.Item> targetIterator,
            org.rumbledb.runtime.plan.RuntimePlan<org.rumbledb.api.Item> numDeleteIterator,
            boolean isFirst,
            Mode mode,
            RuntimeStaticContext staticContext
    ) {
        super(
            Arrays.asList(targetIterator, numDeleteIterator),
            staticContext.toBuilder().isUpdating(true).build()
        );
        this.targetIterator = targetIterator;
        this.numDeleteIterator = numDeleteIterator;
        this.isFirst = isFirst;
        this.mode = mode;
    }

    @Override
    public PendingUpdateList getPendingUpdateList(DynamicContext context) {
        PendingUpdateList pul = new PendingUpdateList();

        Item targetItem = null;
        try {
            targetItem = this.targetIterator.materializeExactlyOne(context);
        } catch (MoreThanOneItemException e) {
            throw new InvalidUpdateTargetException(
                    "The collection name must be a string, but more than one item was provided.",
                    this.getRuntimeStaticContext().getMetadata()
            );
        } catch (NoItemException e) {
            throw new InvalidUpdateTargetException(
                    "The collection name must be a string, but no item was provided.",
                    this.getRuntimeStaticContext().getMetadata()
            );
        }

        if (!targetItem.isString()) {
            throw new InvalidUpdateTargetException(
                    "Expecting collection name as a String, but it was: "
                        + targetItem.getDynamicType().getIdentifierString(),
                    this.getRuntimeStaticContext().getMetadata()
            );
        }

        int numDeleteInt = 1;
        if (this.numDeleteIterator != null) {
            Item numDeleteItem = null;
            try {
                numDeleteItem = this.numDeleteIterator.materializeExactlyOne(context);
            } catch (MoreThanOneItemException e) {
                throw new InvalidUpdateTargetException(
                        "The number to be deleted must be an integer, but more than one item was provided.",
                        this.getRuntimeStaticContext().getMetadata()
                );
            } catch (NoItemException e) {
                throw new InvalidUpdateTargetException(
                        "The number to be deleted must be an integer, but no item was provided.",
                        this.getRuntimeStaticContext().getMetadata()
                );
            }

            if (!numDeleteItem.isInt()) {
                throw new InvalidUpdateTargetException(
                        "Expecting number to be deleted name as an integer, but it was: "
                            + targetItem.getDynamicType().getIdentifierString(),
                        this.getRuntimeStaticContext().getMetadata()
                );
            }

            numDeleteInt = numDeleteItem.getIntValue();

        }

        Collection collection = new Collection(this.mode, targetItem.getStringValue());

        SparkSession session = SparkSessionManager.getInstance().getOrCreateSession();
        String selectQuery = String.format(
            "SELECT %s FROM %s ORDER BY %s %s LIMIT %d",
            SparkSessionManager.rowOrderColumnName,
            collection.getPhysicalName(),
            SparkSessionManager.rowOrderColumnName,
            this.isFirst ? "ASC" : "DESC",
            numDeleteInt
        );
        List<Row> rows = session.sql(selectQuery).collectAsList();

        UpdatePrimitiveFactory factory = UpdatePrimitiveFactory.getInstance();
        for (Row row : rows) {
            double rowOrder = row.getAs(SparkSessionManager.rowOrderColumnName);
            UpdatePrimitive up = factory.createDeleteTupleFromCollectionPrimitive(
                collection,
                rowOrder,
                this.getRuntimeStaticContext().getMetadata()
            );
            pul.addUpdatePrimitive(up);
        }

        return pul;
    }

}
