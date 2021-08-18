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

import org.apache.commons.codec.binary.Hex;
import org.apache.spark.ml.linalg.DenseVector;
import org.apache.spark.ml.linalg.SparseVector;
import org.apache.spark.ml.linalg.Vector;
import org.apache.spark.ml.linalg.VectorUDT;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.types.ArrayType;
import org.apache.spark.sql.types.DataType;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.DecimalType;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import org.joda.time.DateTime;
import org.rumbledb.api.Item;
import org.rumbledb.context.Name;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.ParsingException;
import org.rumbledb.exceptions.RumbleException;
import org.rumbledb.items.ItemFactory;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.ItemType;
import scala.collection.mutable.WrappedArray;
import sparksoniq.spark.SparkSessionManager;

import java.io.Serializable;
import java.io.StringReader;
import java.lang.reflect.Array;
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

    public static Item getItemFromString(String string, ExceptionMetadata metadata) {
        JsonReader object = new JsonReader(new StringReader(string));
        return ItemParser.getItemFromObject(object, metadata);
    }

    public static Item getItemFromObject(JsonReader object, ExceptionMetadata metadata) {
        try {
            if (object.peek() == JsonToken.STRING) {
                return ItemFactory.getInstance().createStringItem(object.nextString());
            }
            if (object.peek() == JsonToken.NUMBER) {
                String number = object.nextString();
                if (number.contains("E") || number.contains("e")) {
                    return ItemFactory.getInstance().createDoubleItem(Double.parseDouble(number));
                }
                if (number.contains(".")) {
                    return ItemFactory.getInstance().createDecimalItem(new BigDecimal(number));
                }
                return ItemFactory.getInstance().createIntegerItem(number);
            }
            if (object.peek() == JsonToken.BOOLEAN) {
                return ItemFactory.getInstance().createBooleanItem(object.nextBoolean());
            }
            if (object.peek() == JsonToken.BEGIN_ARRAY) {
                List<Item> values = new ArrayList<>();
                object.beginArray();
                while (object.hasNext()) {
                    values.add(getItemFromObject(object, metadata));
                }
                object.endArray();
                return ItemFactory.getInstance().createArrayItem(values);
            }
            if (object.peek() == JsonToken.BEGIN_OBJECT) {
                List<String> keys = new ArrayList<>();
                List<Item> values = new ArrayList<>();
                object.beginObject();
                while (object.hasNext()) {
                    keys.add(object.nextName());
                    values.add(getItemFromObject(object, metadata));
                }
                object.endObject();
                return ItemFactory.getInstance()
                    .createObjectItem(keys, values, metadata);
            }
            if (object.peek() == JsonToken.NULL) {
                object.nextNull();
                return ItemFactory.getInstance().createNullItem();
            }
            throw new ParsingException("Invalid value found while parsing. JSON is not well-formed!", metadata);
        } catch (Exception e) {
            RumbleException r = new ParsingException(
                    "An error happened while parsing JSON. JSON is not well-formed! Hint: if you use json-file(), it must be in the JSON Lines format, with one value per line. If this is not the case, consider using json-doc().",
                    metadata
            );
            r.initCause(e);
            throw r;
        }
    }

    public static Item getItemFromRow(Row row, ExceptionMetadata metadata, ItemType itemType) {
        List<String> keys = new ArrayList<>();
        List<Item> values = new ArrayList<>();
        StructType schema = row.schema();
        StructField[] fields = schema.fields();
        String[] fieldnames = schema.fieldNames();

        for (int i = 0; i < fields.length; ++i) {
            StructField field = fields[i];
            DataType fieldType = field.dataType();
            String fieldName = field.name();
            ItemType fieldItemType = itemType == null
                ? null
                : itemType.getObjectContentFacet().get(fieldName).getType();
            Item newItem = convertValueToItem(row, i, null, fieldType, metadata, fieldItemType);
            // NULL values in DataFrames are mapped to absent in JSONiq.
            if (
                !newItem.isNull()
                    || (!fieldName.equals(SparkSessionManager.emptyObjectJSONiqItemColumnName)
                        && fieldType.equals(DataTypes.NullType))
            ) {
                keys.add(fieldName);
                values.add(newItem);
            }
        }

        if (fields.length == 1 && fieldnames[0].equals(SparkSessionManager.atomicJSONiqItemColumnName)) {
            return values.get(0);
        }
        System.err.println("And returning.");
        return ItemFactory.getInstance().createObjectItem(keys, values, metadata);
    }

    public static Item convertValueToItem(
            Object o,
            DataType fieldType,
            ExceptionMetadata metadata,
            ItemType itemType
    ) {
        return convertValueToItem(null, 0, o, fieldType, metadata, itemType);
    }

    private static Item convertValueToItem(
            Row row,
            int i,
            Object o,
            DataType fieldType,
            ExceptionMetadata metadata,
            ItemType itemType
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
            Item item = ItemFactory.getInstance().createStringItem(s);
            if (itemType == null || itemType.equals(BuiltinTypesCatalogue.stringItem)) {
                return item;
            } else {
                return ItemFactory.getInstance().createAnnotatedItem(item, itemType);
            }
        } else if (fieldType.equals(DataTypes.BooleanType)) {
            boolean b;
            if (row != null) {
                b = row.getBoolean(i);
            } else {
                b = (Boolean) o;
            }
            Item item = ItemFactory.getInstance().createBooleanItem(b);
            if (itemType == null || itemType.equals(BuiltinTypesCatalogue.booleanItem)) {
                return item;
            } else {
                return ItemFactory.getInstance().createAnnotatedItem(item, itemType);
            }
        } else if (fieldType.equals(DataTypes.DoubleType)) {
            double value;
            if (row != null) {
                value = row.getDouble(i);
            } else {
                value = (Double) o;
            }
            Item item = ItemFactory.getInstance().createDoubleItem(value);
            if (itemType == null || itemType.equals(BuiltinTypesCatalogue.doubleItem)) {
                return item;
            } else {
                return ItemFactory.getInstance().createAnnotatedItem(item, itemType);
            }
        } else if (fieldType.equals(DataTypes.IntegerType)) {
            int value;
            if (row != null) {
                value = row.getInt(i);
            } else {
                value = (Integer) o;
            }
            Item item = ItemFactory.getInstance().createIntItem(value);
            if (itemType == null || itemType.equals(BuiltinTypesCatalogue.intItem)) {
                return item;
            } else {
                return ItemFactory.getInstance().createAnnotatedItem(item, itemType);
            }
        } else if (fieldType.equals(DataTypes.FloatType)) {
            float value;
            if (row != null) {
                value = row.getFloat(i);
            } else {
                value = (Float) o;
            }
            Item item = ItemFactory.getInstance().createFloatItem(value);
            if (itemType == null || itemType.equals(BuiltinTypesCatalogue.floatItem)) {
                return item;
            } else {
                return ItemFactory.getInstance().createAnnotatedItem(item, itemType);
            }
        } else if (fieldType.equals(decimalType)) {
            BigDecimal value;
            if (row != null) {
                value = row.getDecimal(i);
            } else {
                value = (BigDecimal) o;
            }
            Item item = ItemFactory.getInstance().createDecimalItem(value);
            if (itemType == null || itemType.equals(BuiltinTypesCatalogue.decimalItem)) {
                return item;
            } else {
                return ItemFactory.getInstance().createAnnotatedItem(item, itemType);
            }
        } else if (fieldType.equals(DataTypes.LongType)) {
            BigDecimal value;
            if (row != null) {
                value = new BigDecimal(row.getLong(i));
            } else {
                value = new BigDecimal((Long) o);
            }
            Item item = ItemFactory.getInstance().createDecimalItem(value);
            if (itemType == null || itemType.equals(BuiltinTypesCatalogue.longItem)) {
                return item;
            } else {
                return ItemFactory.getInstance().createAnnotatedItem(item, itemType);
            }
        } else if (fieldType.equals(DataTypes.NullType)) {
            return ItemFactory.getInstance().createNullItem();
        } else if (fieldType.equals(DataTypes.ShortType)) {
            short value;
            if (row != null) {
                value = row.getShort(i);
            } else {
                value = (Short) o;
            }
            Item item = ItemFactory.getInstance().createIntItem(value);
            return ItemFactory.getInstance().createAnnotatedItem(item, itemType);
        } else if (fieldType.equals(DataTypes.TimestampType)) {
            Timestamp value;
            if (row != null) {
                value = row.getTimestamp(i);
            } else {
                value = (Timestamp) o;
            }
            Instant instant = value.toInstant();
            DateTime dt = new DateTime(instant);
            Item item = ItemFactory.getInstance().createDateTimeItem(dt, false);
            if (itemType == null || itemType.equals(BuiltinTypesCatalogue.dateTimeStampItem)) {
                return item;
            } else {
                return ItemFactory.getInstance().createAnnotatedItem(item, itemType);
            }
        } else if (fieldType.equals(DataTypes.DateType)) {
            Date value;
            if (row != null) {
                value = row.getDate(i);
            } else {
                value = (Date) o;
            }
            long instant = value.getTime();
            DateTime dt = new DateTime(instant);
            Item item = ItemFactory.getInstance().createDateItem(dt, false);
            if (itemType == null || itemType.equals(BuiltinTypesCatalogue.dateItem)) {
                return item;
            } else {
                return ItemFactory.getInstance().createAnnotatedItem(item, itemType);
            }
        } else if (fieldType.equals(DataTypes.BinaryType)) {
            byte[] value;
            if (row != null) {
                value = (byte[]) row.get(i);
            } else {
                value = (byte[]) o;
            }
            Item item = ItemFactory.getInstance().createHexBinaryItem(Hex.encodeHexString(value));
            if (itemType == null || itemType.equals(BuiltinTypesCatalogue.hexBinaryItem)) {
                return item;
            } else {
                return ItemFactory.getInstance().createAnnotatedItem(item, itemType);
            }
        } else if (fieldType instanceof StructType) {
            Row value;
            if (row != null) {
                value = row.getStruct(i);
            } else {
                value = (Row) o;
            }
            Item item = getItemFromRow(value, metadata, itemType);
            if (itemType == null || itemType.equals(BuiltinTypesCatalogue.objectItem)) {
                return item;
            } else {
                return ItemFactory.getInstance().createAnnotatedItem(item, itemType);
            }
        } else if (fieldType instanceof ArrayType) {
            ArrayType arrayType = (ArrayType) fieldType;
            DataType dataType = arrayType.elementType();
            ItemType memberType = itemType == null ? null : itemType.getArrayContentFacet();
            List<Item> members = new ArrayList<>();
            if (row != null) {
                List<Object> objects = row.getList(i);
                for (Object object : objects) {
                    members.add(convertValueToItem(object, dataType, metadata, memberType));
                }
            } else {
                @SuppressWarnings("unchecked")
                Object arrayObject = ((WrappedArray<Object>) o).array();
                for (int index = 0; index < Array.getLength(arrayObject); index++) {
                    Object value = Array.get(arrayObject, index);
                    members.add(convertValueToItem(value, dataType, metadata, memberType));
                }
            }
            Item item = ItemFactory.getInstance().createArrayItem(members);
            if (itemType == null || itemType.equals(BuiltinTypesCatalogue.arrayItem)) {
                return item;
            } else {
                return ItemFactory.getInstance().createAnnotatedItem(item, itemType);
            }
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
                Item item = ItemFactory.getInstance().createArrayItem(members);
                if (itemType == null || itemType.equals(BuiltinTypesCatalogue.arrayItem)) {
                    return item;
                } else {
                    return ItemFactory.getInstance().createAnnotatedItem(item, itemType);
                }
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
                Item item = ItemFactory.getInstance().createObjectItem(objectKeyList, objectValueList, metadata);
                if (itemType == null || itemType.equals(BuiltinTypesCatalogue.objectItem)) {
                    return item;
                } else {
                    return ItemFactory.getInstance().createAnnotatedItem(item, itemType);
                }
            } else {
                throw new OurBadException("Unexpected program state reached while converting vectorUDT to rumble item");
            }
        } else {
            throw new RuntimeException("DataFrame type unsupported: " + fieldType.json());
        }
    }

    public static DataType getDataFrameDataTypeFromItemType(ItemType itemType) {
        if (itemType.isSubtypeOf(BuiltinTypesCatalogue.booleanItem)) {
            return DataTypes.BooleanType;
        }
        if (itemType.isSubtypeOf(BuiltinTypesCatalogue.doubleItem)) {
            return DataTypes.DoubleType;
        }
        if (itemType.isSubtypeOf(BuiltinTypesCatalogue.floatItem)) {
            return DataTypes.FloatType;
        }
        if (itemType.isSubtypeOf(BuiltinTypesCatalogue.intItem)) {
            return DataTypes.IntegerType;
        }
        if (itemType.isSubtypeOf(BuiltinTypesCatalogue.longItem)) {
            return DataTypes.LongType;
        }
        if (itemType.isSubtypeOf(BuiltinTypesCatalogue.decimalItem)) {
            return decimalType;
        }
        if (itemType.isSubtypeOf(BuiltinTypesCatalogue.stringItem)) {
            return DataTypes.StringType;
        }
        if (itemType.isSubtypeOf(BuiltinTypesCatalogue.nullItem)) {
            return DataTypes.NullType;
        }
        if (itemType.isSubtypeOf(BuiltinTypesCatalogue.dateItem)) {
            return DataTypes.DateType;
        }
        if (itemType.isSubtypeOf(BuiltinTypesCatalogue.dateTimeItem)) {
            return DataTypes.TimestampType;
        }
        if (itemType.isSubtypeOf(BuiltinTypesCatalogue.hexBinaryItem)) {
            return DataTypes.BinaryType;
        }
        if (itemType.isSubtypeOf(BuiltinTypesCatalogue.objectItem)) {
            return vectorType;
        }
        throw new IllegalArgumentException(
                "Unexpected item type found: '" + itemType + "' in namespace " + itemType.getName().getNamespace() + "."
        );
    }

    public static Name getItemTypeNameFromDataFrameDataType(DataType dataType) {
        if (DataTypes.BooleanType.equals(dataType)) {
            return BuiltinTypesCatalogue.booleanItem.getName();
        }
        if (DataTypes.IntegerType.equals(dataType) || DataTypes.ShortType.equals(dataType)) {
            return BuiltinTypesCatalogue.integerItem.getName();
        }
        if (DataTypes.DoubleType.equals(dataType)) {
            return BuiltinTypesCatalogue.doubleItem.getName();
        }
        if (DataTypes.FloatType.equals(dataType)) {
            return BuiltinTypesCatalogue.floatItem.getName();
        }
        if (dataType.equals(decimalType) || DataTypes.LongType.equals(dataType)) {
            return BuiltinTypesCatalogue.decimalItem.getName();
        }
        if (DataTypes.StringType.equals(dataType)) {
            return BuiltinTypesCatalogue.stringItem.getName();
        }
        if (DataTypes.NullType.equals(dataType)) {
            return BuiltinTypesCatalogue.nullItem.getName();
        }
        if (DataTypes.DateType.equals(dataType)) {
            return BuiltinTypesCatalogue.dateItem.getName();
        }
        if (DataTypes.TimestampType.equals(dataType)) {
            return BuiltinTypesCatalogue.dateTimeItem.getName();
        }
        if (DataTypes.BinaryType.equals(dataType)) {
            return BuiltinTypesCatalogue.hexBinaryItem.getName();
        }
        if (vectorType.equals(dataType)) {
            return BuiltinTypesCatalogue.objectItem.getName();
        }
        throw new OurBadException("Unexpected DataFrame data type found: '" + dataType.toString() + "'.");
    }
}
