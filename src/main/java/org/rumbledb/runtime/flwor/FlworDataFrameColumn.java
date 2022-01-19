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

    public FlworDataFrameColumn(Name variableName, ColumnFormat columnFormat) {
        this.variableName = variableName;
        this.columnFormat = columnFormat;
    }

    public FlworDataFrameColumn(String columnName, StructType inputSchema) {
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
            switch (columnName.substring(pos)) {
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
                    throw new OurBadException("Unrecognized column name: " + columnName);
            }
        }
    }

    public String toString() {
        return "`" + getColumnName() + "`";
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
