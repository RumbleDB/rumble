package sparksoniq.jsoniq.runtime.iterator.functions;

import org.apache.spark.api.java.JavaRDD;
import org.rumbledb.api.Item;
import sparksoniq.exceptions.TreatException;
import sparksoniq.exceptions.UnexpectedTypeException;
import sparksoniq.jsoniq.runtime.iterator.HybridRuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.operational.TypePromotionIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.semantics.DynamicContext;

import java.util.Collections;


public class CheckReturnTypeIterator extends HybridRuntimeIterator {

    private static final long serialVersionUID = 1L;
    private TypePromotionIterator _typePromotionIterator;
    private String _functionName;

    public CheckReturnTypeIterator(TypePromotionIterator typePromotionIterator, IteratorMetadata metadata) {
        super(Collections.singletonList(typePromotionIterator), metadata);
        this._typePromotionIterator = typePromotionIterator;
    }


    public CheckReturnTypeIterator(
            TypePromotionIterator typePromotionIterator,
            String functionName,
            IteratorMetadata metadata
    ) {
        this(typePromotionIterator, metadata);
        this._functionName = functionName;
    }

    @Override
    public JavaRDD<Item> getRDDAux(DynamicContext context) {
        JavaRDD<Item> result;
        try {
            result = _typePromotionIterator.getRDDAux(context);
        } catch (TreatException e) {
            String exceptionMessage = e.getJSONiqErrorMessage();
            throw new UnexpectedTypeException(
                    "Invalid return type for "
                        + (_functionName.equals("") ? "inline" : _functionName)
                        + " function. "
                        + exceptionMessage,
                    getMetadata()
            );
        }
        return result;
    }

    @Override
    protected boolean initIsRDD() {
        return _typePromotionIterator.initIsRDD();
    }

    @Override
    protected void openLocal() {
        try {
            _typePromotionIterator.open(_currentDynamicContext);
        } catch (TreatException e) {
            String exceptionMessage = e.getJSONiqErrorMessage();
            throw new UnexpectedTypeException(
                    "Invalid return type for "
                        + (_functionName.equals("") ? "inline" : _functionName)
                        + " function. "
                        + exceptionMessage,
                    getMetadata()
            );
        }
    }

    @Override
    protected void closeLocal() {
        _typePromotionIterator.close();
    }

    @Override
    protected void resetLocal(DynamicContext context) {
        _typePromotionIterator.reset(context);
    }

    @Override
    protected boolean hasNextLocal() {
        return _typePromotionIterator.hasNext();
    }

    @Override
    protected Item nextLocal() {
        Item result;
        try {
            result = _typePromotionIterator.next();
        } catch (TreatException e) {
            String exceptionMessage = e.getJSONiqErrorMessage();
            throw new UnexpectedTypeException(
                    "Invalid return type for "
                        + (_functionName.equals("") ? "inline" : _functionName)
                        + " function. "
                        + exceptionMessage,
                    getMetadata()
            );
        }
        return result;
    }
}
