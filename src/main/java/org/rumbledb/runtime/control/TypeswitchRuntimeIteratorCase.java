package org.rumbledb.runtime.control;

import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.types.SequenceType;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoSerializable;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TypeswitchRuntimeIteratorCase implements Serializable, KryoSerializable {
    private static final long serialVersionUID = 1L;
    private String variableName;
    private List<SequenceType> sequenceTypeUnion;
    private RuntimeIterator returnIterator;

    public TypeswitchRuntimeIteratorCase(
            String variableName,
            List<SequenceType> sequenceTypeUnion,
            RuntimeIterator returnIterator
    ) {
        this.variableName = variableName;
        this.sequenceTypeUnion = sequenceTypeUnion;
        this.returnIterator = returnIterator;
    }

    public TypeswitchRuntimeIteratorCase(String variableName, RuntimeIterator returnIterator) {
        this.variableName = variableName;
        this.sequenceTypeUnion = null;
        this.returnIterator = returnIterator;
    }

    String getVariableName() {
        return this.variableName;
    }

    List<SequenceType> getSequenceTypeUnion() {
        return this.sequenceTypeUnion;
    }

    RuntimeIterator getReturnIterator() {
        return this.returnIterator;
    }

    @Override
    public void write(Kryo kryo, Output output) {
        kryo.writeObject(output, String.class);
        kryo.writeObject(output, ArrayList.class);
        kryo.writeObject(output, RuntimeIterator.class);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void read(Kryo kryo, Input input) {
        this.variableName = kryo.readObject(input, String.class);
        this.sequenceTypeUnion = kryo.readObject(input, ArrayList.class);
        this.returnIterator = kryo.readObject(input, RuntimeIterator.class);
    }
}
