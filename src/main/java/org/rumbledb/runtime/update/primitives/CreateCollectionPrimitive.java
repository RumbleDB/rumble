package org.rumbledb.runtime.update.primitives;

import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.rumbledb.exceptions.ExceptionMetadata;

import sparksoniq.spark.SparkSessionManager;

import static org.apache.spark.sql.functions.monotonically_increasing_id;


public class CreateCollectionPrimitive implements UpdatePrimitive {
    private Dataset<Row> contents;
    private Collection collection;

    public CreateCollectionPrimitive(
            Collection collection,
            Dataset<Row> contents,
            ExceptionMetadata metadata
    ) {
        this.collection = collection;
        this.contents = contents;
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

        switch (this.collection.getMode()) {
            case HIVE:
                this.contents.write()
                    .format("delta")
                    .saveAsTable(this.collection.getLogicalName());
                break;
            case DELTA:
                this.contents.write()
                    .format("delta")
                    .option("path", this.collection.getLogicalName())
                    .save();
                break;
            case ICEBERG:
                this.contents.writeTo(this.collection.getPhysicalName())
                    .using("iceberg")
                    .createOrReplace();

                // turn on schema evolution for the table
                SparkSession session = SparkSessionManager.getInstance().getOrCreateSession();
                session.sql(
                    String.format(
                        "ALTER TABLE %s SET TBLPROPERTIES ('write.spark.accept-any-schema'='true')",
                        this.collection.getPhysicalName()
                    )
                );
                break;
            default:
                throw new UnsupportedOperationException("Unsupported collection mode: " + this.collection.getMode());
        }

    }

}
