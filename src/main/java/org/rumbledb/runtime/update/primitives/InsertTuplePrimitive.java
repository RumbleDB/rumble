// package org.rumbledb.runtime.update.primitives;

// import org.apache.spark.sql.Dataset;
// import org.apache.spark.sql.expressions.Window;
// import org.apache.spark.sql.Row;
// import org.apache.spark.sql.SparkSession;
// import org.apache.spark.sql.types.DataTypes;
// import org.rumbledb.exceptions.ExceptionMetadata;
// import sparksoniq.spark.SparkSessionManager;

// import static org.apache.spark.sql.functions.col;
// import static org.apache.spark.sql.functions.expr;
// import static org.apache.spark.sql.functions.lit;
// import static org.apache.spark.sql.functions.monotonically_increasing_id;
// import static org.apache.spark.sql.functions.row_number;


// public class InsertTuplePrimitive implements UpdatePrimitive {
//     private final String collection;
//     private Dataset<Row> contents;
//     private double rowOrderBase, rowOrderMax;

//     public InsertTuplePrimitive(
//             String collection,
//             Dataset<Row> contents,
//             double rowOrderBase,
//             double rowOrderMax,
//             ExceptionMetadata metadata
//     ) {
//         this.collection = collection;
//         this.contents = contents;
//         this.rowOrderBase = rowOrderBase;
//         this.rowOrderMax = rowOrderMax;
//     }

//     public static Dataset<Row> insertInDeltaMergeSchema(Dataset<Row> contents, String collection) {
//         // Cast to DoubleType
//         contents = contents.withColumn(
//             SparkSessionManager.rowOrderColumnName,
//             col(SparkSessionManager.rowOrderColumnName).cast(DataTypes.DoubleType)
//             );

//         System.out.println("Inserted after into collection " + collection);
            
//         // Insert in delta with mergeSchema enables
//         contents.write()
//             .format("delta")
//             .mode("append")
//             .option("mergeSchema", "true")
//             .save(collection);
        
//         return contents;
//     }

//     @Override
//     public boolean isInsertTuple() {
//         return true;
//     }

//     @Override
//     public String getCollectionPath() {
//         return this.collection;
//     }

//     @Override
//     public double getRowOrderRangeBase() {
//         return this.rowOrderBase;
//     }

//     @Override
//     public double getRowOrderRangeMax() {
//         return this.rowOrderMax;
//     }

//     @Override
//     public boolean hasSelector() {
//         return false;
//     }

//     @Override
//     public void apply() {
//         applyDelta();
//     }

//     @Override
//     public void applyItem() {
//         // The name of the collection is a string Item, therefore not required
//         // throw new Exception("Apply Item not implemented for Create Collection");
//         return;
//     }

//     @Override
//     public void applyDelta() {
//         SparkSession session = SparkSessionManager.getInstance().getOrCreateSession();

//         String selectQuery = String.format(
//             "SELECT MAX(%s) as maxRowID FROM %s",
//             SparkSessionManager.rowIdColumnName,
//             this.collection
//         );
//         long rowIDStart = session.sql(selectQuery).first().getAs("maxRowID");

//         long rowCount = this.contents.count();
//         double interval = (this.rowOrderMax - this.rowOrderBase) / (rowCount + 3);

//         // Adding metadata columns
//         Dataset<Row> rowNumDF = this.contents.withColumn("rowNum", monotonically_increasing_id());
//         Dataset<Row> rowNumDF2 = rowNumDF.withColumn(
//             "rowNumSeq",
//             row_number().over(Window.orderBy("rowNum"))
//         ).drop("rowNum");
//         Dataset<Row> rowIdDF = rowNumDF2.withColumn(
//             SparkSessionManager.rowIdColumnName,
//             expr(String.format("cast(%d as long) + rowNumSeq", rowIDStart))
//         ).drop("rowNumSeq");

//         Dataset<Row> rowNumDF3 = rowIdDF.withColumn(
//             "RowNumOrder",
//             row_number().over(Window.orderBy(lit(1)))
//         );
//         Dataset<Row> rowNumOrderDF = rowNumDF3.withColumn(
//             SparkSessionManager.rowOrderColumnName,
//             expr(String.format("%f + (rowNumOrder+1) * %f", this.rowOrderBase, interval))
//         ).drop("rowNumOrder");

//         this.contents = rowNumOrderDF;

//         // insertion
//         String safeName = String.format("__insert_tview_%s_%f_%f", this.collection, this.rowOrderBase, this.rowOrderMax)
//             .replaceAll("[^a-zA-Z0-9_]", "_");
//         this.contents.createOrReplaceTempView(safeName);

//         String insertQuery = String.format(
//             "INSERT INTO %s SELECT * FROM %s",
//             this.collection,
//             safeName
//         );
//         session.sql(insertQuery);

//         session.catalog().dropTempView(safeName);
//     }

// }
