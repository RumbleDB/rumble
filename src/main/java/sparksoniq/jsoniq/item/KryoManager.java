package sparksoniq.jsoniq.item;

import com.esotericsoftware.kryo.Kryo;

import java.util.ArrayList;

public class KryoManager {

    private static KryoManager _instance;
    private Kryo kryo;

    public static KryoManager getInstance() {
        if (_instance == null)
            _instance = new KryoManager();
        return _instance;
    }

    private KryoManager() {
    }

    public Kryo getOrCreateKryo() {
        if (kryo == null) {
            kryo = new Kryo();
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


            /*kryo.register(DynamicContext.class);
            kryo.register(FlworTuple.class);
            kryo.register(FlworKey.class);
            kryo.register(SparkRuntimeTupleIterator.class);
            kryo.register(RuntimeIterator.class);
            kryo.register(RuntimeTupleIterator.class);*/


        }
        return kryo;
    }
}
