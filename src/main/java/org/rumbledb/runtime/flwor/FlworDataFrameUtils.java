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

package org.rumbledb.runtime.flwor;

import static org.apache.spark.sql.functions.col;
import static org.apache.spark.sql.functions.count;
import static org.apache.spark.sql.functions.first;
import static org.apache.spark.sql.functions.lit;
import static org.apache.spark.sql.functions.monotonically_increasing_id;
import static org.apache.spark.sql.functions.spark_partition_id;
import static org.apache.spark.sql.functions.sum;
import static org.apache.spark.sql.functions.udf;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.expressions.UserDefinedFunction;
import org.apache.spark.sql.expressions.Window;
import org.apache.spark.sql.types.ArrayType;
import org.apache.spark.sql.types.DataType;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import org.rumbledb.api.Item;
import org.rumbledb.config.RumbleRuntimeConfiguration;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.FunctionIdentifier;
import org.rumbledb.context.Name;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.items.AnnotatedItem;
import org.rumbledb.items.AnyURIItem;
import org.rumbledb.items.ArrayItem;
import org.rumbledb.items.Base64BinaryItem;
import org.rumbledb.items.BooleanItem;
import org.rumbledb.items.DateItem;
import org.rumbledb.items.DateTimeItem;
import org.rumbledb.items.DateTimeStampItem;
import org.rumbledb.items.DayTimeDurationItem;
import org.rumbledb.items.DecimalItem;
import org.rumbledb.items.DoubleItem;
import org.rumbledb.items.DurationItem;
import org.rumbledb.items.FloatItem;
import org.rumbledb.items.FunctionItem;
import org.rumbledb.items.HexBinaryItem;
import org.rumbledb.items.IntItem;
import org.rumbledb.items.IntegerItem;
import org.rumbledb.items.NullItem;
import org.rumbledb.items.ObjectItem;
import org.rumbledb.items.StringItem;
import org.rumbledb.items.TimeItem;
import org.rumbledb.items.YearMonthDurationItem;
import org.rumbledb.items.structured.JSoundDataFrame;
import org.rumbledb.runtime.flwor.FlworDataFrameColumn.ColumnFormat;
import org.rumbledb.types.ItemType;
import org.rumbledb.types.SequenceType;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import scala.collection.mutable.WrappedArray;
import sparksoniq.spark.SparkSessionManager;

public class FlworDataFrameUtils {

    // we use UUID to escape backtick within DataFrame columns
    public static String backtickEscape = "d32a3242-b15d-46b8-b689-d2288f7f492f";

    private static ThreadLocal<byte[]> lastBytesCache = ThreadLocal.withInitial(() -> null);

    private static ThreadLocal<List<Item>> lastObjectItemCache = ThreadLocal.withInitial(() -> null);

    public static void registerKryoClassesKryo(Kryo kryo) {
        kryo.register(Item.class);
        kryo.register(AnnotatedItem.class);

        kryo.register(ArrayItem.class);
        kryo.register(ObjectItem.class);

        kryo.register(AnyURIItem.class);
        kryo.register(Base64BinaryItem.class);
        kryo.register(BooleanItem.class);
        kryo.register(DateItem.class);
        kryo.register(DateTimeItem.class);
        kryo.register(DateTimeStampItem.class);
        kryo.register(DayTimeDurationItem.class);
        kryo.register(DecimalItem.class);
        kryo.register(DoubleItem.class);
        kryo.register(DurationItem.class);
        kryo.register(FloatItem.class);
        kryo.register(HexBinaryItem.class);
        kryo.register(IntegerItem.class);
        kryo.register(IntItem.class);
        kryo.register(NullItem.class);
        kryo.register(StringItem.class);
        kryo.register(TimeItem.class);
        kryo.register(YearMonthDurationItem.class);

        kryo.register(FunctionItem.class);
        kryo.register(FunctionIdentifier.class);
        kryo.register(Name.class);
        kryo.register(SequenceType.class);
        kryo.register(SequenceType.Arity.class);
        kryo.register(ItemType.class);

        kryo.register(ArrayList.class);

        kryo.register(RumbleRuntimeConfiguration.class);
    }

