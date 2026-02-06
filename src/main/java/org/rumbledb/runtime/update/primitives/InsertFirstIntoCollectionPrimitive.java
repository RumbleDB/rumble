package org.rumbledb.runtime.update.primitives;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.expressions.Window;

import org.apache.spark.sql.SparkSession;
import org.rumbledb.exceptions.ExceptionMetadata;
import sparksoniq.spark.SparkSessionManager;

import static org.apache.spark.sql.functions.expr;
import static org.apache.spark.sql.functions.lit;
import static org.apache.spark.sql.functions.max;
import static org.apache.spark.sql.functions.min;
import static org.apache.spark.sql.functions.monotonically_increasing_id;
import static org.apache.spark.sql.functions.row_number;

public class InsertFirstIntoCollectionPrimitive implements UpdatePrimitive {
    public static final double INSERT_OFFSET = 1000.0;

    private final Collection collection;
    private Dataset<Row> contents;

    public InsertFirstIntoCollectionPrimitive(
            Collection collection,
            Dataset<Row> contents,
            ExceptionMetadata metadata
    ) {
        this.contents = contents;
        this.collection = collection;
    }

    @Override
    public boolean isInsertFirstIntoCollection() {
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
    public void apply() {
        applyDelta();
    }

    @Override
    public void applyItem() {
        return;
    }

    @Override
    public void applyDelta() {
        SparkSession session = SparkSessionManager.getInstance().getOrCreateSession();

        // Short aliases for randomized temp columns
        final String tmpMaxRowId = SparkSessionManager.tempMaxRowIdColumnName;
        final String tmpMinRowOrder = SparkSessionManager.tempMinRowOrderColumnName;
        final String tmpRowNum = SparkSessionManager.tempRowNumColumnName;
        final String tmpRowNumSeq = SparkSessionManager.tempRowNumSeqColumnName;
        final String tmpRowNumOrder = SparkSessionManager.tempRowNumOrderColumnName;

        String collectionTableName = this.collection.getPhysicalName();

        // Get highest current row id to seed new rows and minimum row order to calculate base
        Row aggRow = session.table(collectionTableName)
            .agg(
                max(SparkSessionManager.rowIdColumnName).alias(tmpMaxRowId),
                min(SparkSessionManager.rowOrderColumnName).alias(tmpMinRowOrder)
            )
            .first();
        Long rowIDStart = aggRow.getAs(tmpMaxRowId);
        rowIDStart = rowIDStart == null ? 0L : rowIDStart;

        // Calculate row order base
        long rowCount = this.contents.count();
        Double firstRowOrder = aggRow.getAs(tmpMinRowOrder);
        double interval = 1.0;
        double rowOrderBase = firstRowOrder == null
            ? 0.0
            : (firstRowOrder - INSERT_OFFSET) - (rowCount - 1) * interval;

        // Adding metadata columns
        Dataset<Row> rowNumDF = this.contents.withColumn(
            tmpRowNum,
            monotonically_increasing_id()
        );
        Dataset<Row> rowNumDF2 = rowNumDF.withColumn(
            tmpRowNumSeq,
            row_number().over(Window.orderBy(tmpRowNum))
        ).drop(tmpRowNum);
        Dataset<Row> rowIdDF = rowNumDF2.withColumn(
            SparkSessionManager.rowIdColumnName,
            expr(String.format("cast(%d as long) + %s", rowIDStart, tmpRowNumSeq))
        ).drop(tmpRowNumSeq);

        Dataset<Row> rowNumDF3 = rowIdDF.withColumn(
            tmpRowNumOrder,
            row_number().over(Window.orderBy(lit(1)))
        );
        Dataset<Row> rowNumOrderDF = rowNumDF3.withColumn(
            SparkSessionManager.rowOrderColumnName,
            expr(String.format("%f + (%s - 1) * %f", rowOrderBase, tmpRowNumOrder, interval)).cast("double")
        ).drop(tmpRowNumOrder);

        this.contents = rowNumOrderDF;

        // Insert the new rows into the collection
        this.collection.insertUnordered(this.contents);
    }

}
