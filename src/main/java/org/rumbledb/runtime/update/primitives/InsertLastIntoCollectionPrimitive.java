package org.rumbledb.runtime.update.primitives;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.expressions.Window;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.rumbledb.exceptions.ExceptionMetadata;
import sparksoniq.spark.SparkSessionManager;

import static org.apache.spark.sql.functions.expr;
import static org.apache.spark.sql.functions.lit;
import static org.apache.spark.sql.functions.max;
import static org.apache.spark.sql.functions.monotonically_increasing_id;
import static org.apache.spark.sql.functions.row_number;

public class InsertLastIntoCollectionPrimitive implements UpdatePrimitive {
    private final Collection collection;
    private Dataset<Row> contents;
    private boolean isTable;

    public InsertLastIntoCollectionPrimitive(
            String collectionPath,
            Dataset<Row> contents,
            boolean isTable,
            ExceptionMetadata metadata
    ) {
        this.contents = contents;
        this.isTable = isTable;
        this.collection = (isTable) 
            ? new Collection(Mode.HIVE, collectionPath)
            : new Collection(Mode.DELTA, collectionPath);
    }

    @Override
    public boolean isInsertLastIntoCollection() {
        return true;
    }

    @Override
    public String getCollectionPath() {
        // return this.isTable
        //     ? this.collection
        //     : "delta.`" + this.collection + "`";
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

        // Short aliases for randomized temp columns to keep expressions readable.
        final String tmpMaxRowId = SparkSessionManager.tempMaxRowIdColumnName;
        final String tmpMaxRowOrder = SparkSessionManager.tempMaxRowOrderColumnName;
        final String tmpRowNum = SparkSessionManager.tempRowNumColumnName;
        final String tmpRowNumSeq = SparkSessionManager.tempRowNumSeqColumnName;
        final String tmpRowNumOrder = SparkSessionManager.tempRowNumOrderColumnName;

        // String selectQuery = String.format(
        //     "SELECT MAX(%s) as maxRowID FROM %s",
        //     SparkSessionManager.rowIdColumnName,
        //     this.collection.getPhysicalName()
        // );
        // Long rowIDStart = session.sql(selectQuery).first().getAs("maxRowID");
        // rowIDStart = rowIDStart == null ? 0L : rowIDStart;

        // String selectRowOrderQuery = String.format(
        //     "SELECT MAX(%s) as maxRowOrder FROM %s",
        //     SparkSessionManager.rowOrderColumnName,
        //     this.collection.getPhysicalName()
        // );
        // Double lastRowOrder = session.sql(selectRowOrderQuery).first().getAs("maxRowOrder");

        // Get highest current row id to seed new rows and maximum row order to calculate base
        Row aggRow = session
            .table(this.collection.getPhysicalName())
            .agg(
                max(SparkSessionManager.rowIdColumnName).alias(tmpMaxRowId),
                max(SparkSessionManager.rowOrderColumnName).alias(tmpMaxRowOrder)
            )
            .first();
        Long rowIDStart = aggRow.getAs(tmpMaxRowId);
        rowIDStart = rowIDStart == null ? 0L : rowIDStart;

        // Calculate row order base
        Double lastRowOrder = aggRow.getAs(tmpMaxRowOrder);
        double interval = 1.0;
        double rowOrderBase;
        if (lastRowOrder == null) {
            rowOrderBase = 0.0;
        } else {
            rowOrderBase = lastRowOrder + InsertFirstIntoCollectionPrimitive.INSERT_OFFSET;
        }

        // Adding metadata columns
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
            expr(String.format("%f + (%s - 1) * %f", rowOrderBase, tmpRowNumOrder, interval)).cast("double")
        ).drop(tmpRowNumOrder);

        this.contents = rowNumOrderDF;

        // Double rowOrderBase = session.sql(selectRowOrderQuery).first().getAs("maxRowOrder");
        // if (rowOrderBase == null) {
        //     rowOrderBase = 0.0; // Table is empty
        // }
        // double interval = 1.0; // Or any spacing you want

        // // Assign rowOrder: strictly increment for each new row
        // Dataset<Row> rowNumDF3 = rowIdDF.withColumn(
        //     "RowNumOrder", row_number().over(Window.orderBy(lit(1)))
        // );

        // Dataset<Row> rowNumOrderDF = rowNumDF3.withColumn(
        //     SparkSessionManager.rowOrderColumnName,
        //     expr(String.format("CAST(%f + (%s-1)*%f AS DOUBLE)", rowOrderBase, "RowNumOrder", interval))
        // ).drop("RowNumOrder");

        // this.contents = rowNumOrderDF;


        // insertion
        // String safeName = String.format("__insert_tview_%s_%f_%f", this.collection, rowOrderBase, rowOrderMax)
        //     .replaceAll("[^a-zA-Z0-9_]", "_");
        // this.contents.createOrReplaceTempView(safeName);
        // String insertQuery = String.format(
        //     "INSERT INTO %s SELECT * FROM %s",
        //     this.getCollectionPath(),
        //     safeName
        // );
        // session.sql(insertQuery);
        // session.catalog().dropTempView(safeName);



        this.contents = InsertFirstIntoCollectionPrimitive.insertInCollection(this.contents, this.collection);
    }

}
