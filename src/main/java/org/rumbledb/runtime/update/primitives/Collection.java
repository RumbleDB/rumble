package org.rumbledb.runtime.update.primitives;

import java.io.Serializable;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.catalyst.analysis.NoSuchTableException;



public class Collection implements Serializable {
    private static final long serialVersionUID = 1L;
    private Mode mode;
    private String logicalName;
    private String physicalName;

    /**
     * Constructor for Collection using logical path.
     * 
     * @param mode The storage mode of the collection (HIVE, DELTA, etc.)
     * @param collectionPath The logical path of the collection
     */
    public Collection(Mode mode, String collectionPath) {
        this.mode = mode;
        switch (mode) {
            case HIVE:
                this.logicalName = collectionPath;
                this.physicalName = collectionPath;
                break;
            case DELTA:
                this.logicalName = collectionPath;
                this.physicalName = "delta.`" + collectionPath + "`";
                break;
            case ICEBERG:
                this.logicalName = "iceberg." + collectionPath;
                this.physicalName = "iceberg." + collectionPath;
                break;
        }
    }

    /**
     * Constructor for Collection using physical path.
     * 
     * @param collectionPath The physical path of the collection
     */
    public Collection(String collectionPath) {
        if (collectionPath.startsWith("delta.`") && collectionPath.endsWith("`")) {
            // case Delta file
            this.mode = Mode.DELTA;
            this.logicalName = collectionPath.substring(7, collectionPath.length() - 1);
            this.physicalName = collectionPath;
        } else if (collectionPath.startsWith("iceberg.")) {
            // case Iceberg table
            this.mode = Mode.ICEBERG;
            this.logicalName = collectionPath;
            this.physicalName = collectionPath;
        } else {
            // case Hive table
            this.mode = Mode.HIVE;
            this.logicalName = collectionPath;
            this.physicalName = collectionPath;
        }
    }

    /**
     * @return The storage mode of the collection
     */
    public Mode getMode() {
        return this.mode;
    }

    /**
     * @return The logical name of the collection
     */
    public String getLogicalName() {
        return this.logicalName;
    }

    /**
     * @return The physical name of the collection
     */
    public String getPhysicalName() {
        return this.physicalName;
    }

    /**
     * Inserts the given contents into the collection according to its mode.
     * This method does not handle ordering of the inserted contents.
     * 
     * @param contents The dataset to insert into the collection
     */
    public void insertUnordered(Dataset<Row> contents) {
        try {
            switch (this.mode) {
                case HIVE:
                    contents.write()
                        .mode("append")
                        .insertInto(this.logicalName);
                    break;
                case DELTA:
                    contents.write()
                        .format("delta")
                        .mode("append")
                        .option("mergeSchema", "true")
                        .save(this.logicalName);
                    break;
                case ICEBERG:
                    contents.writeTo(this.logicalName)
                        .option("mergeSchema", "true")
                        .append();
                    break;
                default:
                    throw new UnsupportedOperationException("Unsupported collection mode: " + this.mode);
            }
        } catch (NoSuchTableException e) {
            throw new RuntimeException("Target collection not found: " + this.logicalName, e);
        }
    }
}
