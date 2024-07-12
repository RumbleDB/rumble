package org.rumbledb.context;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoSerializable;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.types.SequenceType;

import java.io.Serializable;

public class InScopeVariable implements Serializable, KryoSerializable {
    private static final long serialVersionUID = 1L;

    private Name name;
    private SequenceType sequenceType;
    private ExceptionMetadata metadata;
    private ExecutionMode storageMode;
    private final boolean isAssignable;

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
        this.isAssignable = false; // unspecified means false.
    }

    public InScopeVariable(
            Name name,
            SequenceType type,
            ExceptionMetadata metadata,
            ExecutionMode storageMode,
            boolean isAssignable
    ) {
        this.name = name;
        this.sequenceType = type;
        this.metadata = metadata;
        this.storageMode = storageMode;
        this.isAssignable = isAssignable;
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

    public boolean isAssignable() {
        return isAssignable;
    }
}
