package sparksoniq.jsoniq.runtime.iterator.control;

import sparksoniq.jsoniq.compiler.translator.expr.flowr.FlworVarSequenceType;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.primary.VariableReferenceIterator;

import java.util.List;

public class TypeSwitchCase {
    private final VariableReferenceIterator variable;
    private final List<FlworVarSequenceType> sequenceTypeUnion;
    private final RuntimeIterator returnIterator;

    public TypeSwitchCase(
            VariableReferenceIterator variable,
            List<FlworVarSequenceType> sequenceTypeUnion,
            RuntimeIterator returnIterator
    ) {
        this.variable = variable;
        this.sequenceTypeUnion = sequenceTypeUnion;
        this.returnIterator = returnIterator;
    }

    public TypeSwitchCase(VariableReferenceIterator variable, RuntimeIterator returnIterator) {
        this.variable = variable;
        this.sequenceTypeUnion = null;
        this.returnIterator = returnIterator;
    }

    VariableReferenceIterator getVariable() {
        return variable;
    }

    List<FlworVarSequenceType> getSequenceTypeUnion() {
        return sequenceTypeUnion;
    }

    RuntimeIterator getReturnIterator() {
        return returnIterator;
    }
}
