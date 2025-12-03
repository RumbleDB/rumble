package org.rumbledb.runtime.update.primitives;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.expressions.Window;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DataTypes;
import org.rumbledb.exceptions.ExceptionMetadata;
import sparksoniq.spark.SparkSessionManager;

import static org.apache.spark.sql.functions.col;
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
    private boolean isTable;

    public InsertFirstIntoCollectionPrimitive(
            String collectionPath,
            Dataset<Row> contents,
            boolean isTable,
            ExceptionMetadata metadata
    ) {
        // System.out.println("\n\nCollection Path: " + collectionPath + "\n\n");
        this.contents = contents;
        this.isTable = isTable;
        this.collection = (isTable) 
        ? new Collection(Mode.HIVE, collectionPath) 
        : new Collection(Mode.DELTA, collectionPath);
    }

    public static Dataset<Row> insertInCollection(Dataset<Row> contents, Collection collection) {
        // Cast to DoubleType
        // contents = contents.withColumn(
        //     SparkSessionManager.rowOrderColumnName,
        //     col(SparkSessionManager.rowOrderColumnName).cast(DataTypes.DoubleType)
        //     );

        System.out.println("Inserted after into collection " + collection.getLogicalName());
            
        // Insert respectively according to collection mode
        Mode mode = collection.getMode();
        switch (mode) {
            case HIVE:
                contents.write()
                    .mode("append")
                    .insertInto(collection.getLogicalName());
                break;
            case DELTA:
                contents.write()
                    .format("delta")
                    .mode("append")
                    .option("mergeSchema", "true")
                    .save(collection.getLogicalName());
                break;
            default:
                throw new UnsupportedOperationException("Unsupported collection mode: " + mode);
        }

        return contents;
    }

    @Override
    public boolean isInsertFirstIntoCollection() {
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

        // Short aliases for randomized temp columns
        final String tmpMaxRowId = SparkSessionManager.tempMaxRowIdColumnName;
        final String tmpMinRowOrder = SparkSessionManager.tempMinRowOrderColumnName;
        final String tmpRowNum = SparkSessionManager.tempRowNumColumnName;
        final String tmpRowNumSeq = SparkSessionManager.tempRowNumSeqColumnName;
        final String tmpRowNumOrder = SparkSessionManager.tempRowNumOrderColumnName;

        String collectionTableName = this.collection.getPhysicalName();
        // if (!isTable) {
        //     Dataset<Row> dataFrame = SparkSessionManager.getInstance()
        //     .getOrCreateSession()
        //     .read()
        //     .format("delta")
        //     .load(this.collection.getLogicalName());
        //     collectionTableName = String.format("__query_tview_%s", this.collection.getLogicalName())
        //     .replaceAll("[^a-zA-Z0-9_]", "_");
        //     dataFrame.createOrReplaceTempView(collectionTableName);
        // }
        
        // String selectQuery = String.format(
        //     "SELECT MAX(%s) as maxRowID FROM %s",
        //     SparkSessionManager.rowIdColumnName,
        //     collectionTableName
        //     );
        //     Long rowIDStart = session.sql(selectQuery).first().getAs("maxRowID");
        //     rowIDStart = rowIDStart == null ? 0L : rowIDStart;
            
        //     long rowCount = this.contents.count();
            
        // String selectRowOrderQuery = String.format(
        //     "SELECT MIN(%s) as minRowOrder FROM %s",
        //     SparkSessionManager.rowOrderColumnName,
        //     collectionTableName
        // );
        // Double firstRowOrder = session.sql(selectRowOrderQuery).first().getAs("minRowOrder");

        // Get highest current row id to seed new rows and minimum row order to calculate base
        Row aggRow = session.table(collectionTableName).agg(
            max(SparkSessionManager.rowIdColumnName).alias(tmpMaxRowId),
            min(SparkSessionManager.rowOrderColumnName).alias(tmpMinRowOrder)
        ).first();
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
            expr(
                String.format(
                    "%f + (%s - 1) * %f",
                    rowOrderBase,
                    tmpRowNumOrder,
                    interval
                )
            ).cast("double")
        ).drop(tmpRowNumOrder);

        this.contents = rowNumOrderDF;

        
        // insertion
        // String safeName = String.format("__insert_tview_%s_%f_%f", this.collection.getLogicalName(), rowOrderBase, rowOrderMax)
        //     .replaceAll("[^a-zA-Z0-9_]", "_");
        // this.contents.createOrReplaceTempView(safeName);
        // String insertQuery = String.format(
        //         "INSERT INTO %s SELECT * FROM %s",
        //         collectionTableName,
        //         safeName
        //     );
        //     session.sql(insertQuery);
        //     session.catalog().dropTempView(safeName);
        

        this.contents = InsertFirstIntoCollectionPrimitive.insertInCollection(this.contents, this.collection);
    }

}
