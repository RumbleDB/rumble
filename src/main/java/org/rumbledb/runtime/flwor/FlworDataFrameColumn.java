/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Authors: Ghislain Fourny
 *
 */

package org.rumbledb.runtime.flwor;

import java.io.Serializable;

import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructType;
import org.rumbledb.context.Name;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.types.ItemType;
import org.rumbledb.types.SequenceType;
import org.rumbledb.types.SequenceType.Arity;
import org.rumbledb.types.TypeMappings;

public class FlworDataFrameColumn implements Serializable {

    private static final long serialVersionUID = 1L;

    public enum ColumnFormat {
        SERIALIZED_SEQUENCE,
        NATIVE_SEQUENCE,
        FULLY_NATIVE,
        COUNT,
        SUM,
        MIN,
        MAX,
        AVERAGE
    };

    private String tableName;
    private Name variableName;
    private ColumnFormat columnFormat;

    public FlworDataFrameColumn(
            String tableName,
            Name variableName,
            ColumnFormat columnFormat,
            SequenceType sequenceType
    ) {
        this.tableName = tableName;
        this.variableName = variableName;
        this.columnFormat = columnFormat;
    }

    public FlworDataFrameColumn(String tableName, Name variableName, ColumnFormat columnFormat) {
        this.tableName = tableName;
        this.variableName = variableName;
        this.columnFormat = columnFormat;
    }

    public FlworDataFrameColumn(Name variableName, ColumnFormat columnFormat) {
        this.variableName = variableName;
        this.columnFormat = columnFormat;
    }

    public FlworDataFrameColumn(String columnName, StructType inputSchema) {
        this.tableName = null;
        this.columnFormat = getColumnFormat(columnName, inputSchema);
        int pos = columnName.indexOf(".");
        if (pos == -1) {
            this.variableName = Name.createVariableInNoNamespace(columnName);
        } else {
            this.variableName = Name.createVariableInNoNamespace(columnName.substring(0, pos));
        }
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof FlworDataFrameColumn)) {
            return false;
        }
        FlworDataFrameColumn other = (FlworDataFrameColumn) o;
        if (this.tableName == null && other.tableName != null) {
            return false;
        }
        if (this.tableName != null && other.tableName == null) {
            return false;
        }
        if (this.tableName != null && !this.tableName.equals(other.tableName)) {
            return false;
        }
        if (!this.variableName.equals(other.variableName)) {
            return false;
        }
        if (!this.columnFormat.equals(other.columnFormat)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        if (this.tableName == null) {
            return this.variableName.hashCode() + this.columnFormat.hashCode();
        }
        return this.tableName.hashCode() + this.variableName.hashCode() + this.columnFormat.hashCode();
    }

    public static SequenceType getSequenceTypeFromColumn(String columnName, StructType inputSchema) {
        int pos = columnName.indexOf(".");
        if (pos == -1) {
            int index = inputSchema.fieldIndex(columnName);
            if (inputSchema.fields()[index].dataType().equals(DataTypes.BinaryType)) {
                return SequenceType.ITEM_STAR;
            }
            ItemType itemType = TypeMappings.getItemTypeFromDataFrameDataType(inputSchema.fields()[index].dataType());
            return new SequenceType(itemType, Arity.ZeroOrMore);
        } else {
            switch (columnName.substring(pos)) {
                case ".count":
                    return SequenceType.ITEM_STAR;
                case ".sequence":
                    return SequenceType.ITEM_STAR;
                case ".sum":
                    return SequenceType.ITEM_STAR;
                case ".max":
                    return SequenceType.ITEM_STAR;
                case ".min":
                    return SequenceType.ITEM_STAR;
                case ".average":
                    return SequenceType.ITEM_STAR;
                default:
                    throw new OurBadException("Unrecognized column name: " + columnName);
            }
        }
    }

    public static ColumnFormat getColumnFormat(String columnName, StructType inputSchema) {
        int pos = columnName.indexOf(".");
        if (pos == -1) {
            int index = inputSchema.fieldIndex(columnName);
            if (inputSchema.fields()[index].dataType().equals(DataTypes.BinaryType)) {
                return ColumnFormat.SERIALIZED_SEQUENCE;
            }
            return ColumnFormat.FULLY_NATIVE;
        } else {
            switch (columnName.substring(pos)) {
                case ".count":
                    return ColumnFormat.COUNT;
                case ".sequence":
                    return ColumnFormat.NATIVE_SEQUENCE;
                case ".sum":
                    return ColumnFormat.SUM;
                case ".max":
                    return ColumnFormat.MAX;
                case ".min":
                    return ColumnFormat.MIN;
                case ".average":
                    return ColumnFormat.AVERAGE;
                default:
                    throw new OurBadException("Unrecognized column name: " + columnName);
            }
        }
    }

    public String toString() {
        if (this.tableName != null) {
            return "`" + this.tableName + "`.`" + getColumnName() + "`";
        }
        return "`" + getColumnName() + "`";
    }

    public String getTableName() {
        return this.tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getColumnName() {
        switch (this.columnFormat) {
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
            case AVERAGE:
                return this.variableName.toString() + ".average";
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

    public boolean isAverage() {
        return this.columnFormat.equals(ColumnFormat.AVERAGE);
    }

    public Name getVariableName() {
        return this.variableName;
    }

}