    public static byte[] serializeItem(Item toSerialize, Kryo kryo, Output output) {
        output.clear();
        kryo.writeClassAndObject(output, toSerialize);
        return output.toBytes();
    }

    public static byte[] serializeItemList(List<Item> toSerialize, Kryo kryo, Output output) {
        output.clear();
        kryo.writeClassAndObject(output, toSerialize);
        byte[] serializedBytes = output.toBytes();
        if (toSerialize.size() == 1 && toSerialize.get(0).isObject()) {
            lastBytesCache.set(serializedBytes);
            lastObjectItemCache.set(toSerialize);
        }
        return serializedBytes;
    }

    /**
     * @param inputSchema schema specifies the columns to be used in the query
     * @return list of FLWOR columns in the schema
     */
    public static List<FlworDataFrameColumn> getColumns(
            StructType inputSchema
    ) {
        List<FlworDataFrameColumn> result = new ArrayList<>();
        for (String s : inputSchema.fieldNames()) {
            result.add(new FlworDataFrameColumn(s, inputSchema));
        }
        return result;
    }

    /**
     * @param inputSchema schema specifies the columns to be used in the query
     * @param variable the Name fo a variable
     * @return true if the schema contains values for this variable.
     */
    public static boolean hasColumnForVariable(
            StructType inputSchema,
            Name variable
    ) {
        if (variable.equals(Name.CONTEXT_ITEM)) {
            for (String columnName : inputSchema.fieldNames()) {
                if (columnName.equals(SparkSessionManager.atomicJSONiqItemColumnName)) {
                    return true;
                }
            }
            return false;
        }
        String escapedName = variable.getLocalName().replace("`", FlworDataFrameUtils.backtickEscape);
        for (String columnName : inputSchema.fieldNames()) {
            int pos = columnName.indexOf(".");
            if (pos == -1) {
                if (escapedName.equals(columnName)) {
                    return true;
                }
            } else {
                if (escapedName.equals(columnName.substring(0, pos))) {
                    return true;
                } ;
            }
        }
        return false;
    }

    /**
     * Checks if the specified variable only has a count in a DataFrame with the supplied schema.
     * 
     * @param inputSchema schema specifies the columns to be used in the query.
     * @param variable the name of the variable.
     * @return true if it only has a count, false otherwise.
     */
    public static boolean isVariableAvailableAsCountOnly(
            StructType inputSchema,
            Name variable
    ) {
        for (String columnName : inputSchema.fieldNames()) {
            int pos = columnName.indexOf(".");
            if (pos == -1) {
                if (variable.getLocalName().equals(columnName)) {
                    return false;
                }
            } else {
                if (variable.getLocalName().equals(columnName.substring(0, pos))) {
                    return columnName.substring(pos).equals(".count");
                } ;
            }
        }
        throw new OurBadException("Variable " + variable + "not found.");
    }

    /**
     * Checks if the specified variable is available as a native sequence of items in a DataFrame with the
     * supplied schema.
     * 
     * @param inputSchema schema specifies the columns to be used in the query.
     * @param variable the name of the variable.
     * @return true if it is available as a native sequence of items, false otherwise.
     */
    public static boolean isVariableAvailableAsNativeSequence(
            StructType inputSchema,
            Name variable
    ) {
        for (String columnName : inputSchema.fieldNames()) {
            int pos = columnName.indexOf(".");
            if (pos == -1) {
                if (variable.getLocalName().equals(columnName)) {
                    return false;
                }
            } else {
                if (variable.getLocalName().equals(columnName.substring(0, pos))) {
                    return columnName.substring(pos).equals(".sequence");
                } ;
            }
        }
        throw new OurBadException("Variable " + variable + "not found.");
    }

