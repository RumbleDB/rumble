package org.rumbledb.context;

import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.types.SequenceType;

public class InScopeVariable {

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

    public boolean isAssignable() {
        return this.isAssignable;
    }
}
