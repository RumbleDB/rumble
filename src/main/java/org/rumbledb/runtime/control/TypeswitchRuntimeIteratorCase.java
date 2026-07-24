package org.rumbledb.runtime.control;

import org.rumbledb.context.Name;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.types.SequenceType;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

public class TypeswitchRuntimeIteratorCase implements Serializable {
    @Serial
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


}
