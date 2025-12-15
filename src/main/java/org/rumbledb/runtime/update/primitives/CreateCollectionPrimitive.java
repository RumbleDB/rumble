package org.rumbledb.runtime.update.primitives;

import org.rumbledb.exceptions.ExceptionMetadata;
import sparksoniq.spark.SparkSessionManager;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import static org.apache.spark.sql.functions.monotonically_increasing_id;


public class CreateCollectionPrimitive implements UpdatePrimitive {
    private Dataset<Row> contents;
    private Collection collection;
    private boolean isTable;

    public CreateCollectionPrimitive(
            Collection collection,
            Dataset<Row> contents,
            boolean isTable,
            ExceptionMetadata metadata
    ) {
        // The target should be the name of the collection if isTable is true,
        // or an absolute path to a delta file if isTable is false.
        this.collection = collection;
        this.contents = contents;
        this.isTable = isTable;
    }

    @Override
    public boolean isCreateCollection() {
        return true;
    }

    @Override
    public String getCollectionPath() {
        return this.collection.getPhysicalName();
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
        this.contents = this.contents.withColumn(
            SparkSessionManager.rowIdColumnName,
            monotonically_increasing_id()
        );

        this.contents = this.contents.withColumn(
            SparkSessionManager.rowOrderColumnName,
            monotonically_increasing_id().cast("double")
        );

        if (this.isTable) {
            this.contents.write()
                .format("delta")
                .saveAsTable(this.collection.getLogicalName());
            // this.contents.writeTo(this.collection.getLogicalName())
            // .using("iceberg")
            // .createOrReplace();
        } else {
            this.contents.write()
                .format("delta")
                .option("path", this.collection.getLogicalName())
                .save();
        }

    }

}
