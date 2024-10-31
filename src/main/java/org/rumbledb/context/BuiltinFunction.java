package org.rumbledb.context;

import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.types.FunctionSignature;

public class BuiltinFunction {

    /**
     * Special enum type to specify diverse range of execution modes for built-in functions
     * These are only used to generate the corresponding (standard) execution modes while generating iterators
     */
    public enum BuiltinFunctionExecutionMode {
        UNSET,
        LOCAL,
        RDD,
        DATAFRAME,
        INHERIT_FROM_FIRST_ARGUMENT,
        INHERIT_FROM_FIRST_ARGUMENT_BUT_DATAFRAME_FALLSBACK_TO_LOCAL
    }

    private FunctionIdentifier identifier;
    private final FunctionSignature signature;
    private final Class<? extends RuntimeIterator> functionIteratorClass;
    private final BuiltinFunctionExecutionMode builtinFunctionExecutionMode;

    BuiltinFunction(
            FunctionIdentifier identifier,
            FunctionSignature signature,
            Class<? extends RuntimeIterator> functionIteratorClass,
            BuiltinFunctionExecutionMode builtInFunctionExecutionMode
    ) {
        this.identifier = identifier;
        this.signature = signature;
        this.functionIteratorClass = functionIteratorClass;
        this.builtinFunctionExecutionMode = builtInFunctionExecutionMode;
    }

    public FunctionIdentifier getIdentifier() {
        return this.identifier;
    }

    public FunctionSignature getSignature() {
        return this.signature;
    }

    public Class<? extends RuntimeIterator> getFunctionIteratorClass() {
        return this.functionIteratorClass;
    }

    public BuiltinFunctionExecutionMode getBuiltinFunctionExecutionMode() {
        return this.builtinFunctionExecutionMode;
    }

    @Override
    public boolean equals(Object instance) {
        return instance instanceof BuiltinFunction
            && this.getIdentifier().equals(((BuiltinFunction) instance).getIdentifier())
            && this.getSignature().equals(((BuiltinFunction) instance).getSignature())
            && this.getFunctionIteratorClass().equals(((BuiltinFunction) instance).getFunctionIteratorClass())
            && this.getBuiltinFunctionExecutionMode() == ((BuiltinFunction) instance).getBuiltinFunctionExecutionMode();
    }

    @Override
    public int hashCode() {
        return this.getIdentifier().hashCode()
            + this.getSignature().hashCode()
            + this.getFunctionIteratorClass().hashCode()
            + this.getBuiltinFunctionExecutionMode().hashCode();
    }

}
