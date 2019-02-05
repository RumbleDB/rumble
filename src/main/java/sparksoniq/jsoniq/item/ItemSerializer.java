package sparksoniq.jsoniq.item;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.naming.OperationNotSupportedException;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public class StringItemSerializer extends Serializer<Item> {

    @Override
    public Item read(Kryo kryo, Input input, Class<Item> type) {
        byte cl = input.readByte();
        switch(cl)
        {
        case 0:
            String s = input.readString();
            return StringItemPool.getStringItem(s);
        case 1:
            boolean b = input.readBoolean();
            return new BooleanItem(b);
        case 2:
            int i = input.readInt();
            return new IntegerItem(i);
        case 3:
            BigDecimal d = kryo.readObject(input, BigDecimal.class);
            return new DecimalItem(d);
        case 4:
            double dou = input.readDouble();
            return new DoubleItem(dou);
        case 5:
            return new NullItem();
        case 6:
            int nb = input.readInt();
            List<String> keys = new ArrayList<>(nb);
            List<Item> values = new ArrayList<>(nb);
            for (int it = 0; it < nb; ++it)
            {
                keys.add(input.readString());
                values.add((Item)kryo.readObject(input, Item.class));
            }
            return new ObjectItem(keys, values);
        case 7:
            int size = input.readInt();
            List<Item> values2 = new ArrayList<>(size);
            for (int it = 0; it < size; ++it)
            {
                values2.add((Item)kryo.readObject(input, Item.class));
            }
            return new ArrayItem(values2);
        default:
            return null;
        }
    }

    @Override
    public void write(Kryo kryo, Output output, Item item) {
        if(item.isString())
        {
            output.writeByte(0);
            try {
              output.writeString(item.getStringValue());
            } catch (OperationNotSupportedException e) {
                
            }
        } else if(item.isBoolean())
        {
            output.writeByte(1);
            try {
              output.writeBoolean(item.getBooleanValue());
            } catch (OperationNotSupportedException e) {
                
            }
        } else if(item.isInteger())
        {
            output.writeByte(2);
            try {
              output.writeInt(item.getIntegerValue());
            } catch (OperationNotSupportedException e) {
                
            }
        } else if(item.isDecimal())
        {
            output.writeByte(3);
            try {
              kryo.writeObject(output, item.getDecimalValue());
            } catch (OperationNotSupportedException e) {
                
            }
        } else if(item.isDouble())
        {
            output.writeByte(4);
            try {
              output.writeDouble(item.getDoubleValue());
            } catch (OperationNotSupportedException e) {
                
            }
        } else if(item.isNull())
        {
            output.writeByte(5);
        } else if(item.isObject())
        {
            output.writeByte(6);
            try {
                List<String> keys = item.getKeys();
                output.writeInt(keys.size());
                for (String key : keys)
                {
                    output.writeString(key);
                    kryo.writeObject(output, item.getItemByKey(key));
                }
            } catch (OperationNotSupportedException e) {
            }
        } else if(item.isArray())
        {
            output.writeByte(7);
            try {
                output.writeInt(item.getSize());
                List<Item> items = item.getItems();
                for (Item i : items)
                {
                    kryo.writeObject(output, i);
                }
            } catch (OperationNotSupportedException e) {
            }
        }
    }

}
