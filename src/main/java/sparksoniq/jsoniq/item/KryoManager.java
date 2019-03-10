package sparksoniq.jsoniq.item;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.ByteBufferInput;
import com.esotericsoftware.kryo.io.ByteBufferOutput;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.util.Pool;

import java.util.ArrayList;

public class KryoManager {

    private static KryoManager _instance;
    private Pool<Kryo> kryoPool;
    private Pool<Output> outputPool;
    private Pool<Input> inputPool;

    public static KryoManager getInstance() {
        if (_instance == null)
            _instance = new KryoManager();
        return _instance;
    }

    private KryoManager() {
    }

    public Kryo getOrCreateKryo() {
        if (kryoPool == null) {
            // 3rd param: max capacity is omitted for no limit
            kryoPool = new Pool<Kryo>(true, false) {
                @Override
                protected Kryo create() {
                    Kryo kryo = new Kryo();
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

                    return kryo;
                }
            };
        }
        return kryoPool.obtain();
    }

    public void releaseKryoInstance(Kryo kryo) {
        kryoPool.free(kryo);
    }

    public Output getOrCreateOutput() {
        if (outputPool == null) {
            // 3rd param: max capacity is omitted for no limit
            outputPool = new Pool<Output>(true, false) {
                @Override
                protected Output create() {
                    return new Output(1024, -1);
                }
            };
        }

        return outputPool.obtain();
    }

    public void releaseOutputInstance(Output output) {
        outputPool.free(output);
    }

    public Input getOrCreateInput() {
        if (inputPool == null) {
            // 3rd param: max capacity is omitted for no limit
            inputPool = new Pool<Input>(true, false) {
                @Override
                protected Input create() {
                    return new Input();
                }
            };
        }
        return inputPool.obtain();
    }

    public void releaseInputInstance(Input input) {
        inputPool.free(input);
    }
}
