package sparksoniq.spark.closures;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.ByteBufferInput;
import com.esotericsoftware.kryo.io.ByteBufferOutput;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import sparksoniq.jsoniq.item.Item;
import sparksoniq.jsoniq.item.KryoManager;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

public class ClosureUtils {

    private static KryoManager KM = KryoManager.getInstance();

    public static byte[] serializeItemList(List<Item> toSerialize) {
        Kryo kryo = KM.getOrCreateKryo();

        Output output = new ByteBufferOutput(128, -1);
        kryo.writeClassAndObject(output, toSerialize);
        output.close();

        byte[] byteArray = output.toBytes();

        KM.releaseKryoInstance(kryo);

        return byteArray;
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

        Input input = new Input(new ByteArrayInputStream((byte[])row.get(columnIndex)));

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
            input.setBuffer((byte[])row.get(columnIndex));
            Object deserializedColumnObject = kryo.readClassAndObject(input);
            deserializedColumnObjects.add(deserializedColumnObject);
            input.close();
        }

        KM.releaseKryoInstance(kryo);
        return deserializedColumnObjects;
    }
}
