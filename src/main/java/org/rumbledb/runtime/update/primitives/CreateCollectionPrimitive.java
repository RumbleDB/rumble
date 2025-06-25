package org.rumbledb.runtime.update.primitives;

import org.rumbledb.api.Item;
import org.rumbledb.exceptions.CannotResolveUpdateSelectorException;
import org.rumbledb.exceptions.ExceptionMetadata;
import sparksoniq.spark.SparkSessionManager;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SaveMode;

import static org.apache.spark.sql.functions.lit;
import static org.apache.spark.sql.functions.monotonically_increasing_id;


public class CreateCollectionPrimitive implements UpdatePrimitive {
    private Dataset<Row> contents;
    private String collectionName;
    private boolean isTable;

    public CreateCollectionPrimitive(String collectionName, Dataset<Row> contents, boolean isTable, ExceptionMetadata metadata) {
        // The target should be the name of the collection, an object of the class String
        this.collectionName = collectionName;
        this.contents = contents;
        this.isTable = isTable;
    }

    @Override
    public boolean isCreateCollection() {
        return true;
    }
    
    @Override
    public String getCollectionPath() {
        return this.isTable 
                ? this.collectionName 
                : "delta." + this.collectionName;
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
                         .saveAsTable(this.collectionName);
        } else {
            this.contents.write()
                         .format("delta")
                         .option("path", "delta/"+this.collectionName)
                         .save();
        }

    }

}
