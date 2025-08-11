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

    public EditTuplePrimitive(Item target, Dataset<Row> contents, ExceptionMetadata metadata) {
        this.target = target;
        this.contents = contents;
    }

    @Override
    public boolean isEditTuple() {
        return true;
    }

    @Override
    public String getCollectionPath() {
        return this.target.getTableLocation();
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
        int targetMutabilityLevel = this.target.getMutabilityLevel();
        long targetRowID = this.target.getTopLevelID();
        double targetRowOrder = this.target.getTopLevelOrder();
        String pathInColumn = this.target.getPathIn();

        this.contents = this.contents
            .withColumn(SparkSessionManager.rowIdColumnName, lit(targetRowID))
            .withColumn(SparkSessionManager.rowOrderColumnName, lit(targetRowOrder));

        SparkSession session = SparkSessionManager.getInstance().getOrCreateSession();
        String safeName = collectionPath.replaceAll("[^a-zA-Z0-9_]", "_");
        String tempViewName = String.format("__edit_tview_%s_%d", safeName, targetRowID);
        this.contents.createOrReplaceTempView(tempViewName);

        String deleteQuery = String.format(
            "DELETE FROM %s WHERE %s = %d",
            collectionPath,
            SparkSessionManager.rowIdColumnName,
            targetRowID
        );
        session.sql(deleteQuery);

        String insertQuery = String.format("INSERT INTO %s SELECT * FROM %s", collectionPath, tempViewName);
        session.sql(insertQuery);

        session.catalog().dropTempView(tempViewName);

    }

}
