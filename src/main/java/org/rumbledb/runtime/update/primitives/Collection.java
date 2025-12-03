package org.rumbledb.runtime.update.primitives;

import org.rumbledb.runtime.update.primitives.Mode;


public class Collection {
    private Mode mode;
    private String logicalName;
    private String physicalName;

    /**
     * @param mode The storage mode of the collection (HIVE, DELTA, etc.)
     * @param collectionPath The logical path of the collection
     */    
    public Collection(Mode mode, String collectionPath) {
        this.mode = mode;
        this.logicalName = collectionPath;
        this.physicalName = (mode == Mode.HIVE) ? collectionPath 
            : (mode == Mode.DELTA) ? "delta.`" + collectionPath + "`"
            : "";
    }

    public Mode getMode() {
        return this.mode;
    }

    public String getLogicalName() {
        return this.logicalName;
    }

    public String getPhysicalName() {
        return this.physicalName;
    }   
}