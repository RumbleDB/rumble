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
 * Authors: Stefan Irimescu, Can Berker Cikis
 *
 */

package org.rumbledb.items.parsing;

import com.jsoniter.JsonIterator;
import com.jsoniter.ValueType;
import org.apache.commons.codec.binary.Hex;
import org.apache.spark.ml.linalg.DenseVector;
import org.apache.spark.ml.linalg.SparseVector;
import org.apache.spark.ml.linalg.Vector;
import org.apache.spark.ml.linalg.VectorUDT;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.types.ArrayType;
import org.apache.spark.sql.types.DataType;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.DecimalType;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import org.joda.time.DateTime;
import org.rumbledb.api.Item;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.MLInvalidDataFrameSchemaException;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.SparksoniqRuntimeException;
import org.rumbledb.items.ItemFactory;
import sparksoniq.spark.SparkSessionManager;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class ItemParser implements Serializable {


    private static final long serialVersionUID = 1L;
    private static final DataType vectorType = new VectorUDT();
    public static final DataType decimalType = new DecimalType(30, 15); // 30 and 15 are arbitrary

    public static Item getItemFromObject(JsonIterator object, ExceptionMetadata metadata) {
        try {
            if (object.whatIsNext().equals(ValueType.STRING)) {
                return ItemFactory.getInstance().createStringItem(object.readString());
            }
            if (object.whatIsNext().equals(ValueType.NUMBER)) {
                String number = object.readNumberAsString();
                if (number.contains("E") || number.contains("e")) {
                    return ItemFactory.getInstance().createDoubleItem(Double.parseDouble(number));
                }
                if (number.contains(".") || number.length() >= 12) {
                    return ItemFactory.getInstance().createDecimalItem(new BigDecimal(number));
                }
                try {
                    return ItemFactory.getInstance().createIntegerItem(Integer.parseInt(number));
                } catch (NumberFormatException e) {
                    return ItemFactory.getInstance().createDecimalItem(new BigDecimal(number));
                }
            }
            if (object.whatIsNext().equals(ValueType.BOOLEAN)) {
                return ItemFactory.getInstance().createBooleanItem(object.readBoolean());
            }
            if (object.whatIsNext().equals(ValueType.ARRAY)) {
                List<Item> values = new ArrayList<>();
                while (object.readArray()) {
                    values.add(getItemFromObject(object, metadata));
                }
                return ItemFactory.getInstance().createArrayItem(values);
            }
            if (object.whatIsNext().equals(ValueType.OBJECT)) {
                List<String> keys = new ArrayList<>();
                List<Item> values = new ArrayList<>();
                String s;
                while ((s = object.readObject()) != null) {
                    keys.add(s);
                    values.add(getItemFromObject(object, metadata));
                }
                return ItemFactory.getInstance()
                    .createObjectItem(keys, values, metadata);
            }
            if (object.whatIsNext().equals(ValueType.NULL)) {
                object.readNull();
                return ItemFactory.getInstance().createNullItem();
            }
            throw new SparksoniqRuntimeException("Invalid value found while parsing. JSON is not well-formed!");
        } catch (IOException e) {
            throw new SparksoniqRuntimeException("IO error while parsing. JSON is not well-formed!");
        }
    }

    public static Item getItemFromRow(Row row, ExceptionMetadata metadata) {
        List<String> keys = new ArrayList<>();
        List<Item> values = new ArrayList<>();
        StructType schema = row.schema();
        StructField[] fields = schema.fields();
        String[] fieldnames = schema.fieldNames();

        for (int i = 0; i < fields.length; ++i) {
            StructField field = fields[i];
            DataType fieldType = field.dataType();
            keys.add(field.name());
            addValue(row, i, null, fieldType, values, metadata);
        }

        if (fields.length == 1 && fieldnames[0].equals(SparkSessionManager.atomicJSONiqItemColumnName)) {
            return values.get(0);
        }
        return ItemFactory.getInstance().createObjectItem(keys, values, metadata);
    }

    private static void addValue(
            Row row,
            int i,
            Object o,
            DataType fieldType,
            List<Item> values,
            ExceptionMetadata metadata
    ) {
        if (row != null && row.isNullAt(i)) {
            values.add(ItemFactory.getInstance().createNullItem());
        } else if (fieldType.equals(DataTypes.StringType)) {
            String s;
            if (row != null) {
                s = row.getString(i);
            } else {
                s = (String) o;
            }
            values.add(ItemFactory.getInstance().createStringItem(s));
        } else if (fieldType.equals(DataTypes.BooleanType)) {
            boolean b;
            if (row != null) {
                b = row.getBoolean(i);
            } else {
                b = (Boolean) o;
            }
            values.add(ItemFactory.getInstance().createBooleanItem(b));
        } else if (fieldType.equals(DataTypes.DoubleType)) {
            double value;
            if (row != null) {
                value = row.getDouble(i);
            } else {
                value = (Double) o;
            }
            values.add(ItemFactory.getInstance().createDoubleItem(value));
        } else if (fieldType.equals(DataTypes.IntegerType)) {
            int value;
            if (row != null) {
                value = row.getInt(i);
            } else {
                value = (Integer) o;
            }
            values.add(ItemFactory.getInstance().createIntegerItem(value));
        } else if (fieldType.equals(DataTypes.FloatType)) {
            float value;
            if (row != null) {
                value = row.getFloat(i);
            } else {
                value = (Float) o;
            }
            values.add(ItemFactory.getInstance().createDoubleItem(value));
        } else if (fieldType.equals(decimalType)) {
            BigDecimal value;
            if (row != null) {
                value = row.getDecimal(i);
            } else {
                value = (BigDecimal) o;
            }
            values.add(ItemFactory.getInstance().createDecimalItem(value));
        } else if (fieldType.equals(DataTypes.LongType)) {
            BigDecimal value;
            if (row != null) {
                value = new BigDecimal(row.getLong(i));
            } else {
                value = new BigDecimal((Long) o);
            }
            values.add(ItemFactory.getInstance().createDecimalItem(value));
        } else if (fieldType.equals(DataTypes.NullType)) {
            values.add(ItemFactory.getInstance().createNullItem());
        } else if (fieldType.equals(DataTypes.ShortType)) {
            short value;
            if (row != null) {
                value = row.getShort(i);
            } else {
                value = (Short) o;
            }
            values.add(ItemFactory.getInstance().createIntegerItem(value));
        } else if (fieldType.equals(DataTypes.TimestampType)) {
            Timestamp value;
            if (row != null) {
                value = row.getTimestamp(i);
            } else {
                value = (Timestamp) o;
            }
            Instant instant = value.toInstant();
            DateTime dt = new DateTime(instant);
            values.add(ItemFactory.getInstance().createDateTimeItem(dt, false));
        } else if (fieldType.equals(DataTypes.DateType)) {
            Date value;
            if (row != null) {
                value = row.getDate(i);
            } else {
                value = (Date) o;
            }
            Instant instant = value.toInstant();
            DateTime dt = new DateTime(instant);
            values.add(ItemFactory.getInstance().createDateItem(dt, false));
        } else if (fieldType.equals(DataTypes.BinaryType)) {
            byte[] value;
            if (row != null) {
                value = (byte[]) row.get(i);
            } else {
                value = (byte[]) o;
            }
            values.add(ItemFactory.getInstance().createHexBinaryItem(Hex.encodeHexString(value)));
        } else if (fieldType instanceof StructType) {
            Row value;
            if (row != null) {
                value = row.getStruct(i);
            } else {
                value = (Row) o;
            }
            values.add(getItemFromRow(value, metadata));
        } else if (fieldType instanceof ArrayType) {
            ArrayType arrayType = (ArrayType) fieldType;
            DataType dataType = arrayType.elementType();
            List<Item> members = new ArrayList<>();
            List<Object> objects;
            if (row != null) {
                objects = row.getList(i);
            } else {
                objects = (List<Object>) o;
            }
            for (Object object : objects) {
                addValue(null, 0, object, dataType, members, metadata);
            }
            values.add(ItemFactory.getInstance().createArrayItem(members));
        } else if (fieldType instanceof VectorUDT) {
            Vector vector = (Vector) row.get(i);
            if (vector instanceof DenseVector) {
                // a dense vector is mapped to a rumble array
                DenseVector denseVector = (DenseVector) vector;
                List<Item> members = new ArrayList<>(vector.size());
                for (double value : denseVector.values()) {
                    members.add(ItemFactory.getInstance().createDoubleItem(value));
                }
                values.add(ItemFactory.getInstance().createArrayItem(members));
            } else if (vector instanceof SparseVector) {
                // a sparse vector is mapped to a Rumble object where keys are indices of the non-0 values in the vector
                SparseVector sparseVector = (SparseVector) vector;
                List<String> objectKeyList = new ArrayList<>();
                List<Item> objectValueList = new ArrayList<>();
                int[] vectorIndices = sparseVector.indices();
                double[] vectorValues = sparseVector.values();
                for (int j = 0; j < vectorIndices.length; j++) {
                    objectKeyList.add(String.valueOf(vectorIndices[j]));
                    objectValueList.add(ItemFactory.getInstance().createDoubleItem(vectorValues[j]));
                }
                values.add(ItemFactory.getInstance().createObjectItem(objectKeyList, objectValueList, metadata));
            } else {
                throw new OurBadException("Unexpected program state reached while converting vectorUDT to rumble item");
            }
        } else {
            throw new RuntimeException("DataFrame type unsupported: " + fieldType.json());
        }
    }

    public static DataType getDataFrameDataTypeFromItemTypeName(String itemTypeName, boolean forSparkML) {
        switch (itemTypeName) {
            case "boolean":
                return DataTypes.BooleanType;
            case "integer":
                return DataTypes.IntegerType;
            case "double":
                return DataTypes.DoubleType;
            case "decimal":
                if (forSparkML) {
                    return DataTypes.DoubleType;
                }
                return decimalType;
            case "string":
                return DataTypes.StringType;
            case "null":
                return DataTypes.NullType;
            case "date":
                return DataTypes.DateType;
            case "datetime":
                return DataTypes.TimestampType;
            case "hexbinary":
                return DataTypes.BinaryType;
            case "object":
                return vectorType;
            default:
                throw new IllegalArgumentException("Unexpected item type found: '" + itemTypeName + "'.");
        }
    }

    public static String getItemTypeNameFromDataFrameDataType(DataType dataType) {
        if (DataTypes.BooleanType.equals(dataType)) {
            return "boolean";
        } else if (DataTypes.IntegerType.equals(dataType) || DataTypes.ShortType.equals(dataType)) {
            return "integer";
        } else if (DataTypes.DoubleType.equals(dataType) || DataTypes.FloatType.equals(dataType)) {
            return "double";
        } else if (dataType.equals(decimalType) || DataTypes.LongType.equals(dataType)) {
            return "decimal";
        } else if (DataTypes.StringType.equals(dataType)) {
            return "string";
        } else if (DataTypes.NullType.equals(dataType)) {
            return "null";
        } else if (DataTypes.DateType.equals(dataType)) {
            return "date";
        } else if (DataTypes.TimestampType.equals(dataType)) {
            return "datetime";
        } else if (DataTypes.BinaryType.equals(dataType)) {
            return "hexbinary";
        } else if (vectorType.equals(dataType)) {
            return "object";
        }
        throw new OurBadException("Unexpected DataFrame data type found: '" + dataType.toString() + "'.");
    }

    public static List<Row> getRowsFromItemsUsingSchema(List<Item> items, StructType schema, boolean forSparkML) {
        List<Row> rows = new ArrayList<>();
        for (Item item : items) {
            Row row = getRowFromItemUsingSchema(item, schema, forSparkML);
            rows.add(row);
        }
        return rows;
    }

    public static Row getRowFromItemUsingSchema(Item item, StructType schema, boolean forSparkML) {
        Object[] rowColumns = new Object[schema.fields().length];
        for (int fieldIndex = 0; fieldIndex < schema.fields().length; fieldIndex++) {
            StructField field = schema.fields()[fieldIndex];
            String fieldName = field.name();
            DataType fieldDataType = field.dataType();
            Item columnValue = item.getItemByKey(fieldName);
            if (columnValue == null) {
                throw new MLInvalidDataFrameSchemaException(
                        "Missing field '" + fieldName + "' in object '" + item.serialize() + "'."
                );
            }
            try {
                if (fieldDataType.equals(DataTypes.BooleanType)) {
                    rowColumns[fieldIndex] = columnValue.getBooleanValue();
                } else if (fieldDataType.equals(DataTypes.IntegerType)) { // TODO: shall we be more lenient and cast
                                                                          // numerics to one another?
                    rowColumns[fieldIndex] = columnValue.getIntegerValue();
                } else if (fieldDataType.equals(DataTypes.DoubleType)) {
                    if (forSparkML && columnValue.isDecimal()) {
                        rowColumns[fieldIndex] = columnValue.getDecimalValue().doubleValue();
                    } else {
                        rowColumns[fieldIndex] = columnValue.getDoubleValue();
                    }
                } else if (fieldDataType.equals(decimalType)) {
                    rowColumns[fieldIndex] = columnValue.getDecimalValue();
                } else if (fieldDataType.equals(DataTypes.StringType)) {
                    rowColumns[fieldIndex] = columnValue.getStringValue();
                } else if (fieldDataType.equals(DataTypes.NullType)) {
                    if (!columnValue.isNull()) {
                        throw new RuntimeException("Item '" + columnValue.serialize() + " is not null");
                    }
                    rowColumns[fieldIndex] = null;
                } else if (fieldDataType.equals(DataTypes.DateType)) {
                    rowColumns[fieldIndex] = new Date(columnValue.getDateTimeValue().getMillis());
                } else if (fieldDataType.equals(DataTypes.TimestampType)) {
                    rowColumns[fieldIndex] = new Timestamp(columnValue.getDateTimeValue().getMillis());
                } else if (fieldDataType instanceof ArrayType) {
                    // TODO: read the contents of array and generate rows with it
                    throw new OurBadException("Array item to row conversion is not yet supported.");
                } else if (fieldDataType instanceof StructType) {
                    throw new OurBadException("Object item to row conversion is not yet supported.");
                } else {
                    throw new IllegalArgumentException(
                            "Unexpected item type found for field: '" + fieldName + "' while generating rows."
                    );
                }
            } catch (RuntimeException ex) {
                throw new MLInvalidDataFrameSchemaException(
                        "Schema does not match the data of object: '" + item.serialize() + "'; " + ex.getMessage()
                );
            }
        }

        return RowFactory.create(rowColumns);
    }
}
