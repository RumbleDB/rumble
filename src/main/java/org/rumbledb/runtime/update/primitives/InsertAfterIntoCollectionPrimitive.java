package org.rumbledb.runtime.update.primitives;

import java.util.List;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.expressions.Window;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.rumbledb.api.Item;
import org.rumbledb.exceptions.ExceptionMetadata;
import sparksoniq.spark.SparkSessionManager;

import static org.apache.spark.sql.functions.expr;
import static org.apache.spark.sql.functions.lit;
import static org.apache.spark.sql.functions.max;
import static org.apache.spark.sql.functions.monotonically_increasing_id;
import static org.apache.spark.sql.functions.row_number;

public class InsertAfterIntoCollectionPrimitive implements UpdatePrimitive {
    private final Item target;
    private Dataset<Row> contents;
    private final Collection collection;

    public InsertAfterIntoCollectionPrimitive(
            Item target,
            Dataset<Row> contents,
            ExceptionMetadata metadata
    ) {
        this.target = target;
        this.contents = contents;
        this.collection = target.getCollection();
    }

    @Override
    public boolean isInsertAfterIntoCollection() {
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
        double targetRowOrder = this.target.getTopLevelOrder();
        String collectionTableName = this.collection.getPhysicalName();

        SparkSession session = SparkSessionManager.getInstance().getOrCreateSession();

        // Short aliases for randomized temp columns
        final String tmpMaxRowId = SparkSessionManager.tempMaxRowIdColumnName;
        final String tmpRowNum = SparkSessionManager.tempRowNumColumnName;
        final String tmpRowNumSeq = SparkSessionManager.tempRowNumSeqColumnName;
        final String tmpRowNumOrder = SparkSessionManager.tempRowNumOrderColumnName;

        String selectRowOrderQuery = String.format(
            "SELECT %s from %s WHERE %s >= %f ORDER BY %s ASC LIMIT 2",
            SparkSessionManager.rowOrderColumnName,
            collectionTableName,
            SparkSessionManager.rowOrderColumnName,
            targetRowOrder,
            SparkSessionManager.rowOrderColumnName
        );
        List<Row> res = session.sql(selectRowOrderQuery).collectAsList();

        // Calculate row order bounds
        double rowOrderBase, rowOrderMax;
        rowOrderBase = res.get(0).getAs(SparkSessionManager.rowOrderColumnName);
        if (res.size() == 1) {
            rowOrderMax = rowOrderBase + InsertFirstIntoCollectionPrimitive.INSERT_OFFSET;
        } else {
            rowOrderMax = res.get(1).getAs(SparkSessionManager.rowOrderColumnName);
        }

        // Get highest current row id to seed new rows
        Row aggRow = session
            .table(collectionTableName)
            .agg(max(SparkSessionManager.rowIdColumnName).alias(tmpMaxRowId))
            .first();
        long rowIDStart = aggRow.getAs(tmpMaxRowId);

        long rowCount = this.contents.count();
        double interval = (rowOrderMax - rowOrderBase) / (rowCount + 1);

        Dataset<Row> rowNumDF = this.contents.withColumn(tmpRowNum, monotonically_increasing_id());
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
            expr(String.format("%f + (%s) * %f", rowOrderBase, tmpRowNumOrder, interval)).cast("double")
        ).drop(tmpRowNumOrder);

        this.contents = rowNumOrderDF;

        // Insert the new rows into the collection
        this.collection.insertUnordered(this.contents);
    }

}
