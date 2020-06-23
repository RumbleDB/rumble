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
import org.rumbledb.exceptions.ParsingException;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.types.ItemType;
import scala.collection.mutable.WrappedArray;
import sparksoniq.spark.SparkSessionManager;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashMap;
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
                LinkedHashMap<String, Item> content = new LinkedHashMap<>();
                String s;
                while ((s = object.readObject()) != null) {
                    content.put(s, getItemFromObject(object, metadata));
                }
                return ItemFactory.getInstance()
                    .createObjectItem(content);
            }
            if (object.whatIsNext().equals(ValueType.NULL)) {
                object.readNull();
                return ItemFactory.getInstance().createNullItem();
            }
            throw new ParsingException("Invalid value found while parsing. JSON is not well-formed!", metadata);
        } catch (Exception e) {
            throw new ParsingException(
                    "An error happened while parsing JSON. JSON is not well-formed! Hint: if you use json-file(), it must be in the JSON Lines format, with one value per line. If this is not the case, consider using json-doc().",
                    metadata
            );
        }
    }

    public static Item getItemFromRow(Row row, ExceptionMetadata metadata) {
        StructType schema = row.schema();
        StructField[] fields = schema.fields();
        LinkedHashMap<String, Item> content = new LinkedHashMap<>(fields.length, 1);
        String[] fieldnames = schema.fieldNames();

        for (int i = 0; i < fields.length; ++i) {
            StructField field = fields[i];
            DataType fieldType = field.dataType();
            content.put(field.name(), getValue(row, i, null, fieldType, metadata));
        }

        if (fields.length == 1 && fieldnames[0].equals(SparkSessionManager.atomicJSONiqItemColumnName)) {
            return content.get(fieldnames[0]);
        }
        return ItemFactory.getInstance().createObjectItem(content);
    }

    private static Item getValue(
            Row row,
            int i,
            Object o,
            DataType fieldType,
            ExceptionMetadata metadata
    ) {
        if (row != null && row.isNullAt(i)) {
            return ItemFactory.getInstance().createNullItem();
        } else if (fieldType.equals(DataTypes.StringType)) {
            String s;
            if (row != null) {
                s = row.getString(i);
            } else {
                s = (String) o;
            }
            return ItemFactory.getInstance().createStringItem(s);
        } else if (fieldType.equals(DataTypes.BooleanType)) {
            boolean b;
            if (row != null) {
                b = row.getBoolean(i);
            } else {
                b = (Boolean) o;
            }
            return ItemFactory.getInstance().createBooleanItem(b);
        } else if (fieldType.equals(DataTypes.DoubleType)) {
            double value;
            if (row != null) {
                value = row.getDouble(i);
            } else {
                value = (Double) o;
            }
            return ItemFactory.getInstance().createDoubleItem(value);
        } else if (fieldType.equals(DataTypes.IntegerType)) {
            int value;
            if (row != null) {
                value = row.getInt(i);
            } else {
                value = (Integer) o;
            }
            return ItemFactory.getInstance().createIntegerItem(value);
        } else if (fieldType.equals(DataTypes.FloatType)) {
            float value;
            if (row != null) {
                value = row.getFloat(i);
            } else {
                value = (Float) o;
            }
            return ItemFactory.getInstance().createDoubleItem(value);
        } else if (fieldType.equals(decimalType)) {
            BigDecimal value;
            if (row != null) {
                value = row.getDecimal(i);
            } else {
                value = (BigDecimal) o;
            }
            return ItemFactory.getInstance().createDecimalItem(value);
        } else if (fieldType.equals(DataTypes.LongType)) {
            BigDecimal value;
            if (row != null) {
                value = new BigDecimal(row.getLong(i));
            } else {
                value = new BigDecimal((Long) o);
            }
            return ItemFactory.getInstance().createDecimalItem(value);
        } else if (fieldType.equals(DataTypes.NullType)) {
            return ItemFactory.getInstance().createNullItem();
        } else if (fieldType.equals(DataTypes.ShortType)) {
            short value;
            if (row != null) {
                value = row.getShort(i);
            } else {
                value = (Short) o;
            }
            return ItemFactory.getInstance().createIntegerItem(value);
        } else if (fieldType.equals(DataTypes.TimestampType)) {
            Timestamp value;
            if (row != null) {
                value = row.getTimestamp(i);
            } else {
                value = (Timestamp) o;
            }
            Instant instant = value.toInstant();
            DateTime dt = new DateTime(instant);
            return ItemFactory.getInstance().createDateTimeItem(dt, false);
        } else if (fieldType.equals(DataTypes.DateType)) {
            Date value;
            if (row != null) {
                value = row.getDate(i);
            } else {
                value = (Date) o;
            }
            Instant instant = value.toInstant();
            DateTime dt = new DateTime(instant);
            return ItemFactory.getInstance().createDateItem(dt, false);
        } else if (fieldType.equals(DataTypes.BinaryType)) {
            byte[] value;
            if (row != null) {
                value = (byte[]) row.get(i);
            } else {
                value = (byte[]) o;
            }
            return ItemFactory.getInstance().createHexBinaryItem(Hex.encodeHexString(value));
        } else if (fieldType instanceof StructType) {
            Row value;
            if (row != null) {
                value = row.getStruct(i);
            } else {
                value = (Row) o;
            }
            return getItemFromRow(value, metadata);
        } else if (fieldType instanceof ArrayType) {
            ArrayType arrayType = (ArrayType) fieldType;
            DataType dataType = arrayType.elementType();
            List<Item> members = null;
            if (row != null) {
                List<Object> objects = row.getList(i);
                members = new ArrayList<>(objects.size());
                for (Object object : objects) {
                    members.add(getValue(null, 0, object, dataType, metadata));
                }
            } else {
                @SuppressWarnings("unchecked")
                Object arrayObject = ((WrappedArray<Object>) o).array();
                members = new ArrayList<>(Array.getLength(arrayObject));
                for (int index = 0; index < Array.getLength(arrayObject); index++) {
                    Object value = Array.get(arrayObject, index);
                    members.add(getValue(null, 0, value, dataType, metadata));
                }
            }
            return ItemFactory.getInstance().createArrayItem(members);
        } else if (fieldType instanceof VectorUDT) {
            Vector vector;
            if (row != null) {
                vector = (Vector) row.get(i);
            } else {
                vector = (Vector) o;
            }
            if (vector instanceof DenseVector) {
                // a dense vector is mapped to a rumble array
                DenseVector denseVector = (DenseVector) vector;
                List<Item> members = new ArrayList<>(vector.size());
                for (double value : denseVector.values()) {
                    members.add(ItemFactory.getInstance().createDoubleItem(value));
                }
                return ItemFactory.getInstance().createArrayItem(members);
            } else if (vector instanceof SparseVector) {
                // a sparse vector is mapped to a Rumble object where keys are indices of the non-0 values in the vector
                SparseVector sparseVector = (SparseVector) vector;
                int[] vectorIndices = sparseVector.indices();
                LinkedHashMap<String, Item> objectContent = new LinkedHashMap<>(vectorIndices.length, 1);
                double[] vectorValues = sparseVector.values();
                for (int j = 0; j < vectorIndices.length; j++) {
                    objectContent.put(
                        String.valueOf(vectorIndices[j]),
                        ItemFactory.getInstance().createDoubleItem(vectorValues[j])
                    );
                }
                return ItemFactory.getInstance().createObjectItem(objectContent);
            } else {
                throw new OurBadException("Unexpected program state reached while converting vectorUDT to rumble item");
            }
        } else {
            throw new RuntimeException("DataFrame type unsupported: " + fieldType.json());
        }
    }

    public static DataType getDataFrameDataTypeFromItemTypeName(String itemTypeName) {
        if (itemTypeName.equals(ItemType.booleanItem.getName())) {
            return DataTypes.BooleanType;
        }
        if (itemTypeName.equals(ItemType.integerItem.getName())) {
            return DataTypes.IntegerType;
        }
        if (itemTypeName.equals(ItemType.doubleItem.getName())) {
            return DataTypes.DoubleType;
        }
        if (itemTypeName.equals(ItemType.decimalItem.getName())) {
            return decimalType;
        }
        if (itemTypeName.equals(ItemType.stringItem.getName())) {
            return DataTypes.StringType;
        }
        if (itemTypeName.equals(ItemType.nullItem.getName())) {
            return DataTypes.NullType;
        }
        if (itemTypeName.equals(ItemType.dateItem.getName())) {
            return DataTypes.DateType;
        }
        if (itemTypeName.equals(ItemType.dateTimeItem.getName())) {
            return DataTypes.TimestampType;
        }
        if (itemTypeName.equals(ItemType.hexBinaryItem.getName())) {
            return DataTypes.BinaryType;
        }
        if (itemTypeName.equals("object")) {
            return vectorType;
        }
        throw new IllegalArgumentException("Unexpected item type found: '" + itemTypeName + "'.");
    }

    public static String getItemTypeNameFromDataFrameDataType(DataType dataType) {
        if (DataTypes.BooleanType.equals(dataType)) {
            return ItemType.booleanItem.getName();
        }
        if (DataTypes.IntegerType.equals(dataType) || DataTypes.ShortType.equals(dataType)) {
            return ItemType.integerItem.getName();
        }
        if (DataTypes.DoubleType.equals(dataType) || DataTypes.FloatType.equals(dataType)) {
            return ItemType.doubleItem.getName();
        }
        if (dataType.equals(decimalType) || DataTypes.LongType.equals(dataType)) {
            return ItemType.decimalItem.getName();
        }
        if (DataTypes.StringType.equals(dataType)) {
            return ItemType.stringItem.getName();
        }
        if (DataTypes.NullType.equals(dataType)) {
            return ItemType.nullItem.getName();
        }
        if (DataTypes.DateType.equals(dataType)) {
            return ItemType.dateItem.getName();
        }
        if (DataTypes.TimestampType.equals(dataType)) {
            return ItemType.dateTimeItem.getName();
        }
        if (DataTypes.BinaryType.equals(dataType)) {
            return ItemType.hexBinaryItem.getName();
        }
        if (vectorType.equals(dataType)) {
            return "object";
        }
        throw new OurBadException("Unexpected DataFrame data type found: '" + dataType.toString() + "'.");
    }

    public static List<Row> getRowsFromItemsUsingSchema(List<Item> items, StructType schema) {
        List<Row> rows = new ArrayList<>();
        for (Item item : items) {
            Row row = getRowFromItemUsingSchema(item, schema);
            rows.add(row);
        }
        return rows;
    }

    public static Row getRowFromItemUsingSchema(Item item, StructType schema) {
        Object[] rowColumns = new Object[schema.fields().length];
        for (int fieldIndex = 0; fieldIndex < schema.fields().length; fieldIndex++) {
            StructField field = schema.fields()[fieldIndex];
            Object rowColumn = getRowColumnFromItemUsingSchemaField(item, field);
            rowColumns[fieldIndex] = rowColumn;
        }
        return RowFactory.create(rowColumns);
    }

    private static Object getRowColumnFromItemUsingSchemaField(Item item, StructField field) {
        String fieldName = field.name();
        DataType fieldDataType = field.dataType();
        Item columnValueItem = item.getItemByKey(fieldName);
        if (columnValueItem == null) {
            throw new MLInvalidDataFrameSchemaException(
                    "Missing field '" + fieldName + "' in object '" + item.serialize() + "'."
            );
        }
        try {
            return getRowColumnFromItemUsingDataType(columnValueItem, fieldDataType);
        } catch (MLInvalidDataFrameSchemaException ex) {
            throw new MLInvalidDataFrameSchemaException(
                    "Data does not fit to the given schema in field '"
                        + fieldName
                        + "'; "
                        + ex.getJSONiqErrorMessage()
            );
        }
    }

    private static Object getRowColumnFromItemUsingDataType(Item item, DataType dataType) {
        try {
            if (dataType instanceof ArrayType) {
                List<Item> arrayItems = item.getItems();
                Object[] arrayItemsForRow = new Object[arrayItems.size()];
                DataType elementType = ((ArrayType) dataType).elementType();
                for (int i = 0; i < arrayItems.size(); i++) {
                    Item arrayItem = item.getItemAt(i);
                    arrayItemsForRow[i] = getRowColumnFromItemUsingDataType(arrayItem, elementType);
                }
                return arrayItemsForRow;
            }

            if (dataType instanceof StructType) {
                return getRowFromItemUsingSchema(item, (StructType) dataType);
            }

            if (dataType.equals(DataTypes.BooleanType)) {
                return item.getBooleanValue();
            }
            if (dataType.equals(DataTypes.IntegerType)) {
                return item.castToIntegerValue();
            }
            if (dataType.equals(DataTypes.DoubleType)) {
                return item.castToDoubleValue();
            }
            if (dataType.equals(decimalType)) {
                return item.castToDecimalValue();
            }
            if (dataType.equals(DataTypes.StringType)) {
                return item.getStringValue();
            }
            if (dataType.equals(DataTypes.NullType)) {
                if (!item.isNull()) {
                    throw new OurBadException("Item '" + item.serialize() + " is not null");
                }
                return null;
            }
            if (dataType.equals(DataTypes.DateType)) {
                return new Date(item.getDateTimeValue().getMillis());
            }
            if (dataType.equals(DataTypes.TimestampType)) {
                return new Timestamp(item.getDateTimeValue().getMillis());
            }
        } catch (OurBadException ex) {
            // OurBadExceptions triggered by invalid use of value getters here are caused by user's schema
            throw new MLInvalidDataFrameSchemaException(ex.getJSONiqErrorMessage());
        }

        throw new OurBadException(
                "Unhandled item type found while generating rows: '" + dataType + "' ."
        );
    }
}
