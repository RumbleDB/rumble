package org.rumbledb.runtime.update.primitives;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.rumbledb.api.Item;
import org.rumbledb.exceptions.ExceptionMetadata;
import sparksoniq.spark.SparkSessionManager;

import static org.apache.spark.sql.functions.lit;


public class EditTuplePrimitive implements UpdatePrimitive {
    private Item target;
    private Dataset<Row> contents;
    private Row targetRow;
    private final Collection collection;

    public EditTuplePrimitive(Item target, Dataset<Row> contents, ExceptionMetadata metadata) {
        this.target = target;
        this.contents = contents;
        this.collection = target.getCollection();
    }

    @Override
    public boolean isEditTuple() {
        return true;
    }

    @Override
    public String getCollectionPath() {
        return this.collection.getPhysicalName();
    }

    @Override
    public double getRowOrder() {
        return this.target.getTopLevelOrder();
    }

    @Override
    public boolean hasSelector() {
        return false;
    }

    @Override
    public Dataset<Row> getContentDataFrame() {
        return this.contents;
    }

    @Override
    public Item getTarget() {
        return this.target;
    }

    @Override
    public void apply() {
        applyDelta();
    }

    @Override
    public void applyItem() {
        return;
    }

    @Override
    public void applyDelta() {
        String collectionPath = this.getCollectionPath();
        long targetRowID = this.target.getTopLevelID();

        this.contents = this.contents
            .withColumn(SparkSessionManager.rowIdColumnName, lit(targetRowID))
            .withColumn(SparkSessionManager.rowOrderColumnName, lit(this.target.getTopLevelOrder()));

        SparkSession session = SparkSessionManager.getInstance().getOrCreateSession();

        String deleteQuery = String.format(
            "DELETE FROM %s WHERE %s = %d",
            collectionPath,
            SparkSessionManager.rowIdColumnName,
            targetRowID
        );
        session.sql(deleteQuery);

        // Insert back the edited tuple
        this.collection.insertUnordered(this.contents);
    }

}
