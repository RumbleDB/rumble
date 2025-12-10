package org.rumbledb.runtime.update.primitives;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import org.rumbledb.runtime.update.primitives.Mode;


public class Collection {
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
        this.logicalName = collectionPath;
        this.physicalName = (mode == Mode.HIVE) ? collectionPath 
            : (mode == Mode.DELTA) ? "delta.`" + collectionPath + "`"
            : "null";
    }

    /**
     * Constructor for Collection using physical path.
     * 
     * @param collectionPath The physical path of the collection
     */
    public Collection(String collectionPath) {
        if (collectionPath.startsWith("delta.`") && collectionPath.endsWith("`")) {
            this.mode = Mode.DELTA;
            this.logicalName = collectionPath.substring(7, collectionPath.length() - 1);
        } else {
            this.mode = Mode.HIVE;
            this.logicalName = collectionPath;
        }
        this.physicalName = collectionPath;
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
            default:
                throw new UnsupportedOperationException("Unsupported collection mode: " + this.mode);
        }
    }
}