    /**
     * Checks if the specified variable is available as a serialized sequence of items in a DataFrame with the
     * supplied schema.
     * 
     * @param inputSchema schema specifies the columns to be used in the query.
     * @param variable the name of the variable.
     * @return true if it is available as a serialized sequence of items, false otherwise.
     */
    public static boolean isVariableAvailableAsSerializedSequence(
            StructType inputSchema,
            Name variable
    ) {
        for (String columnName : inputSchema.fieldNames()) {
            int pos = columnName.indexOf(".");
            if (pos == -1) {
                if (variable.getLocalName().equals(columnName)) {
                    int index = inputSchema.fieldIndex(columnName);
                    if (inputSchema.fields()[index].dataType().equals(DataTypes.BinaryType)) {
                        return true;
                    }
                    return false;
                }
            } else {
                if (variable.getLocalName().equals(columnName.substring(0, pos))) {
                    return false;
                }
            }
        }
        throw new OurBadException("Variable " + variable + "not found.");
    }

    /**
     * If the variable is available as a single native item, returns its native SQL data type.
     * 
     * @param inputSchema schema specifies the columns to be used in the query.
     * @param variable the name of the variable.
     * @return the native SQL data type of the variable.
     */
    public static DataType nativeTypeOfVariable(
            StructType inputSchema,
            Name variable
    ) {
        for (String columnName : inputSchema.fieldNames()) {
            int pos = columnName.indexOf(".");
            if (pos == -1) {
                if (variable.getLocalName().equals(columnName)) {
                    int index = inputSchema.fieldIndex(columnName);
                    return inputSchema.fields()[index].dataType();
                }
            }
        }
        throw new OurBadException("Variable " + variable + "not found.");
    }

    /**
     * Checks if the specified variable is available as a single native item in a DataFrame with the
     * supplied schema.
     * 
     * @param inputSchema schema specifies the columns to be used in the query.
     * @param variable the name of the variable.
     * @return true if it is available as a single native item, false otherwise.
     */
    public static boolean isVariableAvailableAsNativeItem(
            StructType inputSchema,
            Name variable
    ) {
        if (variable.equals(Name.CONTEXT_ITEM)) {
            for (String columnName : inputSchema.fieldNames()) {
                if (columnName.equals(SparkSessionManager.atomicJSONiqItemColumnName)) {
                    return true;
                }
            }
            return false;
        }
        for (String columnName : inputSchema.fieldNames()) {
            int pos = columnName.indexOf(".");
            if (pos == -1) {
                if (variable.getLocalName().equals(columnName)) {
                    int index = inputSchema.fieldIndex(columnName);
                    if (inputSchema.fields()[index].dataType().equals(DataTypes.BinaryType)) {
                        return false;
                    }
                    return true;
                }
            } else {
                if (variable.getLocalName().equals(columnName.substring(0, pos))) {
                    return false;
                }
            }
        }
        throw new OurBadException("Variable " + variable + " not found.");
    }

    /**
     * Lists the names of the columns of the schema that needed by the dependencies.
     * Pre-aggregrated counts have .count suffixes and might not exactly match the FLWOR variable name.
     * 
     * @param inputSchema schema specifies the columns to be used in the query
     * @param dependencies restriction of the results to within a specified set
     * @return list of SQL column names in the schema
     */
    public static List<FlworDataFrameColumn> getColumns(
            StructType inputSchema,
            Map<Name, DynamicContext.VariableDependency> dependencies
    ) {
        return getColumns(inputSchema, dependencies, null, null);
    }

    /**
     * Lists the names of the columns of the schema that needed by the dependencies.
     * Pre-aggregrated counts have .count suffixes and might not exactly match the FLWOR variable name.
     * 
     * @param inputSchema schema specifies the columns to be used in the query
     * @param dependencies restriction of the results to within a specified set
     * @return list of SQL column names in the schema
     */
    public static List<FlworDataFrameColumn> getColumns(
            StructType inputSchema,
            Map.Entry<Name, DynamicContext.VariableDependency> dependencies
    ) {
        List<FlworDataFrameColumn> result = new ArrayList<>();
        getColumns(inputSchema, dependencies, null, null, result);
        return result;
    }

