package org.rumbledb.runtime.functions;

import org.rumbledb.api.Item;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.exceptions.UnknownFunctionCallException;
import org.rumbledb.exceptions.UnsupportedFeatureException;
import org.rumbledb.items.FunctionItem;
import org.rumbledb.runtime.LocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.functions.base.BuiltinFunctionCatalogue;
import org.rumbledb.runtime.functions.base.FunctionIdentifier;

import sparksoniq.jsoniq.ExecutionMode;

public class NamedFunctionReferenceRuntimeIterator extends LocalRuntimeIterator {

    private static final long serialVersionUID = 1L;
    private FunctionIdentifier functionIdentifier;

    public NamedFunctionReferenceRuntimeIterator(
            FunctionIdentifier functionIdentifier,
            ExecutionMode executionMode,
            ExceptionMetadata iteratorMetadata
    ) {
        super(null, executionMode, iteratorMetadata);
        this.functionIdentifier = functionIdentifier;
    }

    @Override
    public Item next() {
        if (this.hasNext) {
            this.hasNext = false;
            if (BuiltinFunctionCatalogue.exists(functionIdentifier)) {
                throw new UnsupportedFeatureException(
                        "Higher order functions using builtin functions are not supported.",
                        getMetadata()
                );
            }
            if (this.currentDynamicContextForLocalExecution.checkUserDefinedFunctionExists(functionIdentifier)) {
                FunctionItem result = this.currentDynamicContextForLocalExecution.getUserDefinedFunction(
                    functionIdentifier
                ).deepCopy();
                result.populateClosureFromDynamicContext(this.currentDynamicContextForLocalExecution, getMetadata());
                return result;
            }
            throw new UnknownFunctionCallException(
                    functionIdentifier.getName(),
                    functionIdentifier.getArity(),
                    getMetadata()
            );
        }

        throw new IteratorFlowException(
                RuntimeIterator.FLOW_EXCEPTION_MESSAGE + this.functionIdentifier,
                getMetadata()
        );
    }
}
