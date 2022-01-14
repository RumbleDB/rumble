package org.rumbledb.runtime.flwor;

import java.io.Serializable;

import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructType;
import org.rumbledb.context.Name;

public class FlworDataFrameColumn implements Serializable {
   
    private static final long serialVersionUID = 1L;

    enum ColumnFormat {
        SERIALIZED_SEQUENCE,
        NATIVE_SEQUENCE,
        FULLY_NATIVE,
        COUNT,
        SUM,
        MIN,
        MAX
    };
    private Name variableName;
    private ColumnFormat columnFormat;
    
    public FlworDataFrameColumn(Name variableName, ColumnFormat columnFormat)
    {
        this.variableName = variableName;
        this.columnFormat = columnFormat;
    }
    
    public FlworDataFrameColumn(String columnName, StructType inputSchema)
    {
        int pos = columnName.indexOf(".");
        if (pos == -1) {
            this.variableName = Name.createVariableInNoNamespace(columnName);
            int index = inputSchema.fieldIndex(columnName);
            if (inputSchema.fields()[index].dataType().equals(DataTypes.BinaryType)) {
                this.columnFormat = ColumnFormat.SERIALIZED_SEQUENCE;
                return;
            }
            this.columnFormat = ColumnFormat.FULLY_NATIVE;
            return;
        } else {
            this.variableName = Name.createVariableInNoNamespace(columnName.substring(0, pos));
            switch(columnName.substring(pos)) {
                case ".count":
                    this.columnFormat = ColumnFormat.COUNT;
                    break;
                case ".sequence":
                    this.columnFormat = ColumnFormat.NATIVE_SEQUENCE;
                    break;
                case ".sum":
                    this.columnFormat = ColumnFormat.SUM;
                    break;
                case ".max":
                    this.columnFormat = ColumnFormat.MAX;
                    break;
                case ".min":
                    this.columnFormat = ColumnFormat.MIN;
                    break;
                default:
            }
        }
    }
    
    public String toString() {
        return "`" + getColumnName() + "`";
    }
    
    public String getColumnName() {
        switch(this.columnFormat) {
            case SERIALIZED_SEQUENCE:
                return this.variableName.toString();
            case FULLY_NATIVE:
                return this.variableName.toString();
            case NATIVE_SEQUENCE:
                return this.variableName.toString() + ".sequence";
            case COUNT:
                return this.variableName.toString() + ".count";
            case SUM:
                return this.variableName.toString() + ".sum";
            case MAX:
                return this.variableName.toString() + ".max";
            case MIN:
                return this.variableName.toString() + ".min";
        }
        return null;
    }
    
    public boolean isSerializedSequence() {
        return this.columnFormat.equals(ColumnFormat.SERIALIZED_SEQUENCE);
    }
    
    public boolean isFullyNative() {
        return this.columnFormat.equals(ColumnFormat.FULLY_NATIVE);
    }
    
    public boolean isNativeSequence() {
        return this.columnFormat.equals(ColumnFormat.NATIVE_SEQUENCE);
    }
    
    public boolean isCount() {
        return this.columnFormat.equals(ColumnFormat.COUNT);
    }
    
    public boolean isSum() {
        return this.columnFormat.equals(ColumnFormat.SUM);
    }
    
    public boolean isMin() {
        return this.columnFormat.equals(ColumnFormat.MIN);
    }
    
    public boolean isMax() {
        return this.columnFormat.equals(ColumnFormat.MAX);
    }

    public Name getVariableName() {
        return this.variableName;
    }

}
