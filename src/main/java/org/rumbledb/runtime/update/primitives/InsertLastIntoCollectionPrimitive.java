package org.rumbledb.runtime.update.primitives;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.expressions.Window;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.rumbledb.exceptions.ExceptionMetadata;
import sparksoniq.spark.SparkSessionManager;

import static org.apache.spark.sql.functions.expr;
import static org.apache.spark.sql.functions.lit;
import static org.apache.spark.sql.functions.monotonically_increasing_id;
import static org.apache.spark.sql.functions.row_number;


public class InsertLastIntoCollectionPrimitive implements UpdatePrimitive {

    private final String collection;
    private Dataset<Row> contents;
    private boolean isTable;

    public InsertLastIntoCollectionPrimitive(
            String collection,
            Dataset<Row> contents,
            boolean isTable,
            ExceptionMetadata metadata
    ) {
        this.collection = collection;
        this.contents = contents;
        this.isTable = isTable;
    }

    @Override
    public boolean isInsertLastIntoCollection() {
        return true;
    }

    @Override
    public String getCollectionPath() {
        return this.isTable
            ? this.collection
            : "delta.`" + this.collection + "`";
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

        String selectQuery = String.format(
            "SELECT MAX(%s) as maxRowID FROM %s",
            SparkSessionManager.rowIdColumnName,
            this.getCollectionPath()
        );
        Long rowIDStart = session.sql(selectQuery).first().getAs("maxRowID");
        rowIDStart = rowIDStart == null ? 0L : rowIDStart;


        long rowCount = this.contents.count();

        Double rowOrderMax = null;
        String selectRowOrderQuery = String.format(
            "SELECT MAX(%s) as maxRowOrder FROM %s",
            SparkSessionManager.rowOrderColumnName,
            this.getCollectionPath()
        );
        Double rowOrderBase = session.sql(selectRowOrderQuery).first().getAs("maxRowOrder");
        if (rowOrderBase == null) {
            rowOrderBase = 0.0;
            rowOrderMax = (double) rowCount;
        } else {
            rowOrderMax = 2.0 * rowOrderBase;
        }

        double interval = (rowOrderMax - rowOrderBase) / (rowCount + 3);

        // Adding metadata columns
        Dataset<Row> rowNumDF = this.contents.withColumn("rowNum", monotonically_increasing_id());
        Dataset<Row> rowNumDF2 = rowNumDF.withColumn(
            "rowNumSeq",
            row_number().over(Window.orderBy("rowNum"))
        ).drop("rowNum");
        Dataset<Row> rowIdDF = rowNumDF2.withColumn(
            SparkSessionManager.rowIdColumnName,
            expr(String.format("cast(%d as long) + rowNumSeq", rowIDStart))
        ).drop("rowNumSeq");

        Dataset<Row> rowNumDF3 = rowIdDF.withColumn(
            "RowNumOrder",
            row_number().over(Window.orderBy(lit(1)))
        );
        Dataset<Row> rowNumOrderDF = rowNumDF3.withColumn(
            SparkSessionManager.rowOrderColumnName,
            expr(String.format("%f + (rowNumOrder+1) * %f", rowOrderBase, interval))
        ).drop("rowNumOrder");

        this.contents = rowNumOrderDF;

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

        this.contents = InsertFirstIntoCollectionPrimitive.insertInDeltaMergeSchema(this.contents, this.collection);
    }

}
