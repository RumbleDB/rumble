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
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.ParsingException;
import org.rumbledb.exceptions.RumbleException;
import org.rumbledb.items.ItemFactory;

import com.fasterxml.jackson.dataformat.yaml.YAMLParser;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.FieldDescriptor;
import org.rumbledb.types.ItemType;
import scala.collection.immutable.ArraySeq;
import scala.collection.Iterator;

import sparksoniq.spark.SparkSessionManager;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ItemParser implements Serializable {


    private static final long serialVersionUID = 1L;

    /**
     * Parses a JSON string to an item.
     * 
     * @param string the JSON string.
     * @param metadata exception metadata is an error is thrown.
     * @return the parsed item.
     */
    public static Item getItemFromString(String string, ExceptionMetadata metadata) {
        string = "[ " + string + " ]";
        JsonReader object = new JsonReader(new StringReader(string));
        Item arrayItem = ItemParser.getItemFromObject(object, metadata);
        if (arrayItem.getSize() == 0) {
            throw new ParsingException("Empty string to parse as JSON!", metadata);
        }
        return arrayItem.getItemAt(0);
    }

    /**
     * Parses a JSON string, accessible via a reader, to an item.
     * 
     * @param object the JSON reader.
     * @param metadata exception metadata is an error is thrown.
     * @return the parsed item.
     */
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
                return ItemFactory.getInstance().createArrayItem(values, false);
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
                    .createObjectItem(keys, values, metadata, false);
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

    /**
     * Parses a JSON string, accessible via a reader, to an item.
     * 
     * @param parser the YAML parser.
     * @param lookahead the lookahead token.
     * @param metadata exception metadata is an error is thrown.
     * @return the parsed item.
     */
    public static Item getItemFromYAML(
            YAMLParser parser,
            com.fasterxml.jackson.core.JsonToken lookahead,
            ExceptionMetadata metadata
    ) {
        try {
            if (lookahead == null) {
                // System.err.println("End of file.");
                return null;
            }
            // System.err.println("Lookahead (top level): " + lookahead.toString());
            if (lookahead.equals(com.fasterxml.jackson.core.JsonToken.VALUE_STRING)) {
                return ItemFactory.getInstance().createStringItem(parser.getValueAsString());
            }
            if (lookahead.equals(com.fasterxml.jackson.core.JsonToken.VALUE_NUMBER_INT)) {
                String number = parser.getValueAsString();
                if (number.contains("E") || number.contains("e")) {
                    return ItemFactory.getInstance().createDoubleItem(Double.parseDouble(number));
                }
                if (number.contains(".")) {
                    return ItemFactory.getInstance().createDecimalItem(new BigDecimal(number));
                }
                return ItemFactory.getInstance().createIntegerItem(number);
            }
            if (lookahead.equals(com.fasterxml.jackson.core.JsonToken.VALUE_NUMBER_FLOAT)) {
                String number = parser.getValueAsString();
                if (number.contains("E") || number.contains("e")) {
                    return ItemFactory.getInstance().createDoubleItem(Double.parseDouble(number));
                }
                if (number.contains(".")) {
                    return ItemFactory.getInstance().createDecimalItem(new BigDecimal(number));
                }
                return ItemFactory.getInstance().createIntegerItem(number);
            }
            if (lookahead.equals(com.fasterxml.jackson.core.JsonToken.VALUE_FALSE)) {
                return ItemFactory.getInstance().createBooleanItem(false);
            }
            if (lookahead.equals(com.fasterxml.jackson.core.JsonToken.VALUE_TRUE)) {
                return ItemFactory.getInstance().createBooleanItem(true);
            }
            if (lookahead.equals(com.fasterxml.jackson.core.JsonToken.START_ARRAY)) {
                List<Item> values = new ArrayList<>();
                com.fasterxml.jackson.core.JsonToken nt = parser.nextToken();
                // System.err.println("Next token (reading array): " + nt.toString());
                while (!nt.equals(com.fasterxml.jackson.core.JsonToken.END_ARRAY)) {
                    values.add(getItemFromYAML(parser, nt, metadata));
                    nt = parser.nextToken();
                    // System.err.println("Next token (reading array): " + nt.toString());
                }
                // System.err.println("Finished reading array.");
                return ItemFactory.getInstance().createArrayItem(values, false);
            }
            if (lookahead.equals(com.fasterxml.jackson.core.JsonToken.START_OBJECT)) {
                List<String> keys = new ArrayList<>();
                List<Item> values = new ArrayList<>();
                com.fasterxml.jackson.core.JsonToken nt = parser.nextToken();
                // System.err.println("Next token (reading object): " + lookahead.toString());
                while (!nt.equals(com.fasterxml.jackson.core.JsonToken.END_OBJECT)) {
                    if (!nt.equals(com.fasterxml.jackson.core.JsonToken.FIELD_NAME)) {
                        throw new ParsingException("Expected field name!", metadata);
                    }
                    keys.add(parser.getText());
                    nt = parser.nextToken();
                    // System.err.println("Next token (reading object): " + nt.toString());
                    values.add(getItemFromYAML(parser, nt, metadata));
                    nt = parser.nextToken();
                    // System.err.println("Next token (reading object): " + nt.toString());
                }
                // System.err.println("Finished reading object.");
                return ItemFactory.getInstance()
                    .createObjectItem(keys, values, metadata, false);
            }
            if (lookahead.equals(com.fasterxml.jackson.core.JsonToken.VALUE_NULL)) {
                return ItemFactory.getInstance().createNullItem();
            }
            throw new ParsingException(
                    "Invalid value found while parsing. YAML is not well-formed! Unexpected " + lookahead.toString(),
                    metadata
            );
        } catch (IOException e) {
            RumbleException r = new ParsingException(
                    "An error happened while parsing YAML. YAML is not well-formed!",
                    metadata
            );
            r.initCause(e);
            throw r;
        }
    }

    /**
     * Converts a DataFrame row to an item.
     * 
     * @param row the DataFrame row.
     * @param metadata exception metadata is an error is thrown.
     * @param itemType the type to annotate the output item with (for now, it can be null for no annotation).
     * @return the converted item.
     */
    public static Item getItemFromRow(Row row, ExceptionMetadata metadata, ItemType itemType) {
        List<String> keys = new ArrayList<>();
        List<Item> values = new ArrayList<>();
        StructType schema = row.schema();
        StructField[] fields = schema.fields();
        String[] fieldnames = schema.fieldNames();

        if (fields.length == 1 && fieldnames[0].equals(SparkSessionManager.atomicJSONiqItemColumnName)) {
            return convertValueToItem(row, 0, null, fields[0].dataType(), metadata, itemType);
        }

        if (
            fields.length == 5
                && fieldnames[0].equals(SparkSessionManager.atomicJSONiqItemColumnName)
                && fieldnames[4].equals("tableLocation")
        ) {
            ItemType resType = null;
            if (itemType != null) {
                resType = itemType.getObjectContentFacet()
                    .get(SparkSessionManager.atomicJSONiqItemColumnName)
                    .getType();
            }
            Item res = convertValueToItem(row, 0, null, fields[0].dataType(), metadata, resType);
            // TODO: refactor to not need to loop and check strings -- Indexes perhaps?
            for (int i = 0; i < fields.length; ++i) {
                String fieldName = fields[i].name();

                if (fieldName.equals(SparkSessionManager.mutabilityLevelColumnName)) {
                    res.setMutabilityLevel(row.getInt(i));
                    continue;
                }
                if (fieldName.equals(SparkSessionManager.rowIdColumnName)) {
                    res.setTopLevelID(row.getLong(i));
                    continue;
                }
                if (fieldName.equals(SparkSessionManager.pathInColumnName)) {
                    res.setPathIn(row.getString(i));
                    continue;
                }
                if (fieldName.equals(SparkSessionManager.tableLocationColumnName)) {
                    res.setTableLocation(row.getString(i));
                }
            }
            return res;
        }

        Map<String, FieldDescriptor> content = null;

        if (itemType != null && !itemType.equals(BuiltinTypesCatalogue.item)) {
            content = itemType.getObjectContentFacet();
            if (content == null) {
                throw new OurBadException(
                        "Object descriptor content in type " + itemType.getIdentifierString() + " is null."
                );
            }
        }

        int mutabilityLevel = -1;
        long topLevelID = -1;
        String pathIn = "null";
        String tableLocation = "null";

        for (int i = 0; i < fields.length; ++i) {
            StructField field = fields[i];
            DataType fieldType = field.dataType();
            String fieldName = field.name();
            ItemType fieldItemType = null;

            if (fieldName.equals(SparkSessionManager.mutabilityLevelColumnName)) {
                mutabilityLevel = row.getInt(i);
                continue;
            }
            if (fieldName.equals(SparkSessionManager.rowIdColumnName)) {
                topLevelID = row.getLong(i);
                continue;
            }
            if (fieldName.equals(SparkSessionManager.pathInColumnName)) {
                pathIn = row.getString(i);
                continue;
            }
            if (fieldName.equals(SparkSessionManager.tableLocationColumnName)) {
                tableLocation = row.getString(i);
                continue;
            }

            if (content != null) {
                FieldDescriptor descriptor = content.get(fieldName);
                if (descriptor != null) {
                    fieldItemType = descriptor.getType();
                    if (fieldItemType == null) {
                        throw new OurBadException(
                                "Type for field "
                                    + fieldName
                                    + " in type "
                                    + itemType.getIdentifierString()
                                    + " is null."
                        );
                    }
                }
            }
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

        Item res = ItemFactory.getInstance().createObjectItem(keys, values, metadata, false);
        res.setMutabilityLevel(mutabilityLevel);
        res.setTopLevelID(topLevelID);
        res.setPathIn(pathIn);
        res.setTableLocation(tableLocation);

        return res;
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
        if (itemType != null && itemType.getName() == null) {
            itemType = itemType.getBaseType();
        }
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
        } else if (fieldType instanceof DecimalType && ((DecimalType) fieldType).scale() == 0) {
            BigDecimal value;
            if (row != null) {
                value = row.getDecimal(i);
            } else {
                value = (BigDecimal) o;
            }
            BigInteger integerValue = value.toBigIntegerExact();
            Item item = ItemFactory.getInstance().createIntegerItem(integerValue);
            if (itemType == null || itemType.equals(BuiltinTypesCatalogue.integerItem)) {
                return item;
            } else {
                return ItemFactory.getInstance().createAnnotatedItem(item, itemType);
            }
        } else if (fieldType instanceof DecimalType) {
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
            long value;
            if (row != null) {
                value = row.getLong(i);
            } else {
                value = ((Long) o).longValue();
            }
            Item item = ItemFactory.getInstance().createLongItem(value);
            if (itemType == null || itemType.equals(BuiltinTypesCatalogue.longItem)) {
                return item;
            } else {
                return ItemFactory.getInstance().createAnnotatedItem(item, itemType);
            }
        } else if (fieldType.equals(DataTypes.NullType)) {
            return ItemFactory.getInstance().createNullItem();
        } else if (fieldType.equals(DataTypes.ByteType)) {
            byte value;
            if (row != null) {
                value = row.getByte(i);
            } else {
                value = (Byte) o;
            }
            Item item = ItemFactory.getInstance().createIntItem(value);
            return ItemFactory.getInstance().createAnnotatedItem(item, itemType);
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
            ItemType memberType = null;
            if (itemType != null && !itemType.equals(BuiltinTypesCatalogue.item)) {
                memberType = itemType.getArrayContentFacet();
            }
            List<Item> members = new ArrayList<>();
            if (row != null) {
                List<Object> objects = row.getList(i);
                for (Object object : objects) {
                    members.add(convertValueToItem(object, dataType, metadata, memberType));
                }
            } else {
                @SuppressWarnings("unchecked")
                Iterator<Object> iterator = null;
                if (o instanceof scala.collection.mutable.ArraySeq) {
                    iterator = ((scala.collection.mutable.ArraySeq<Object>) o).iterator();
                } else {
                    iterator = ((ArraySeq<Object>) o).iterator();
                }
                while (iterator.hasNext()) {
                    Object value = iterator.next();

                    members.add(convertValueToItem(value, dataType, metadata, memberType));
                }
            }
            Item item = ItemFactory.getInstance().createArrayItem(members, false);
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
                Item item = ItemFactory.getInstance().createArrayItem(members, false);
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
                Item item = ItemFactory.getInstance().createObjectItem(objectKeyList, objectValueList, metadata, false);
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
}
