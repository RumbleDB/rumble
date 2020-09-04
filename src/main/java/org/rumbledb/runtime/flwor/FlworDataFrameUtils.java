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

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.expressions.UserDefinedFunction;
import org.apache.spark.sql.expressions.Window;
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
import org.rumbledb.items.ArrayItem;
import org.rumbledb.items.Base64BinaryItem;
import org.rumbledb.items.BooleanItem;
import org.rumbledb.items.DateItem;
import org.rumbledb.items.DateTimeItem;
import org.rumbledb.items.DayTimeDurationItem;
import org.rumbledb.items.DecimalItem;
import org.rumbledb.items.DoubleItem;
import org.rumbledb.items.DurationItem;
import org.rumbledb.items.FunctionItem;
import org.rumbledb.items.HexBinaryItem;
import org.rumbledb.items.IntItem;
import org.rumbledb.items.IntegerItem;
import org.rumbledb.items.NullItem;
import org.rumbledb.items.ObjectItem;
import org.rumbledb.items.StringItem;
import org.rumbledb.items.TimeItem;
import org.rumbledb.items.YearMonthDurationItem;
import org.rumbledb.types.ItemType;
import org.rumbledb.types.SequenceType;

import scala.collection.mutable.WrappedArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.spark.sql.functions.col;
import static org.apache.spark.sql.functions.count;
import static org.apache.spark.sql.functions.first;
import static org.apache.spark.sql.functions.lit;
import static org.apache.spark.sql.functions.monotonically_increasing_id;
import static org.apache.spark.sql.functions.spark_partition_id;
import static org.apache.spark.sql.functions.sum;
import static org.apache.spark.sql.functions.udf;

public class FlworDataFrameUtils {

    private static ThreadLocal<byte[]> lastBytesCache = ThreadLocal.withInitial(() -> null);

    private static ThreadLocal<List<Item>> lastObjectItemCache = ThreadLocal.withInitial(() -> null);

