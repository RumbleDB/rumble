package org.rumbledb.items;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.ByteBufferInput;
import com.esotericsoftware.kryo.io.ByteBufferOutput;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.util.Pool;
import org.rumbledb.api.Item;
import org.rumbledb.runtime.functions.base.FunctionIdentifier;
import org.rumbledb.types.ItemType;
import org.rumbledb.types.SequenceType;

import java.util.ArrayList;

public class KryoManager {

    private static KryoManager instance;
    private Pool<Kryo> kryoPool;
    private Pool<Output> outputPool;
    private Pool<Input> inputPool;

    public static KryoManager getInstance() {
        if (instance == null) {
            instance = new KryoManager();
        }
        return instance;
    }

    private KryoManager() {
    }

    public Kryo getOrCreateKryo() {
        if (this.kryoPool == null) {
            // 3rd param: max capacity is omitted for no limit
            this.kryoPool = new Pool<Kryo>(true, false) {
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

                    kryo.register(FunctionItem.class);
                    kryo.register(FunctionIdentifier.class);
                    kryo.register(SequenceType.class);
                    kryo.register(SequenceType.Arity.class);
                    kryo.register(ItemType.class);

                    kryo.register(ArrayList.class);

                    /*
                     * kryo.register(DynamicContext.class);
                     * kryo.register(FlworTuple.class);
                     * kryo.register(FlworKey.class);
                     * kryo.register(SparkRuntimeTupleIterator.class);
                     * kryo.register(RuntimeIterator.class);
                     * kryo.register(RuntimeTupleIterator.class);
                     */

                    return kryo;
                }
            };
        }
        return this.kryoPool.obtain();
    }

    public void releaseKryoInstance(Kryo kryo) {
        this.kryoPool.free(kryo);
    }

    public Output getOrCreateOutput() {
        if (this.outputPool == null) {
            // 3rd param: max capacity is omitted for no limit
            this.outputPool = new Pool<Output>(true, false) {
                @Override
                protected Output create() {
                    return new ByteBufferOutput(128, -1);
                }
            };
        }

        return this.outputPool.obtain();
    }

    public void releaseOutputInstance(Output output) {
        this.outputPool.free(output);
    }

    public Input getOrCreateInput() {
        if (this.inputPool == null) {
            // 3rd param: max capacity is omitted for no limit
            this.inputPool = new Pool<Input>(true, false) {
                @Override
                protected Input create() {
                    return new ByteBufferInput();
                }
            };
        }
        return this.inputPool.obtain();
    }

    public void releaseInputInstance(Input input) {
        this.inputPool.free(input);
    }
}