    /**
     * Lists the names of the columns of the schema that needed by the dependencies, but except duplicates (which are
     * overriden).
     * 
     * @param inputSchema schema specifies the type information for all input columns (included those not needed).
     * @param dependencies restriction of the results to within a specified set
     * @param variablesToRestrictTo variables whose columns must refer to.
     * @param variablesToExclude variables whose columns should be projected away.
     * @return list of SQL column names in the schema
     */
    public static List<FlworDataFrameColumn> getColumns(
            StructType inputSchema,
            Map<Name, DynamicContext.VariableDependency> dependencies,
            List<Name> variablesToRestrictTo,
            List<Name> variablesToExclude
    ) {
        List<FlworDataFrameColumn> result = new ArrayList<>();
        if (dependencies == null) {
            for (String columnName : inputSchema.fieldNames()) {
                FlworDataFrameColumn column = new FlworDataFrameColumn(columnName, inputSchema);
                if (variablesToExclude != null && variablesToExclude.contains(column.getVariableName())) {
                    continue;
                }
                if (variablesToRestrictTo != null && !variablesToRestrictTo.contains(column.getVariableName())) {
                    continue;
                }
                result.add(column);
            }
            return result;
        }
        for (Map.Entry<Name, DynamicContext.VariableDependency> dependency : dependencies.entrySet()) {
            getColumns(inputSchema, dependency, variablesToRestrictTo, variablesToExclude, result);
        }
        return result;
    }

    /**
     * Lists the names of the columns of the schema that needed by the dependencies, but except duplicates (which are
     * overriden).
     * 
     * @param inputSchema schema specifies the type information for all input columns (included those not needed).
     * @param dependency the one variable dependency to look for
     * @param variablesToRestrictTo variables whose columns must refer to.
     * @param variablesToExclude variables whose columns should be projected away.
     * @param result the list for outputting SQL column names in the schema
     */
    public static void getColumns(
            StructType inputSchema,
            Map.Entry<Name, DynamicContext.VariableDependency> dependency,
            List<Name> variablesToRestrictTo,
            List<Name> variablesToExclude,
            List<FlworDataFrameColumn> result
    ) {
        Name variableName = dependency.getKey();
        Set<String> columnNames = new HashSet<>(Arrays.asList(inputSchema.fieldNames()));
        if (variablesToExclude != null && variablesToExclude.contains(variableName)) {
            return;
        }
        if (variablesToRestrictTo != null && !variablesToRestrictTo.contains(variableName)) {
            return;
        }
        switch (dependency.getValue()) {
            case FULL: {
                if (columnNames.contains(variableName.toString())) {
                    result.add(new FlworDataFrameColumn(variableName.toString(), inputSchema));
                    return;
                }
                if (columnNames.contains(variableName.toString() + ".sequence")) {
                    result.add(new FlworDataFrameColumn(variableName, ColumnFormat.NATIVE_SEQUENCE));
                    return;
                }
                throw new OurBadException(
                        "Expecting full variable dependency on "
                            + variableName
                            + " but column not found in the data frame."
                );
            }
            case COUNT: {
                if (columnNames.contains(variableName.toString() + ".count")) {
                    result.add(new FlworDataFrameColumn(variableName, ColumnFormat.COUNT));
                    return;
                }
                if (columnNames.contains(variableName.toString() + ".sequence")) {
                    result.add(new FlworDataFrameColumn(variableName, ColumnFormat.NATIVE_SEQUENCE));
                    return;
                }
                if (columnNames.contains(variableName.toString())) {
                    result.add(new FlworDataFrameColumn(variableName.toString(), inputSchema));
                    return;
                }
                throw new OurBadException(
                        "Expecting count variable dependency on "
                            + variableName
                            + " but no appropriate column was found in the data frame."
                );
            }
            case SUM: {
                if (columnNames.contains(variableName.toString() + ".sum")) {
                    result.add(new FlworDataFrameColumn(variableName, ColumnFormat.SUM));
                    return;
                }
                if (columnNames.contains(variableName.toString() + ".sequence")) {
                    result.add(new FlworDataFrameColumn(variableName, ColumnFormat.NATIVE_SEQUENCE));
                    return;
                }
                if (columnNames.contains(variableName.toString())) {
                    result.add(new FlworDataFrameColumn(variableName.toString(), inputSchema));
                    return;
                }
                throw new OurBadException(
                        "Expecting sum variable dependency on "
                            + variableName
                            + "but no appropriate column was found in the data frame."
                );
            }
            case MIN: {
                if (columnNames.contains(variableName.toString() + ".min")) {
                    result.add(new FlworDataFrameColumn(variableName, ColumnFormat.MIN));
                    return;
                }
                if (columnNames.contains(variableName.toString() + ".sequence")) {
                    result.add(new FlworDataFrameColumn(variableName, ColumnFormat.NATIVE_SEQUENCE));
                    return;
                }
                if (columnNames.contains(variableName.toString())) {
                    result.add(new FlworDataFrameColumn(variableName.toString(), inputSchema));
                    return;
                }
                throw new OurBadException(
                        "Expecting min variable dependency on "
                            + variableName
                            + "but no appropriate column was found in the data frame."
                );
            }
            case MAX: {
                if (columnNames.contains(variableName.toString() + ".max")) {
                    result.add(new FlworDataFrameColumn(variableName, ColumnFormat.MAX));
                    return;
                }
                if (columnNames.contains(variableName.toString() + ".sequence")) {
                    result.add(new FlworDataFrameColumn(variableName, ColumnFormat.NATIVE_SEQUENCE));
                    return;
                }
                if (columnNames.contains(variableName.toString())) {
                    result.add(new FlworDataFrameColumn(variableName.toString(), inputSchema));
                    return;
                }
                throw new OurBadException(
                        "Expecting max variable dependency on "
                            + variableName
                            + "but no appropriate column was found in the data frame."
                );
            }
            case AVERAGE: {
                if (columnNames.contains(variableName.toString() + ".average")) {
                    result.add(new FlworDataFrameColumn(variableName, ColumnFormat.MAX));
                    return;
                }
                if (columnNames.contains(variableName.toString() + ".sequence")) {
                    result.add(new FlworDataFrameColumn(variableName, ColumnFormat.NATIVE_SEQUENCE));
                    return;
                }
                if (columnNames.contains(variableName.toString())) {
                    result.add(new FlworDataFrameColumn(variableName.toString(), inputSchema));
                    return;
                }
                throw new OurBadException(
                        "Expecting average variable dependency on "
                            + variableName
                            + "but no appropriate column was found in the data frame."
                );
            }
            default:
                throw new OurBadException(
                        "Dependency " + dependency.getValue() + " is not supported yet."
                );
        }
    }

