/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
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

package sparksoniq.spark;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.expressions.UserDefinedFunction;
import org.apache.spark.sql.expressions.Window;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructType;
import scala.collection.mutable.WrappedArray;
import sparksoniq.exceptions.SparksoniqRuntimeException;
import sparksoniq.jsoniq.item.ArrayItem;
import sparksoniq.jsoniq.item.BooleanItem;
import sparksoniq.jsoniq.item.DecimalItem;
import sparksoniq.jsoniq.item.DoubleItem;
import sparksoniq.jsoniq.item.IntegerItem;
import sparksoniq.jsoniq.item.Item;
import sparksoniq.jsoniq.item.KryoManager;
import sparksoniq.jsoniq.item.NullItem;
import sparksoniq.jsoniq.item.ObjectItem;
import sparksoniq.jsoniq.item.StringItem;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
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

public class DataFrameUtils {

    private static KryoManager KM = KryoManager.getInstance();
    
    public static void registerKryoClassesKryo(Kryo kryo)
    {
        kryo.register(Item.class);
        kryo.register(ArrayItem.class);
        kryo.register(ObjectItem.class);
        kryo.register(StringItem.class);
        kryo.register(IntegerItem.class);
        kryo.register(DoubleItem.class);
        kryo.register(DecimalItem.class);
        kryo.register(NullItem.class);
        kryo.register(BooleanItem.class);
        kryo.register(ArrayList.class);
    }

    public static byte[] serializeItem(Item toSerialize, Kryo kryo, Output output) {
        output.clear();
        kryo.writeClassAndObject(output, toSerialize);
        return output.toBytes();
    }

    public static List<byte[]> serializeItemList(List<Item> toSerialize, Kryo kryo, Output output) {
        List<byte[]> result = new ArrayList<byte[]>();
        for(Item i : toSerialize)
        {
            output.clear();
            kryo.writeClassAndObject(output, toSerialize);
            result.add(output.toBytes());
        }
        return result;
    }

    /**
     * @param inputSchema            schema specifies the columns to be used in the query
     * @param duplicateVariableIndex enables skipping a variable
     * @param trailingComma          boolean field to have a trailing comma
     * @return comma separated variables to be used in spark SQL
     */
    public static String getSQL(
            StructType inputSchema,
            int duplicateVariableIndex,
            boolean trailingComma) {
        String[] columnNames = inputSchema.fieldNames();
        StringBuilder queryColumnString = new StringBuilder();
        for (int columnIndex = 0; columnIndex < columnNames.length; columnIndex++) {
            if (columnIndex == duplicateVariableIndex) {
                continue;
            }
            queryColumnString.append("`");
            queryColumnString.append(columnNames[columnIndex]);
            queryColumnString.append("`");
            if (trailingComma || columnIndex != (columnNames.length - 1)) {
                queryColumnString.append(",");
            }
        }

        return queryColumnString.toString();
    }

    /**
     * @param inputSchema            schema specifies the columns to be used in the query
     * @param duplicateVariableIndex enables skipping a variable
     * @param trailingComma          boolean field to have a trailing comma
     * @return comma separated variables to be used in spark SQL
     */
    public static String getGroupbyProjectSQL(
            StructType inputSchema,
            int duplicateVariableIndex,
            boolean trailingComma,
            String serializerUdfName,
            List<String> groupbyVariableNames
    ) {
        String[] columnNames = inputSchema.fieldNames();
        StringBuilder queryColumnString = new StringBuilder();
        for (int columnIndex = 0; columnIndex < columnNames.length; columnIndex++) {
            String columnName = columnNames[columnIndex];
            boolean applyDistinct = false;
            if (columnIndex == duplicateVariableIndex) {
                continue;
            }
            if (groupbyVariableNames.contains(columnName)) {
                applyDistinct = true;
            }

            queryColumnString.append(serializerUdfName);
            queryColumnString.append("(");
            if (applyDistinct) {
                queryColumnString.append("array_distinct(");
            }
            queryColumnString.append("collect_list(`");
            queryColumnString.append(columnName);
            queryColumnString.append("`)");
            if (applyDistinct) {
                queryColumnString.append(")");
            }
            queryColumnString.append(") as `");
            queryColumnString.append(columnName);
            queryColumnString.append("`");

            if (trailingComma || columnIndex != (columnNames.length - 1)) {
                queryColumnString.append(",");
            }
        }

        return queryColumnString.toString();
    }

