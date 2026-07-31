package org.rumbledb.runtime.update.expression;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.runtime.update.PendingUpdateList;
import org.rumbledb.runtime.update.primitives.Collection;
import org.rumbledb.runtime.update.primitives.UpdatePrimitive;
import org.rumbledb.runtime.update.primitives.UpdatePrimitiveFactory;
import sparksoniq.spark.SparkSessionManager;

import java.io.Serial;
import java.util.Arrays;
import java.util.List;

public class DeleteSearchFromCollectionIterator extends UpdatingExpressionIterator {

    @Serial
    private static final long serialVersionUID = 1L;
    private final org.rumbledb.runtime.plan.RuntimePlan<org.rumbledb.api.Item> contentIterator;

    public DeleteSearchFromCollectionIterator(
            org.rumbledb.runtime.plan.RuntimePlan<org.rumbledb.api.Item> contentIterator,
            RuntimeStaticContext staticContext
    ) {
        super(Arrays.asList(contentIterator), staticContext.toBuilder().isUpdating(true).build());
        this.contentIterator = contentIterator;
    }

    @Override
    public PendingUpdateList getPendingUpdateList(DynamicContext context) {
        PendingUpdateList pul = new PendingUpdateList();
        UpdatePrimitiveFactory factory = UpdatePrimitiveFactory.getInstance();

        if (this.contentIterator.getRuntimeStaticContext().getExecutionMode().isDataFrame()) {
            // DataFrame case
            Dataset<Row> contentDF = this.contentIterator.getDataFrame(context).getDataFrame();
            List<Row> rows = contentDF.collectAsList();


            if (rows.isEmpty()) {
                // Not throwing an error for empty deletion
                return null;
            }

            Collection collection = new Collection(rows.get(0).getAs(SparkSessionManager.tableLocationColumnName));
            for (Row row : rows) {
                UpdatePrimitive up = factory.createDeleteTupleFromCollectionPrimitive(
                    collection,
                    row.getAs(SparkSessionManager.rowOrderColumnName),
                    this.getRuntimeStaticContext().getMetadata()
                );
                pul.addUpdatePrimitive(up);
            }
        } else if (this.contentIterator.getRuntimeStaticContext().getExecutionMode().isRDD()) {
            // TODO: habndle RDD case
        } else {
            // Local case
            for (Item item : this.contentIterator.materialize(context)) {
                // checks : not 0, not >1 (in try-catch) - is object/array (generated error)
                UpdatePrimitive up = factory.createDeleteTupleFromCollectionPrimitive(
                    item.getCollection(),
                    item.getTopLevelOrder(),
                    this.getRuntimeStaticContext().getMetadata()
                );
                pul.addUpdatePrimitive(up);
            }
        }


        return pul;
    }

}