    /**
     * Prepares the parameters supplied to a UDF, as a row obtained from the specified attributes.
     * 
     * @param columnNames the names of the columns to pass as a parameter.
     * @return The parameters expressed in SQL.
     */
    public static String getUDFParametersFromColumns(
            List<FlworDataFrameColumn> columnNames
    ) {
        String udfSQL = FlworDataFrameUtils.getSQLColumnProjection(columnNames, false);

        return String.format(
            "struct(%s)",
            udfSQL
        );
    }

    /**
     * Prepares a SQL projection from the specified column names.
     * Not for use in FLWOR DataFrames! Only for native storage of sequences of objects.
     * 
     * @param columnNames schema specifies the columns to be used in the query
     * @param trailingComma boolean field to have a trailing comma
     * @return comma separated variables to be used in spark SQL
     */
    public static String getSQLProjection(
            List<String> columnNames,
            boolean trailingComma
    ) {
        StringBuilder queryColumnString = new StringBuilder();
        String comma = "";
        for (String var : columnNames) {
            queryColumnString.append(comma);
            comma = ",";
            queryColumnString.append("`");
            queryColumnString.append(var);
            queryColumnString.append("`");
        }
        if (trailingComma) {
            queryColumnString.append(comma);
        }
        return queryColumnString.toString();
    }

