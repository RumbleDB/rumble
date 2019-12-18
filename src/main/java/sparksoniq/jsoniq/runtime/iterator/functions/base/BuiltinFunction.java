package sparksoniq.jsoniq.runtime.iterator.functions.base;

import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;

public class BuiltinFunction {

    private FunctionIdentifier identifier;
    private final FunctionSignature signature;
    private final Class<? extends RuntimeIterator> functionIteratorClass;

    BuiltinFunction(
            FunctionIdentifier identifier,
            FunctionSignature signature,
            Class<? extends RuntimeIterator> functionIteratorClass
    ) {
        this.identifier = identifier;
        this.signature = signature;
        this.functionIteratorClass = functionIteratorClass;
    }

    public FunctionIdentifier getIdentifier() {
        return identifier;
    }

    FunctionSignature getSignature() {
        return signature;
    }

    public Class<? extends RuntimeIterator> getFunctionIteratorClass() {
        return functionIteratorClass;
    }


    @Override
    public boolean equals(Object instance) {
        return instance instanceof BuiltinFunction
            && this.getIdentifier().equals(((BuiltinFunction) instance).getIdentifier())
            && this.getSignature().equals(((BuiltinFunction) instance).getSignature())
            && this.getFunctionIteratorClass().equals(((BuiltinFunction) instance).getFunctionIteratorClass());
    }

    @Override
    public int hashCode() {
        return this.getIdentifier().hashCode()
            + this.getSignature().hashCode()
            + this.getFunctionIteratorClass().hashCode();
    }

}
