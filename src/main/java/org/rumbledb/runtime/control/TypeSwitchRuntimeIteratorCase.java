package org.rumbledb.runtime.control;

import org.rumbledb.expressions.flowr.FlworVarSequenceType;
import org.rumbledb.runtime.RuntimeIterator;

import java.util.List;

public class TypeSwitchRuntimeIteratorCase {
    private final String variableName;
    private final List<FlworVarSequenceType> sequenceTypeUnion;
    private final RuntimeIterator returnIterator;

    public TypeSwitchRuntimeIteratorCase(
            String variableName,
            List<FlworVarSequenceType> sequenceTypeUnion,
            RuntimeIterator returnIterator
    ) {
        this.variableName = variableName;
        this.sequenceTypeUnion = sequenceTypeUnion;
        this.returnIterator = returnIterator;
    }

    public TypeSwitchRuntimeIteratorCase(String variableName, RuntimeIterator returnIterator) {
        this.variableName = variableName;
        this.sequenceTypeUnion = null;
        this.returnIterator = returnIterator;
    }

    String getVariableName() {
        return this.variableName;
    }

    List<FlworVarSequenceType> getSequenceTypeUnion() {
        return this.sequenceTypeUnion;
    }

    RuntimeIterator getReturnIterator() {
        return this.returnIterator;
    }
}
