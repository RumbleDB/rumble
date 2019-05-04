package sparksoniq.spark;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.ByteBufferInput;
import com.esotericsoftware.kryo.io.ByteBufferOutput;
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
import sparksoniq.jsoniq.item.Item;
import sparksoniq.jsoniq.item.KryoManager;

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

    public static byte[] serializeItem(Item toSerialize) {
        Kryo kryo = KM.getOrCreateKryo();

        Output output = new ByteBufferOutput(128, -1);
        kryo.writeClassAndObject(output, toSerialize);
        output.close();

        byte[] byteArray = output.toBytes();

        KM.releaseKryoInstance(kryo);

        return byteArray;
    }

    public static byte[] serializeItemList(List<Item> toSerialize) {
        Kryo kryo = KM.getOrCreateKryo();

        Output output = new ByteBufferOutput(128, -1);
        kryo.writeClassAndObject(output, toSerialize);
        output.close();

        byte[] byteArray = output.toBytes();

        KM.releaseKryoInstance(kryo);

        return byteArray;
    }

    public static String getUdfSQL(StructType inputSchema) {
        String[] columnNames = inputSchema.fieldNames();
        StringBuilder queryColumnString = new StringBuilder();
        for (int columnIndex = 0; columnIndex < columnNames.length; columnIndex++) {
            queryColumnString.append("`");
            queryColumnString.append(columnNames[columnIndex]);
            queryColumnString.append("`");
            if (columnIndex != (columnNames.length - 1)) {
                queryColumnString.append(",");
            }
        }
        return queryColumnString.toString();
    }

    public static String getSelectSQL(StructType inputSchema, int duplicateVariableIndex) {
        String[] columnNames = inputSchema.fieldNames();
        StringBuilder queryColumnString = new StringBuilder();
        for (int columnIndex = 0; columnIndex < columnNames.length; columnIndex++) {
            if (columnIndex == duplicateVariableIndex) {
                continue;
            }
            queryColumnString.append("`");
            queryColumnString.append(columnNames[columnIndex]);
            queryColumnString.append("`");
            queryColumnString.append(",");
        }

        return queryColumnString.toString();
    }

    public static Object deserializeByteArray(byte[] toDeserialize) {
        Kryo kryo = KM.getOrCreateKryo();

        Input input = new Input(new ByteArrayInputStream(toDeserialize));

        Object obj = kryo.readClassAndObject(input);
        input.close();

        KM.releaseKryoInstance(kryo);

        return obj;
    }

    public static void deserializeWrappedParameters(WrappedArray wrappedParameters, List<List<Item>> deserializedParams) {
        Object[] serializedParams = (Object[]) wrappedParameters.array();
        for (int paramIndex = 0; paramIndex < serializedParams.length; paramIndex++) {
            Object serializedParam = serializedParams[paramIndex];
            Object deserializedParam = deserializeByteArray((byte[]) serializedParam);
            deserializedParams.add((List<Item>) deserializedParam);
        }
    }

    public static Row reserializeRowWithNewData(Row prevRow, List<Item> newColumn, int duplicateColumnIndex) {
        List<byte[]> newRowColumns = new ArrayList<>();
        for (int columnIndex = 0; columnIndex < prevRow.length(); columnIndex++) {
            if (duplicateColumnIndex == columnIndex) {
                newRowColumns.add(serializeItemList(newColumn));
            } else {
                newRowColumns.add((byte[]) prevRow.get(columnIndex));
            }
        }
        if (duplicateColumnIndex == -1) {
            newRowColumns.add(serializeItemList(newColumn));
        }
        return RowFactory.create(newRowColumns.toArray());
    }

    public static Object deserializeRowColumn(Row row, int columnIndex) {
        Kryo kryo = KM.getOrCreateKryo();

        Input input = new Input(new ByteArrayInputStream((byte[]) row.get(columnIndex)));

        Object obj = kryo.readClassAndObject(input);
        input.close();

        KM.releaseKryoInstance(kryo);

        return obj;
    }

    public static List<Object> deserializeEntireRow(Row row) {
        Kryo kryo = KM.getOrCreateKryo();

        ArrayList<Object> deserializedColumnObjects = new ArrayList<>();
        for (int columnIndex = 0; columnIndex < row.length(); columnIndex++) {
            Input input = new ByteBufferInput();
            input.setBuffer((byte[]) row.get(columnIndex));
            Object deserializedColumnObject = kryo.readClassAndObject(input);
            deserializedColumnObjects.add(deserializedColumnObject);
            input.close();
        }

        KM.releaseKryoInstance(kryo);
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
