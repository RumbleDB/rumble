package sparksoniq.jsoniq.item;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.ByteBufferInput;
import com.esotericsoftware.kryo.io.ByteBufferOutput;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.JavaSerializer;
import com.esotericsoftware.kryo.util.Pool;

import java.util.ArrayList;

import org.rumbledb.api.Item;
import sparksoniq.jsoniq.compiler.translator.expr.Expression;
import sparksoniq.jsoniq.compiler.translator.expr.ExpressionOrClause;
import sparksoniq.jsoniq.compiler.translator.expr.postfix.PostFixExpression;
import sparksoniq.jsoniq.compiler.translator.expr.postfix.extensions.PostfixExtension;
import sparksoniq.jsoniq.runtime.iterator.functions.FunctionItemIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.base.FunctionIdentifier;
import sparksoniq.semantics.types.ItemType;
import sparksoniq.semantics.types.SequenceType;

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
                    kryo.register(FunctionItem.class);

                    kryo.register(ArrayList.class);

                    kryo.register(FunctionIdentifier.class);
                    kryo.register(ExpressionOrClause.class);
                    kryo.register(SequenceType.class);
                    kryo.register(SequenceType.Arity.class);
                    kryo.register(ItemType.class);

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
                    return new ByteBufferOutput(128, -1);
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
                    return new ByteBufferInput();
                }
            };
        }
        return inputPool.obtain();
    }

    public void releaseInputInstance(Input input) {
        inputPool.free(input);
    }
}
