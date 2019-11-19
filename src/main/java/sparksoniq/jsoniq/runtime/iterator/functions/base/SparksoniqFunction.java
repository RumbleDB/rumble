package sparksoniq.jsoniq.runtime.iterator.functions.base;

import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;

public class SparksoniqFunction {

    private final FunctionSignature signature;
    private final Class<? extends RuntimeIterator> functionIterator;

    SparksoniqFunction(
            FunctionSignature signature,
            Class<? extends RuntimeIterator> functionIterator) {
        this.signature = signature;
        this.functionIterator = functionIterator;
    }

    public FunctionSignature getSignature() {
        return signature;
    }

    public Class<? extends RuntimeIterator> getFunctionIterator() {
        return functionIterator;
    }
}
