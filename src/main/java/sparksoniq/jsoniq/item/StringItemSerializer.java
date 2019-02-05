package sparksoniq.jsoniq.item;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public class StringItemSerializer extends Serializer<StringItem> {

    @Override
    public StringItem read(Kryo kryo, Input input, Class<StringItem> type) {
            String s = input.readString();
            return StringItemPool.getStringItem(s);
    }

    @Override
    public void write(Kryo kryo, Output output, StringItem item) {
      output.writeString(item.getStringValue());
    }
}