    /**
     * Prepares a SQL projection from the specified column names.
     * 
     * @param columnNames schema specifies the columns to be used in the query
     * @param trailingComma boolean field to have a trailing comma
     * @return comma separated variables to be used in spark SQL
     */
    public static String getSQLColumnProjection(
            List<FlworDataFrameColumn> columnNames,
            boolean trailingComma
    ) {
        if (columnNames.isEmpty() && !trailingComma) {
            return "'' AS `" + SparkSessionManager.temporaryColumnName + "`";
        }
        StringBuilder queryColumnString = new StringBuilder();
        String comma = "";
        for (FlworDataFrameColumn var : columnNames) {
            queryColumnString.append(comma);
            comma = ",";
            queryColumnString.append(var);
        }
        if (trailingComma) {
            queryColumnString.append(comma);
        }
        return queryColumnString.toString();
    }

    public static StructField[] recursiveRename(StructType schema, boolean inverse) {
        return Arrays.stream(schema.fields()).map(field -> {
            String newName = inverse
                ? field.name().replace(FlworDataFrameUtils.backtickEscape, "`")
                : field.name().replace("`", FlworDataFrameUtils.backtickEscape);
            if (field.dataType() instanceof StructType) {
                StructType castedField = (StructType) field.dataType();
                return new StructField(
                        newName,
                        new StructType(recursiveRename(castedField, inverse)),
                        field.nullable(),
                        field.metadata()
                );
            } else if (field.dataType() instanceof ArrayType) {
                ArrayType castedField = (ArrayType) field.dataType();
                if (castedField.elementType() instanceof StructType) {
                    StructType castedElementType = (StructType) castedField.elementType();
                    return new StructField(
                            newName,
                            new ArrayType(
                                    new StructType(recursiveRename(castedElementType, inverse)),
                                    castedField.containsNull()
                            ),
                            field.nullable(),
                            field.metadata()
                    );
                } else {
                    return new StructField(newName, field.dataType(), field.nullable(), field.metadata());
                }
            } else {
                return new StructField(newName, field.dataType(), field.nullable(), field.metadata());
            }
        }).toArray(StructField[]::new);
    }

    /**
     * recursevely escape/de-escape backticks from all fields in a sparkSql schema
     *
     * @param schema schema to escape
     * @param inverse if true, perform de-escaping, otherwise escape
     * @return the new schema appropriately escaped/de-escaped
     */
    public static StructType escapeSchema(StructType schema, boolean inverse) {
        return new StructType(recursiveRename(schema, inverse));
    }

