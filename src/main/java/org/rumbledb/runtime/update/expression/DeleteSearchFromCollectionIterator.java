package org.rumbledb.runtime.update.expression;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.runtime.HybridRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.update.PendingUpdateList;
import org.rumbledb.runtime.update.primitives.Collection;
import org.rumbledb.runtime.update.primitives.UpdatePrimitive;
import org.rumbledb.runtime.update.primitives.UpdatePrimitiveFactory;
import sparksoniq.spark.SparkSessionManager;

import java.util.Arrays;
import java.util.List;

public class DeleteSearchFromCollectionIterator extends HybridRuntimeIterator {

    private static final long serialVersionUID = 1L;
    private final RuntimeIterator contentIterator;

    public DeleteSearchFromCollectionIterator(
            RuntimeIterator contentIterator,
            RuntimeStaticContext staticContext
    ) {
        super(Arrays.asList(contentIterator), staticContext);
        this.contentIterator = contentIterator;
        this.isUpdating = true;
    }

    public boolean hasPositionIterator() {
        return false;
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
        // TODO: Ascertain this
        return false;
    }

    @Override
    protected Item nextLocal() {
        // TODO: Check for this
        return null;
    }

    @Override
    public PendingUpdateList getPendingUpdateList(DynamicContext context) {
        PendingUpdateList pul = new PendingUpdateList();
        UpdatePrimitiveFactory factory = UpdatePrimitiveFactory.getInstance();

        if (this.contentIterator.isDataFrame()) {
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
                    this.getMetadata()
                );
                pul.addUpdatePrimitive(up);
            }
        } else if (this.contentIterator.isRDD()) {
            // TODO: habndle RDD case
        } else {
            // Local case
            // this.contentIterator.materializeExactlyOneItem in a try-catch, throw the new error generated
            this.contentIterator.open(context);
            while (this.contentIterator.hasNext()) {
                // checks : not 0, not >1 (in try-catch) - is object/array (generated error)
                Item item = this.contentIterator.next();
                UpdatePrimitive up = factory.createDeleteTupleFromCollectionPrimitive(
                    item.getCollection(),
                    item.getTopLevelOrder(),
                    this.getMetadata()
                );
                pul.addUpdatePrimitive(up);
            }
            this.contentIterator.close();
        }


        return pul;
    }

}
