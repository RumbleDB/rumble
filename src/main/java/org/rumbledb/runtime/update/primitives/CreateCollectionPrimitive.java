package org.rumbledb.runtime.update.primitives;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.AnalysisException;
import org.apache.spark.sql.catalyst.analysis.TableAlreadyExistsException;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.TooManyCollectionCreationsOnSameTargetException;

import sparksoniq.spark.SparkSessionManager;

import static org.apache.spark.sql.functions.monotonically_increasing_id;


public class CreateCollectionPrimitive implements UpdatePrimitive {
    private final Collection collection;
    private Dataset<Row> contents;
    private final ExceptionMetadata metadata;

    public CreateCollectionPrimitive(
            Collection collection,
            Dataset<Row> contents,
            ExceptionMetadata metadata
    ) {
        this.collection = collection;
        this.contents = contents;
        this.metadata = metadata;
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

        try {
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
                    // Create using the Iceberg catalog (can be custom if configured)
                    // and enable schema evolution at creation time.
                    this.contents.writeTo(this.collection.getLogicalName())
                        .using("iceberg")
                        .tableProperty("write.spark.accept-any-schema", "true")
                        .create();
                    break;
                default:
                    throw new UnsupportedOperationException(
                            "Create Collection: Unsupported collection mode: " + this.collection.getMode()
                    );
            }
        } catch (AnalysisException e) {
            // Error handling for duplicate create targets across formats.
            if (
                e instanceof TableAlreadyExistsException
                    || "DELTA_PATH_EXISTS".equals(e.getErrorClass())
            ) {
                throw new TooManyCollectionCreationsOnSameTargetException(
                        this.collection.getLogicalName(),
                        this.metadata
                );
            }
            throw new RuntimeException(e);
        }
    }
}
