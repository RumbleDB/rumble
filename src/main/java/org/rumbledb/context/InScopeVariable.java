package org.rumbledb.context;

import lombok.Getter;
import lombok.Setter;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.types.SequenceType;

@Getter
public class InScopeVariable {

    private final Name name;
    private final SequenceType sequenceType;
    private final ExceptionMetadata metadata;
    @Setter
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

}
