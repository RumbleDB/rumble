package sparksoniq.jsoniq.runtime.iterator.functions;

import org.apache.spark.api.java.JavaRDD;
import org.rumbledb.api.Item;
import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.exceptions.TreatException;
import sparksoniq.exceptions.UnexpectedTypeException;
import sparksoniq.exceptions.UnknownFunctionCallException;
import sparksoniq.jsoniq.runtime.iterator.HybridRuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.base.FunctionIdentifier;
import sparksoniq.jsoniq.runtime.iterator.functions.base.FunctionSignature;
import sparksoniq.jsoniq.runtime.iterator.functions.base.BuiltinFunction;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.semantics.DynamicContext;

import java.lang.reflect.Constructor;
import java.util.List;


public class BuiltinFunctionCallIterator extends HybridRuntimeIterator {

    private static final long serialVersionUID = 1L;
    // parametrized fields
    private FunctionIdentifier _functionIdentifier;
    private List<RuntimeIterator> _functionArguments;

    // calculated fields
    private RuntimeIterator _functionCallIterator;
    private Item _nextResult;

    public BuiltinFunctionCallIterator(
            BuiltinFunction builtinFunction,
            List<RuntimeIterator> arguments,
            IteratorMetadata iteratorMetadata
    ) {
        super(arguments, iteratorMetadata);
        _functionIdentifier = builtinFunction.getIdentifier();
        _functionArguments = arguments;
        _functionCallIterator = initializeFnBodyIterator(builtinFunction.getFunctionIterator(), iteratorMetadata);
    }

    @Override
    public void openLocal() {
        _currentDynamicContext = new DynamicContext(_currentDynamicContext);
        try {
            _functionCallIterator.open(_currentDynamicContext);
        } catch (TreatException e) {
            String exceptionMessage = e.getJSONiqErrorMessage();
            throw new UnexpectedTypeException(
                    "Invalid argument for "
                        + (_functionIdentifier.getName().equals("") ? "inline" : _functionIdentifier.getName())
                        + " function. "
                        + exceptionMessage,
                    getMetadata()
            );
        }
        setNextResult();
    }

    @Override
    public Item nextLocal() {
        if (this._hasNext) {
            Item result = _nextResult;
            setNextResult();
            return result;
        }
        throw new IteratorFlowException(
                RuntimeIterator.FLOW_EXCEPTION_MESSAGE + " in " + _functionIdentifier.getName() + "  function",
                getMetadata()
        );
    }

    @Override
    protected boolean hasNextLocal() {
        return _hasNext;
    }

    @Override
    protected void resetLocal(DynamicContext context) {
        _functionCallIterator.reset(_currentDynamicContext);
        setNextResult();
    }

    @Override
    protected void closeLocal() {
        // ensure that recursive function calls terminate gracefully
        // the function call in the body of the deepest recursion call is never visited, never opened and never closed
        if (this.isOpen()) {
            _functionCallIterator.close();
        }
    }

    public void setNextResult() {
        _nextResult = null;
        if (_functionCallIterator.hasNext()) {
            try {
                _nextResult = _functionCallIterator.next();
            } catch (TreatException e) {
                String exceptionMessage = e.getJSONiqErrorMessage();
                throw new UnexpectedTypeException(
                        "Invalid argument for "
                            + (_functionIdentifier.getName().equals("") ? "inline" : _functionIdentifier.getName())
                            + " function. "
                            + exceptionMessage,
                        getMetadata()
                );
            }
        }

        if (_nextResult == null) {
            this._hasNext = false;
            _functionCallIterator.close();
        } else {
            this._hasNext = true;
        }
    }

    @Override
    public JavaRDD<Item> getRDDAux(DynamicContext dynamicContext) {
        _currentDynamicContext = new DynamicContext(_currentDynamicContext);
        JavaRDD<Item> result;
        try {
            result = _functionCallIterator.getRDD(_currentDynamicContext);
        } catch (TreatException e) {
            String exceptionMessage = e.getJSONiqErrorMessage();
            throw new UnexpectedTypeException(
                    "Invalid argument for "
                        + (_functionIdentifier.getName().equals("") ? "inline" : _functionIdentifier.getName())
                        + " function. "
                        + exceptionMessage,
                    getMetadata()
            );
        }
        return result;
    }

    @Override
    public boolean initIsRDD() {
        return _functionCallIterator.isRDD();
    }

    private RuntimeIterator initializeFnBodyIterator(
            Class<? extends RuntimeIterator> fnBodyIteratorClass,
            IteratorMetadata metadata
    ) {
        try {
            Constructor<? extends RuntimeIterator> ctor = fnBodyIteratorClass.getConstructor(
                List.class,
                IteratorMetadata.class
            );
            return ctor.newInstance(_functionArguments, metadata);
        } catch (ReflectiveOperationException e) {
            throw new UnknownFunctionCallException(_functionIdentifier.getName(), _functionArguments.size(), metadata);
        }

    }
}