    public static Item deserializeByteArray(byte[] toDeserialize, Kryo kryo, Input input) {
        input.setBuffer(toDeserialize);
        return (Item) kryo.readClassAndObject(input);
    }

    public static void deserializeWrappedParameters(WrappedArray wrappedParameters, List<List<Item>> deserializedParams, Kryo kryo, Input input) {
        Object[] serializedParams = (Object[]) wrappedParameters.array();
        for (Object serializedParam: serializedParams) {
            List<byte[]> bytes = (List<byte[]>) serializedParam;
            List<Item> deserializedParam = new ArrayList<Item>();
            for(byte[] b : bytes)
            {
                deserializedParam.add(deserializeByteArray(b, kryo, input));
            }
            deserializedParams.add(deserializedParam);
        }
    }

    public static Row reserializeRowWithNewData(Row prevRow, List<Item> newColumn, int duplicateColumnIndex, Kryo kryo, Output output) {
        List<List<byte[]>> newRowColumns = new ArrayList<>();
        for (int columnIndex = 0; columnIndex < prevRow.length(); columnIndex++) {
            if (duplicateColumnIndex == columnIndex) {
                newRowColumns.add(serializeItemList(newColumn, kryo, output));
            } else {
                newRowColumns.add((List<byte[]>) prevRow.get(columnIndex));
            }
        }
        if (duplicateColumnIndex == -1) {
            newRowColumns.add(serializeItemList(newColumn, kryo, output));
        }
        return RowFactory.create(newRowColumns.toArray());
    }

    public static List<Item> deserializeRowField(Row row, int columnIndex, Kryo kryo, Input input) {
        List<byte[]> bytes = (List<byte[]>) row.get(columnIndex);
        List<Item> result = new ArrayList<Item>();
        for(byte[] b : bytes)
        {
            input.setBuffer(b);
            result.add((Item) kryo.readClassAndObject(input));
        }
        return result;
    }

    public static List<List<Item>> deserializeEntireRow(Row row, Kryo kryo, Input input) {
        List<List<Item>> deserializedColumnObjects = new ArrayList<List<Item>>();
        for (int columnIndex = 0; columnIndex < row.length(); columnIndex++) {
            List<byte[]> bytes = (List<byte[]>) row.get(columnIndex);
            List<Item> result = new ArrayList<Item>();
            for(byte[] b : bytes)
            {
                input.setBuffer(b);
                result.add((Item) kryo.readClassAndObject(input));
            }
            deserializedColumnObjects.add(result);
        }

        return deserializedColumnObjects;
    }

    /**
     * Algorithm taken from following link and adapted to Java with minor improvements.
     * https://stackoverflow.com/a/48454000/10707488
     *
     * @param df        - df to perform the operation on
     * @param offset    - starting offset for the first index
     * @param indexName - name of the index column
     * @return returns Dataset<Row> with the added 'indexName' column containing indices
     */
    public static Dataset<Row> zipWithIndex(Dataset<Row> df, Long offset, String indexName) {
        Dataset<Row> dfWithPartitionId = df
                .withColumn("partition_id", spark_partition_id())
                .withColumn("inc_id", monotonically_increasing_id());

        Object partitionOffsetsObject = dfWithPartitionId
                .groupBy("partition_id")
                .agg(count(lit(1)).alias("cnt"), first("inc_id").alias("inc_id"))
                .orderBy("partition_id")
                .select(col("partition_id"),
                        sum("cnt").over(Window.orderBy("partition_id"))
                                .minus(col("cnt"))
                                .minus(col("inc_id"))
                                .plus(lit(offset).alias("cnt")))
                .collect();
        Row[] partitionOffsetsArray = ((Row[]) partitionOffsetsObject);
        Map<Integer, Long> partitionOffsets = new HashMap<>();
        for (int i = 0; i < partitionOffsetsArray.length; i++) {
            partitionOffsets.put(partitionOffsetsArray[i].getInt(0), partitionOffsetsArray[i].getLong(1));
        }

        UserDefinedFunction getPartitionOffset = udf(
                (partitionId) -> partitionOffsets.get((Integer) partitionId), DataTypes.LongType
        );

        return dfWithPartitionId
                .withColumn("partition_offset", getPartitionOffset.apply(col("partition_id")))
                .withColumn(indexName, col("partition_offset").plus(col("inc_id")))
                .drop("partition_id", "partition_offset", "inc_id");
    }
}