    /**
     * Prepares a SQL projection for use in a GROUP BY query.
     * 
     * @param inputSchema schema specifies the type information for all input columns (included those not needed).
     * @param duplicateVariableIndex enables skipping a variable
     * @param trailingComma field to have a trailing comma
     * @param serializerUdfName name of the serializer function
     * @param groupbyVariableNames names of group by variables
     * @param dependencies variable dependencies of the group by clause
     * @return comma separated variables to be used in spark SQL
     */
    public static String getGroupBySQLProjection(
            StructType inputSchema,
            int duplicateVariableIndex,
            boolean trailingComma,
            String serializerUdfName,
            List<Name> groupbyVariableNames,
            Map<Name, DynamicContext.VariableDependency> dependencies
    ) {
        StringBuilder queryColumnString = new StringBuilder();
        String comma = "";
        for (Map.Entry<Name, DynamicContext.VariableDependency> dependency : dependencies.entrySet()) {
            queryColumnString.append(comma);
            comma = ",";
            for (FlworDataFrameColumn column : getColumns(inputSchema, dependency)) {
                int columnIndex = inputSchema.fieldIndex(column.getColumnName());
                if (columnIndex == duplicateVariableIndex) {
                    continue;
                }
                DataType dt = inputSchema.fields()[columnIndex].dataType();

                if (column.isCount()) {
                    queryColumnString.append(String.format("sum(%s)", column));
                } else if (column.isSum()) {
                    queryColumnString.append(String.format("sum(%s)", column));
                } else if (column.isMax()) {
                    queryColumnString.append(String.format("max(%s)", column));
                } else if (column.isMin()) {
                    queryColumnString.append(String.format("min(%s)", column));
                } else if (
                    shouldCalculateCountGroupingColumn(dependencies, groupbyVariableNames, column)
                ) {
                    queryColumnString.append("1");
                } else if (shouldCalculateCount(dependencies, column)) {
                    if (column.isNativeSequence()) {
                        queryColumnString.append(String.format("sum(cardinality(%s))", column));
                    } else {
                        queryColumnString.append(String.format("count(%s)", column));
                    }
                    column = new FlworDataFrameColumn(column.getVariableName(), ColumnFormat.COUNT);
                } else if (isProcessingGroupingColumn(groupbyVariableNames, column)) {
                    // rows that end up in the same group have the same value for the grouping column
                    // return a single instance of this value in the grouping column
                    queryColumnString.append(String.format("first(%s)", column));
                } else if (column.isNativeSequence()) {
                    // aggregate the column values for each row in the group
                    queryColumnString.append("arraymerge" + Math.abs(dt.hashCode()));
                    queryColumnString.append(String.format("(collect_list(%s))", column));
                } else if (dt.equals(DataTypes.BinaryType)) {
                    // aggregate the column values for each row in the group
                    queryColumnString.append(serializerUdfName);
                    queryColumnString.append(String.format("(collect_list(%s))", column));
                } else {
                    // aggregate the column values for each row in the group
                    queryColumnString.append(String.format("collect_list(%s)", column));
                    column = new FlworDataFrameColumn(column.getVariableName(), ColumnFormat.NATIVE_SEQUENCE);
                }

                queryColumnString.append(String.format(" as %s", column));

            }
        }
        if (comma.equals("")) {
            queryColumnString.append("TRUE");
        }
        if (trailingComma) {
            queryColumnString.append(",");
        }

        return queryColumnString.toString();
    }

    public static boolean isNativeSequence(StructType schema, String columnName) {
        String[] fields = schema.fieldNames();
        for (String field : fields) {
            if (field.equals(columnName)) {
                return columnName.endsWith(".sequence");
            }
        }
        throw new OurBadException("Column does not exist: " + columnName);
    }

    private static boolean shouldCalculateCountGroupingColumn(
            Map<Name, DynamicContext.VariableDependency> dependencies,
            List<Name> groupbyVariableNames,
            FlworDataFrameColumn column
    ) {
        return dependencies.containsKey(column.getVariableName())
            && dependencies.get(
                column.getVariableName()
            ) == DynamicContext.VariableDependency.COUNT
            && groupbyVariableNames.contains(column.getVariableName());
    }

    private static boolean shouldCalculateCount(
            Map<Name, DynamicContext.VariableDependency> dependencies,
            FlworDataFrameColumn column
    ) {
        return dependencies.containsKey(column.getVariableName())
            && dependencies.get(
                column.getVariableName()
            ) == DynamicContext.VariableDependency.COUNT;
    }

    private static boolean isProcessingGroupingColumn(
            List<Name> groupbyVariableNames,
            FlworDataFrameColumn column
    ) {
        return groupbyVariableNames.contains(column.getVariableName());
    }

    private static Object deserializeByteArray(byte[] toDeserialize, Kryo kryo, Input input) {
        byte[] bytes = lastBytesCache.get();
        if (bytes != null) {
            if (Arrays.equals(bytes, toDeserialize)) {
                return lastObjectItemCache.get();
            }
        }
        input.setBuffer(toDeserialize);
        return kryo.readClassAndObject(input);
    }

    public static void deserializeWrappedParameters(
            WrappedArray<byte[]> wrappedParameters,
            List<List<Item>> deserializedParams,
            Kryo kryo,
            Input input
    ) {
        Object[] serializedParams = (Object[]) wrappedParameters.array();
        for (Object serializedParam : serializedParams) {
            if (serializedParam == null) {
                deserializedParams.add(Collections.emptyList());
                continue;
            }
            @SuppressWarnings("unchecked")
            List<Item> deserializedParam = (List<Item>) deserializeByteArray((byte[]) serializedParam, kryo, input);
            deserializedParams.add(deserializedParam);
        }
    }