    public static void registerKryoClassesKryo(Kryo kryo) {
        kryo.register(Item.class);
        kryo.register(ArrayItem.class);
        kryo.register(ObjectItem.class);
        kryo.register(StringItem.class);
        kryo.register(IntItem.class);
        kryo.register(IntegerItem.class);
        kryo.register(DoubleItem.class);
        kryo.register(DecimalItem.class);
        kryo.register(NullItem.class);
        kryo.register(BooleanItem.class);

        kryo.register(FunctionItem.class);
        kryo.register(FunctionIdentifier.class);
        kryo.register(Name.class);
        kryo.register(SequenceType.class);
        kryo.register(SequenceType.Arity.class);
        kryo.register(ItemType.class);

        kryo.register(DurationItem.class);
        kryo.register(YearMonthDurationItem.class);
        kryo.register(DayTimeDurationItem.class);

        kryo.register(DateTimeItem.class);
        kryo.register(DateItem.class);
        kryo.register(TimeItem.class);

        kryo.register(Base64BinaryItem.class);
        kryo.register(HexBinaryItem.class);

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
     * @param duplicateVariableIndex enables skipping a variable
     * @param dependencies restriction of the results to within a specified set
     * @return list of SQL column names in the schema
     */
    public static List<String> getColumnNames(
            StructType inputSchema,
            int duplicateVariableIndex,
            Map<Name, DynamicContext.VariableDependency> dependencies
    ) {
        return getColumnNames(inputSchema, duplicateVariableIndex, -1, dependencies);
    }

    /**
     * @param inputSchema schema specifies the columns to be used in the query
     * @param duplicateVariableIndex enables skipping a variable
     * @param duplicatePositionalVariableIndex enables skipping another variable
     * @param dependencies restriction of the results to within a specified set
     * @return list of SQL column names in the schema
     */
    public static List<String> getColumnNames(
            StructType inputSchema,
            int duplicateVariableIndex,
            int duplicatePositionalVariableIndex,
            Map<Name, DynamicContext.VariableDependency> dependencies
    ) {
        List<String> result = new ArrayList<>();
        String[] columnNames = inputSchema.fieldNames();
        for (int columnIndex = 0; columnIndex < columnNames.length; columnIndex++) {
            if (columnIndex == duplicateVariableIndex || columnIndex == duplicatePositionalVariableIndex) {
                continue;
            }
            String var = columnNames[columnIndex];
            if (dependencies == null) {
                result.add(columnNames[columnIndex]);
            } else {
                for (Name name : dependencies.keySet()) {
                    if (name.toString().equals(var)) {
                        result.add(columnNames[columnIndex]);
                    }
                }
            }
        }
        return result;
    }

    /**
     * @param inputSchema schema specifies the columns to be used in the query
     * @param duplicateVariableIndex enables skipping a variable
     * @param dependencies restriction of the results to within a specified set
     * @return list of SQL column names in the schema
     */
    public static Map<String, List<String>> getColumnNamesByType(
            StructType inputSchema,
            int duplicateVariableIndex,
            Map<Name, DynamicContext.VariableDependency> dependencies
    ) {
        Map<String, List<String>> result = new HashMap<>();
        result.put("byte[]", new ArrayList<>());
        result.put("Long", new ArrayList<>());
        StructField[] columns = inputSchema.fields();
        for (Name field : dependencies.keySet()) {
            if (inputSchema.getFieldIndex(field.getLocalName()).isEmpty()) {
                continue;
            } ;
            int columnIndex = inputSchema.fieldIndex(field.getLocalName());
            if (columnIndex == duplicateVariableIndex) {
                continue;
            }
            String var = columns[columnIndex].name();
            DataType type = columns[columnIndex].dataType();
            if (type.equals(DataTypes.BinaryType)) {
                result.get("byte[]").add(var);
            } else if (type.equals(DataTypes.LongType)) {
                result.get("Long").add(var);
            }
        }
        return result;
    }

    public static String getUDFParameters(
            Map<String, List<String>> columnNamesByType
    ) {
        String udfBinarySQL = FlworDataFrameUtils.getListOfSQLVariables(columnNamesByType.get("byte[]"), false);
        String udfLongSQL = FlworDataFrameUtils.getListOfSQLVariables(columnNamesByType.get("Long"), false);

        return String.format(
            "array(%s), array(%s)",
            udfBinarySQL,
            udfLongSQL
        );
    }

    /**
     * @param inputSchema schema specifies the columns to be used in the query
     * @return list of SQL column names in the schema
     */
    public static List<String> getColumnNames(
            StructType inputSchema
    ) {
        return getColumnNames(inputSchema, -1, null);
    }

    /**
     * @param columnNames schema specifies the columns to be used in the query
     * @param trailingComma boolean field to have a trailing comma
     * @return comma separated variables to be used in spark SQL
     */
    public static String getListOfSQLVariables(
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
     * @param inputSchema schema specifies the columns to be used in the query
     * @param duplicateVariableIndex enables skipping a variable
     * @param trailingComma field to have a trailing comma
     * @param serializerUdfName name of the serializer function
     * @param groupbyVariableNames names of group by variables
     * @param dependencies variable dependencies of the group by clause
     * @param columnNamesByType mapping from types(eg. Long) to columnNames(eg. [testColumn1, testColumn2])
     * @return comma separated variables to be used in spark SQL
     */
    public static String getGroupbyProjectSQL(
            StructType inputSchema,
            int duplicateVariableIndex,
            boolean trailingComma,
            String serializerUdfName,
            List<Name> groupbyVariableNames,
            Map<Name, DynamicContext.VariableDependency> dependencies,
            Map<String, List<String>> columnNamesByType
    ) {
        StringBuilder queryColumnString = new StringBuilder();
        String comma = "";
        for (Name field : dependencies.keySet()) {
            queryColumnString.append(comma);
            comma = ",";
            int columnIndex = inputSchema.fieldIndex(field.getLocalName());
            if (columnIndex == duplicateVariableIndex) {
                continue;
            }

            String columnName = field.getLocalName();

            if (isCountPreComputed(columnNamesByType, columnName)) {
                queryColumnString.append("sum(`");
                queryColumnString.append(columnName);
                queryColumnString.append("`)");
            } else if (shouldCalculateCount(dependencies, columnName)) {
                queryColumnString.append("count(`");
                queryColumnString.append(columnName);
                queryColumnString.append("`)");
            } else if (isProcessingGroupingColumn(groupbyVariableNames, columnName)) {
                // rows that end up in the same group have the same value for the grouping column
                // return a single instance of this value in the grouping column
                queryColumnString.append(serializerUdfName);
                queryColumnString.append("(array(first(`");
                queryColumnString.append(columnName);
                queryColumnString.append("`)))");
            } else {
                // aggregate the column values for each row in the group
                queryColumnString.append(serializerUdfName);
                queryColumnString.append("(collect_list(`");
                queryColumnString.append(columnName);
                queryColumnString.append("`))");
            }

            queryColumnString.append(" as `");
            queryColumnString.append(columnName);
            queryColumnString.append("`");

        }
        if (trailingComma) {
            queryColumnString.append(",");
        }

        return queryColumnString.toString();
    }

    private static boolean isCountPreComputed(Map<String, List<String>> columnNamesByType, String columnName) {
        return columnNamesByType.get("Long").contains(columnName);
    }

    private static boolean shouldCalculateCount(
            Map<Name, DynamicContext.VariableDependency> dependencies,
            String columnName
    ) {
        return dependencies.containsKey(Name.createVariableInNoNamespace(columnName))
            && dependencies.get(
                Name.createVariableInNoNamespace(columnName)
            ) == DynamicContext.VariableDependency.COUNT;
    }

    private static boolean isProcessingGroupingColumn(
            List<Name> groupbyVariableNames,
            String columnName
    ) {
        return groupbyVariableNames.contains(Name.createVariableInNoNamespace(columnName));
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
            } else {
                @SuppressWarnings("unchecked")
                List<Item> deserializedParam = (List<Item>) deserializeByteArray((byte[]) serializedParam, kryo, input);
                deserializedParams.add(deserializedParam);
            }
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
}
