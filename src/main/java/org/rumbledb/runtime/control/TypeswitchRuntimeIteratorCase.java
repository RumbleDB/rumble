package org.rumbledb.runtime.control;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

import lombok.Getter;

import org.rumbledb.context.Name;
import org.rumbledb.runtime.plan.ItemRuntimePlan;
import org.rumbledb.types.SequenceType;

@Getter
public class TypeswitchRuntimeIteratorCase implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final Name variableName;
    private final List<SequenceType> sequenceTypeUnion;
    private final ItemRuntimePlan returnIterator;

    public TypeswitchRuntimeIteratorCase(
            Name variableName, List<SequenceType> sequenceTypeUnion, ItemRuntimePlan returnIterator) {
        this.variableName = variableName;
        this.sequenceTypeUnion = sequenceTypeUnion;
        this.returnIterator = returnIterator;
    }

    public TypeswitchRuntimeIteratorCase(Name variableName, ItemRuntimePlan returnIterator) {
        this.variableName = variableName;
        this.sequenceTypeUnion = null;
        this.returnIterator = returnIterator;
    }
}
