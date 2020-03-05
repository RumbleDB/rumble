package org.rumbledb.runtime.control;

import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.types.SequenceType;

import java.util.List;

public class TypeswitchRuntimeIteratorCase {
    private final String variableName;
    private final List<SequenceType> sequenceTypeUnion;
    private final RuntimeIterator returnIterator;

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
}
