package org.rumbledb.runtime.update.primitives;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.rumbledb.exceptions.ExceptionMetadata;
import sparksoniq.spark.SparkSessionManager;

import static org.apache.spark.sql.functions.lit;


public class EditTuplePrimitive implements UpdatePrimitive {
    private Dataset<Row> contents;
    private Dataset<Row> target;
    private Row targetRow;

    public EditTuplePrimitive(Dataset<Row> target, Dataset<Row> contents, ExceptionMetadata metadata) {
        this.target = target;
        this.contents = contents;
        this.targetRow = target.first();
    }

    @Override
    public boolean isEditTuple() {
        return true;
    }

    @Override
    public String getCollectionPath() {
        return this.targetRow.getAs(SparkSessionManager.tableLocationColumnName);
    }

    @Override
    public double getRowOrder() {
        return this.targetRow.getAs(SparkSessionManager.rowOrderColumnName);
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
    public Dataset<Row> getTargetDataFrame() {
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
        int targetMutabilityLevel = this.targetRow.getAs(SparkSessionManager.mutabilityLevelColumnName);
        long targetRowID = this.targetRow.getAs(SparkSessionManager.rowIdColumnName);
        double targetRowOrder = this.targetRow.getAs(SparkSessionManager.rowOrderColumnName);
        String pathInColumn = this.targetRow.getAs(SparkSessionManager.pathInColumnName);

        this.contents = this.contents
            .withColumn(SparkSessionManager.rowIdColumnName, lit(targetRowID))
            .withColumn(SparkSessionManager.rowOrderColumnName, lit(targetRowOrder))
            .withColumn(SparkSessionManager.mutabilityLevelColumnName, lit(targetMutabilityLevel))
            .withColumn(SparkSessionManager.pathInColumnName, lit(pathInColumn))
            .withColumn(SparkSessionManager.tableLocationColumnName, lit(collectionPath));

        System.out.println("##Contents\n" + this.contents.schema().treeString());
        System.out.println("##Target\n" + this.target.schema().treeString());

        // System.out.println("##"+this.contents.schema().equals(this.target.schema()));
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