    public static Row reserializeRowWithNewData(
            Row prevRow,
            List<Item> newColumn,
            int duplicateColumnIndex,
            Kryo kryo,
            Output output
    ) {
        List<byte[]> newRowColumns = new ArrayList<>();
        for (int columnIndex = 0; columnIndex < prevRow.length(); columnIndex++) {
            if (duplicateColumnIndex == columnIndex) {
                newRowColumns.add(serializeItemList(newColumn, kryo, output));
            } else {
                newRowColumns.add((byte[]) prevRow.get(columnIndex));
            }
        }
        if (duplicateColumnIndex == -1) {
            newRowColumns.add(serializeItemList(newColumn, kryo, output));
        }
        return RowFactory.create(newRowColumns.toArray());
    }

    public static long getCountOfField(Row row, int columnIndex) {
        Object o = row.get(columnIndex);
        if (o instanceof Long) {
            return ((Long) o).longValue();
        } else {
            throw new OurBadException("Count is not available. Items should have been deserialized and counted.");
        }
    }

    /**
     * Zips a JSoundDataFrame to a special column.
     *
     * @param jdf - the JSoundDataframe to perform the operation on
     * @param offset - starting offset for the first index
     * @return returns JSoundDataFrame with the added column containing indices (with some specific UUID)
     */
    public static JSoundDataFrame zipWithIndex(JSoundDataFrame jdf, Long offset) {
        return new JSoundDataFrame(
                zipWithIndex(jdf.getDataFrame(), offset, SparkSessionManager.countColumnName),
                jdf.getItemType()
        );
    }

    /**
     * Algorithm taken from following link and adapted to Java with minor improvements.
     * https://stackoverflow.com/a/48454000/10707488
     *
     * @param df - df to perform the operation on
     * @param offset - starting offset for the first index
     * @param indexName - name of the index column
     * @return returns DataFrame with the added 'indexName' column containing indices
     */
    public static Dataset<Row> zipWithIndex(Dataset<Row> df, Long offset, String indexName) {
        Dataset<Row> dfWithPartitionId = df
            .withColumn("partition_id", spark_partition_id())
            .withColumn("inc_id", monotonically_increasing_id());

        dfWithPartitionId.persist();

        Object partitionOffsetsObject = dfWithPartitionId
            .groupBy("partition_id")
            .agg(count(lit(1)).alias("cnt"), first("inc_id").alias("inc_id"))
            .orderBy("partition_id")
            .select(
                col("partition_id"),
                sum("cnt").over(Window.orderBy("partition_id"))
                    .minus(col("cnt"))
                    .minus(col("inc_id"))
                    .plus(lit(offset).alias("cnt"))
            )
            .collect();
        Row[] partitionOffsetsArray = ((Row[]) partitionOffsetsObject);
        Map<Integer, Long> partitionOffsets = new HashMap<>();
        for (Row row : partitionOffsetsArray) {
            partitionOffsets.put(row.getInt(0), row.getLong(1));
        }

        UserDefinedFunction getPartitionOffset = udf(
            (partitionId) -> partitionOffsets.get(partitionId),
            DataTypes.LongType
        );

        return dfWithPartitionId
            .withColumn("partition_offset", getPartitionOffset.apply(col("partition_id")))
            .withColumn(indexName, col("partition_offset").plus(col("inc_id")))
            .drop("partition_id", "partition_offset", "inc_id");
    }

    public static StructType schemaUnion(StructType leftSchema, StructType rightSchema) {
        List<StructField> fieldList = new ArrayList<StructField>();
        for (StructField f : leftSchema.fields()) {
            fieldList.add(f);
        }
        for (StructField f : rightSchema.fields()) {
            fieldList.add(f);
        }

        StructField[] fields = new StructField[fieldList.size()];
        fieldList.toArray(fields);
        return new StructType(fields);
    }

    public static String createTempView(Dataset<Row> df) {
        String name = "input" + UUID.randomUUID().toString().replaceAll("-", "");
        df.createOrReplaceTempView(name);
        return name;
    }
}
