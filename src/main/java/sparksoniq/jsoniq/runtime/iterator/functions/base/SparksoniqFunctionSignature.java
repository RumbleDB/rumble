package sparksoniq.jsoniq.runtime.iterator.functions.base;

public class SparksoniqFunctionSignature {
    private final int arity;
    private final String functionName;

    public SparksoniqFunctionSignature(String functionName, int arity) {
        this.functionName = functionName;
        this.arity = arity;
    }

    public int getArity() {
        return arity;
    }

    public String getFunctionName() {
        return functionName;
    }

    @Override
    public boolean equals(Object instance) {
        return instance instanceof SparksoniqFunctionSignature
                && this.functionName.equals(((SparksoniqFunctionSignature) instance).getFunctionName())
                && this.arity == ((SparksoniqFunctionSignature) instance).getArity();
    }

    @Override
    public int hashCode() {
        return functionName.hashCode() + arity;
    }
}
