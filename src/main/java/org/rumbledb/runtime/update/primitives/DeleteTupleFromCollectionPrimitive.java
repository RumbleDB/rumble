package org.rumbledb.runtime.update.primitives;

import org.rumbledb.exceptions.ExceptionMetadata;
import sparksoniq.spark.SparkSessionManager;
import org.apache.spark.sql.SparkSession;


public class DeleteTupleFromCollectionPrimitive implements UpdatePrimitive {
    private String collectionPath;
    private double rowOrder;
    private ExceptionMetadata metadata;

    public DeleteTupleFromCollectionPrimitive(String collectionPath, double rowOrder, ExceptionMetadata metadata) {
        this.collectionPath = collectionPath;
        this.rowOrder = rowOrder;
    }

    @Override
    public boolean isDeleteTuple() {
        return true;
    }

    @Override
    public String getCollectionPath() {
        return this.collectionPath;
    }

    @Override
    public double getRowOrder() {
        return this.rowOrder;
    }

    @Override
    public boolean hasSelector() {
        return false;
    }

    @Override
    public void apply() {
        applyDelta();
    }

    @Override
    public void applyItem() {
        // The name of the collection is a string Item, therefore not required
        // throw new Exception("Apply Item not implemented for Create Collection");
        return;
    }

    @Override
    public void applyDelta() {
        SparkSession session = SparkSessionManager.getInstance().getOrCreateSession();

        String deleteQuery = String.format(
            "DELETE FROM %s WHERE %s = %s",
            this.collectionPath,
            SparkSessionManager.rowOrderColumnName,
            String.valueOf(this.rowOrder)
        );
        session.sql(deleteQuery);
    }

}
