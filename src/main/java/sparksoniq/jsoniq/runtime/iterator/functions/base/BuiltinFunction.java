package sparksoniq.jsoniq.runtime.iterator.functions.base;

import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;

public class BuiltinFunction {

    private FunctionIdentifier identifier;
    private final FunctionSignature signature;
    private final Class<? extends RuntimeIterator> functionIterator;

    BuiltinFunction(
            FunctionIdentifier identifier,
            FunctionSignature signature,
            Class<? extends RuntimeIterator> functionIterator
    ) {
        this.identifier = identifier;
        this.signature = signature;
        this.functionIterator = functionIterator;
    }

    public FunctionIdentifier getIdentifier() {
        return identifier;
    }

    FunctionSignature getSignature() {
        return signature;
    }

    public Class<? extends RuntimeIterator> getFunctionIterator() {
        return functionIterator;
    }


    @Override
    public boolean equals(Object instance) {
        return instance instanceof BuiltinFunction
            && this.getIdentifier().equals(((BuiltinFunction) instance).getIdentifier())
            && this.getSignature().equals(((BuiltinFunction) instance).getSignature())
            && this.getFunctionIterator().equals(((BuiltinFunction) instance).getFunctionIterator());
    }

    @Override
    public int hashCode() {
        return this.getIdentifier().hashCode() + this.getSignature().hashCode() + this.getFunctionIterator().hashCode();
    }

}
