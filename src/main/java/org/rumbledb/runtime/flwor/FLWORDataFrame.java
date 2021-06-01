package org.rumbledb.runtime.flwor;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import org.rumbledb.context.Name;
import org.rumbledb.items.structured.JSoundDataFrame;
import org.rumbledb.types.ItemType;

public class FLWORDataFrame implements Serializable {
    private static final long serialVersionUID = 1L;

    private Dataset<Row> dataFrame;
    private Set<String> counts;
    private Set<String> sequencesAsArraysOfBytes;
    private Set<String> sequencesAsBytes;
    private Map<String, ItemType> nativelyTypedSequences;

    public FLWORDataFrame(Dataset<Row> df) {
        this.dataFrame = df;
        this.counts = new HashSet<>();
        this.sequencesAsArraysOfBytes = new HashSet<>();
        this.sequencesAsBytes = new HashSet<>();
        this.nativelyTypedSequences = new HashMap<>();
    }

    public FLWORDataFrame(Dataset<Row> df, FLWORDataFrame fdf) {
        this.dataFrame = df;
        this.counts = new HashSet<>(fdf.counts);
        this.sequencesAsArraysOfBytes = new HashSet<>(fdf.sequencesAsArraysOfBytes);
        this.sequencesAsBytes = new HashSet<>(fdf.sequencesAsBytes);
        this.nativelyTypedSequences = new HashMap<>(fdf.nativelyTypedSequences);
    }

    public void setCount(Name name) {
        this.counts.add(name.toString());
    }

    public void setArraysOfBytes(Name name) {
        this.sequencesAsArraysOfBytes.add(name.toString());
    }

    public void setBytes(Name name) {
        this.sequencesAsBytes.add(name.toString());
    }

    public void setNativeType(Name name, ItemType itemType) {
        this.nativelyTypedSequences.put(name.toString(), itemType);
    }

    public boolean isConsistent() {
        StructType schema = this.dataFrame.schema();
        for (StructField field : schema.fields()) {
            String fieldName = field.name();
            if (this.counts.contains(fieldName)) {
                if (this.sequencesAsBytes.contains(fieldName)) {
                    return false;
                }
                if (this.sequencesAsArraysOfBytes.contains(fieldName)) {
                    return false;
                }
                if (this.nativelyTypedSequences.containsKey(fieldName)) {
                    return false;
                }
                return true;
            }
            if (this.sequencesAsArraysOfBytes.contains(fieldName)) {
                if (this.counts.contains(fieldName)) {
                    return false;
                }
                if (this.sequencesAsBytes.contains(fieldName)) {
                    return false;
                }
                if (this.nativelyTypedSequences.containsKey(fieldName)) {
                    return false;
                }
                return true;
            }
            if (this.sequencesAsBytes.contains(fieldName)) {
                if (this.sequencesAsArraysOfBytes.contains(fieldName)) {
                    return false;
                }
                if (this.counts.contains(fieldName)) {
                    return false;
                }
                if (this.nativelyTypedSequences.containsKey(fieldName)) {
                    return false;
                }
                return true;
            }
            if (!this.nativelyTypedSequences.containsKey(fieldName)) {
                return false;
            }
        }
        Set<String> allExpectedColumns = new HashSet<>();
        allExpectedColumns.addAll(this.counts);
        allExpectedColumns.addAll(this.sequencesAsArraysOfBytes);
        allExpectedColumns.addAll(this.sequencesAsBytes);
        allExpectedColumns.addAll(this.nativelyTypedSequences.keySet());
        for (String key : allExpectedColumns) {
            boolean found = false;
            for (StructField field : schema.fields()) {
                if (field.name().equals(key)) {
                    found = true;
                }
            }
            if (!found) {
                return false;
            }
        }
        return true;
    }

    public Dataset<Row> getDataFrame() {
        return this.dataFrame;
    }

    public boolean isCount(Name name) {
        return this.counts.contains(name.toString());
    }

    public boolean isArraysOfBytes(Name name) {
        return this.sequencesAsArraysOfBytes.contains(name.toString());
    }

    public boolean isBytes(Name name) {
        return this.sequencesAsBytes.contains(name.toString());
    }

    public ItemType getNativeType(Name name) {
        return this.nativelyTypedSequences.get(name.toString());
    }
    
    public StructType schema()
    {
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
