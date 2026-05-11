package org.rumbledb.runtime.control;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoSerializable;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.rumbledb.context.Name;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.types.SequenceType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TypeswitchRuntimeIteratorCase implements Serializable, KryoSerializable {
    private static final long serialVersionUID = 1L;
    private Name variableName;
    private List<SequenceType> sequenceTypeUnion;
    private RuntimeIterator returnIterator;

    public TypeswitchRuntimeIteratorCase(
            Name variableName,
            List<SequenceType> sequenceTypeUnion,
            RuntimeIterator returnIterator
    ) {
        this.variableName = variableName;
        this.sequenceTypeUnion = sequenceTypeUnion;
        this.returnIterator = returnIterator;
    }

    public TypeswitchRuntimeIteratorCase(Name variableName, RuntimeIterator returnIterator) {
        this.variableName = variableName;
        this.sequenceTypeUnion = null;
        this.returnIterator = returnIterator;
    }

    public Name getVariableName() {
        return this.variableName;
    }

    public List<SequenceType> getSequenceTypeUnion() {
        return this.sequenceTypeUnion;
    }

    public RuntimeIterator getReturnIterator() {
        return this.returnIterator;
    }

    @Override
    public void write(Kryo kryo, Output output) {
        kryo.writeObject(output, Name.class);
        kryo.writeObject(output, ArrayList.class);
        kryo.writeObject(output, RuntimeIterator.class);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void read(Kryo kryo, Input input) {
        this.variableName = kryo.readObject(input, Name.class);
        this.sequenceTypeUnion = kryo.readObject(input, ArrayList.class);
        this.returnIterator = kryo.readObject(input, RuntimeIterator.class);
    }
}
