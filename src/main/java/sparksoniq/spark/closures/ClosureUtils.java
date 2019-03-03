package sparksoniq.spark.closures;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.apache.spark.sql.Row;
import sparksoniq.jsoniq.item.Item;
import sparksoniq.jsoniq.item.KryoManager;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

public class ClosureUtils {

    public static byte[] serializeItemList(List<Item> toSerialize) {
        Kryo kryo = KryoManager.getInstance().getOrCreateKryo();

        Output output = new Output(new ByteArrayOutputStream());
        kryo.writeClassAndObject(output, toSerialize);
        output.close();

        byte[] byteArray = output.getBuffer();
        return byteArray;
    }

    public static Object deserializeRow(Row row) {
        Input input = new Input(new ByteArrayInputStream((byte[])row.get(0)));

        Kryo kryo = KryoManager.getInstance().getOrCreateKryo();

        Object obj = kryo.readClassAndObject(input);
        input.close();

        return obj;
    }

}
