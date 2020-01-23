package sparksoniq.jsoniq.runtime.iterator.control;

import sparksoniq.jsoniq.compiler.translator.expr.flowr.FlworVarSequenceType;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;

import java.util.List;

public class TypeSwitchCase {
    private final String variableName;
    private final List<FlworVarSequenceType> sequenceTypeUnion;
    private final RuntimeIterator returnIterator;

    public TypeSwitchCase(
            String variableName,
            List<FlworVarSequenceType> sequenceTypeUnion,
            RuntimeIterator returnIterator
    ) {
        this.variableName = variableName;
        this.sequenceTypeUnion = sequenceTypeUnion;
        this.returnIterator = returnIterator;
    }

    public TypeSwitchCase(String variableName, RuntimeIterator returnIterator) {
        this.variableName = variableName;
        this.sequenceTypeUnion = null;
        this.returnIterator = returnIterator;
    }

    String getVariableName() {
        return variableName;
    }

    List<FlworVarSequenceType> getSequenceTypeUnion() {
        return sequenceTypeUnion;
    }

    RuntimeIterator getReturnIterator() {
        return returnIterator;
    }
}
