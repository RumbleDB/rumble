package org.rumbledb.runtime.flwor;

import java.io.Serializable;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.StructType;

public class FLWORDataFrame implements Serializable {
    private static final long serialVersionUID = 1L;

    private Dataset<Row> dataFrame;
    private FLWORSchema flworSchema;


    public FLWORDataFrame(Dataset<Row> df) {
        this.dataFrame = df;
        this.flworSchema = new FLWORSchema();
        if (!isConsistent()) {
            df.printSchema();
            System.err.println(this.flworSchema.toString());
            // throw new OurBadException("Inconsistent FLWOR schema!");
        }
    }

    public FLWORDataFrame(Dataset<Row> df, FLWORSchema fs) {
        this.dataFrame = df;
        this.flworSchema = new FLWORSchema(fs);
    }

    public boolean isConsistent() {
        return this.flworSchema.isConsistent(this.dataFrame);
    }

    public Dataset<Row> getDataFrame() {
        return this.dataFrame;
    }

    public FLWORSchema getSchema() {
        return this.flworSchema;
    }

    public StructType schema() {
        return this.dataFrame.schema();
    }

    public void createOrReplaceTempView(String name) {
        this.dataFrame.createOrReplaceTempView(name);
    }

    public long count() {
        return this.dataFrame.count();
    }

    public SparkSession sparkSession() {
        return this.dataFrame.sparkSession();
    }

}
