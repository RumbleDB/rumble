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

public class InsertBeforeIntoCollectionPrimitive implements UpdatePrimitive {
    private final Collection collection;
    private final Item target;
    private Dataset<Row> contents;


    public InsertBeforeIntoCollectionPrimitive(
            Item target,
            Dataset<Row> contents,
            ExceptionMetadata metadata
    ) {
        this.target = target;
        this.contents = contents;
        String collectionPath = target.getTableLocation().substring(7, target.getTableLocation().length() - 1);
        this.collection = new Collection(Mode.DELTA, collectionPath);
    }

    @Override
    public boolean isInsertBeforeIntoCollection() {
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
        // String collection = this.target.getTableLocation();

        SparkSession session = SparkSessionManager.getInstance().getOrCreateSession();

        // Short aliases for randomized temp columns to keep expressions readable.
        final String tmpMaxRowId = SparkSessionManager.tempMaxRowIdColumnName;
        final String tmpRowNum = SparkSessionManager.tempRowNumColumnName;
        final String tmpRowNumSeq = SparkSessionManager.tempRowNumSeqColumnName;
        final String tmpRowNumOrder = SparkSessionManager.tempRowNumOrderColumnName;

        String selectRowOrderQuery = String.format(
            "SELECT %s from %s WHERE %s <= %f ORDER BY %s DESC LIMIT 2",
            SparkSessionManager.rowOrderColumnName,
            this.collection.getPhysicalName(),
            SparkSessionManager.rowOrderColumnName,
            targetRowOrder,
            SparkSessionManager.rowOrderColumnName
        );
        List<Row> res = session.sql(selectRowOrderQuery).collectAsList();

        double rowOrderBase, rowOrderMax;
        rowOrderMax = res.get(0).getAs(SparkSessionManager.rowOrderColumnName);
        if (res.size() == 1) {
            rowOrderBase = targetRowOrder - InsertFirstIntoCollectionPrimitive.INSERT_OFFSET;
        } else {
            rowOrderBase = res.get(1).getAs(SparkSessionManager.rowOrderColumnName);
        }

        // String selectRowIDQuery = String.format(
        //     "SELECT MAX(%s) as maxRowID FROM %s",
        //     SparkSessionManager.rowIdColumnName,
        //     this.collection.getPhysicalName()
        // );
        // long rowIDStart = session.sql(selectRowIDQuery).first().getAs("maxRowID");

        // Get highest current row id to seed new rows
        Row aggRow = session
            .table(this.collection.getPhysicalName())
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

        // String safeName = String.format("__insertb_tview_%s_%f_%f", collection, rowOrderBase, rowOrderMax)
        //     .replaceAll("[^a-zA-Z0-9_]", "_");
        // this.contents.createOrReplaceTempView(safeName);
        // String insertQuery = String.format(
        //     "INSERT INTO %s SELECT * FROM %s",
        //     collection,
        //     safeName
        // );
        // session.sql(insertQuery);
        // session.catalog().dropTempView(safeName);



        this.contents = InsertFirstIntoCollectionPrimitive.insertInCollection(this.contents, this.collection);
    }

}
