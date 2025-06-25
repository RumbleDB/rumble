package org.rumbledb.runtime.update.primitives;

import org.rumbledb.api.Item;
import org.rumbledb.exceptions.CannotResolveUpdateSelectorException;
import org.rumbledb.exceptions.CannotRetrieveResourceException;
import org.rumbledb.exceptions.ExceptionMetadata;
import sparksoniq.spark.SparkSessionManager;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SaveMode;
import org.apache.spark.sql.SparkSession;

import static org.apache.spark.sql.functions.lit;
import static org.apache.spark.sql.functions.monotonically_increasing_id;


public class InsertTuplePrimitive implements UpdatePrimitive {
    private final String collection;
    private Dataset<Row> contents;
    private double rowOrderBase, rowOrderMax;

    public InsertTuplePrimitive(
        String collection, Dataset<Row> contents, double rowOrderBase, double rowOrderMax, ExceptionMetadata metadata
    ) {
        this.collection = collection;
        this.contents = contents;
        this.rowOrderBase = rowOrderBase;
        this.rowOrderMax = rowOrderMax;
    }

    @Override 
    public boolean isInsertTuple() {
        return true;
    }
    
    @Override
    public String getCollectionPath() {
        return this.collection;
    }

    @Override
    public double getRowOrderRangeBase() {
        return this.rowOrderBase;
    }

    @Override
    public double getRowOrderRangeMax() {
        return this.rowOrderMax;
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
        
        long rowCount = this.contents.count();
        double interval = ( this.rowOrderMax - this.rowOrderBase ) / (rowCount + 1);

        this.contents = this.contents.withColumn(
            SparkSessionManager.rowIdColumnName,
            monotonically_increasing_id()
        );

        this.contents = this.contents.withColumn(
            SparkSessionManager.rowOrderColumnName,
            monotonically_increasing_id().cast("double")
        );

    }

} 