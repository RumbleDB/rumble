package org.rumbledb.context;

import java.io.Serializable;

import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.types.SequenceType;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoSerializable;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public class InScopeVariable implements Serializable, KryoSerializable {
    private static final long serialVersionUID = 1L;

    private Name name;
    private SequenceType sequenceType;
    private ExceptionMetadata metadata;
    private ExecutionMode storageMode;

    public InScopeVariable(
            Name name,
            SequenceType sequenceType,
            ExceptionMetadata metadata,
            ExecutionMode storageMode
    ) {
        this.name = name;
        this.sequenceType = sequenceType;
        this.metadata = metadata;
        this.storageMode = storageMode;
    }

    public Name getName() {
        return this.name;
    }

    public SequenceType getSequenceType() {
        return this.sequenceType;
    }

    public ExceptionMetadata getMetadata() {
        return this.metadata;
    }

    public ExecutionMode getStorageMode() {
        return this.storageMode;
    }

    public void setStorageMode(ExecutionMode mode) {
        this.storageMode = mode;
    }

    @Override
    public void write(Kryo kryo, Output output) {
        kryo.writeObject(output, this.name);
        kryo.writeObject(output, this.sequenceType);
        kryo.writeObject(output, this.metadata);
        kryo.writeObject(output, this.storageMode);
    }

    @Override
    public void read(Kryo kryo, Input input) {
        this.name = kryo.readObject(input, Name.class);
        this.sequenceType = kryo.readObject(input, SequenceType.class);
        this.metadata = kryo.readObject(input, ExceptionMetadata.class);
        this.storageMode = kryo.readObject(input, ExecutionMode.class);
    }
}
