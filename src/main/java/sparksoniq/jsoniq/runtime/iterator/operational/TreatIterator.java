package sparksoniq.jsoniq.runtime.iterator.operational;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.rumbledb.api.Item;
import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.exceptions.TreatException;
import sparksoniq.exceptions.UnexpectedTypeException;
import sparksoniq.jsoniq.runtime.iterator.HybridRuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.sequences.general.TreatAsClosure;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.semantics.DynamicContext;
import sparksoniq.semantics.types.ItemType;
import sparksoniq.semantics.types.ItemTypes;
import sparksoniq.semantics.types.SequenceType;

import java.util.Collections;


public class TreatIterator extends HybridRuntimeIterator {

    private static final long serialVersionUID = 1L;
    private RuntimeIterator _iterator;
    private final SequenceType _sequenceType;
    private boolean _shouldThrowTreatException;

    private ItemType itemType;
    private String sequenceTypeName;

    private Item _nextResult;
    private Item _currentResult;
    private int _childIndex;

    public TreatIterator(
            RuntimeIterator iterator,
            SequenceType sequenceType,
            boolean shouldThrowTreatException,
            IteratorMetadata iteratorMetadata
    ) {
        super(Collections.singletonList(iterator), iteratorMetadata);
        _iterator = iterator;
        this._sequenceType = sequenceType;
        itemType = _sequenceType.getItemType();
        sequenceTypeName = ItemTypes.getItemTypeName(itemType.getType().toString());
        this._shouldThrowTreatException = shouldThrowTreatException;
    }

    @Override
    public boolean hasNextLocal() {
        return _hasNext;
    }

    @Override
    public void resetLocal(DynamicContext context) {
        _iterator.reset(_currentDynamicContext);
        setNextResult();
    }

    @Override
    public void closeLocal() {
        _iterator.close();
    }

    @Override
    public void openLocal() {
        this._childIndex = 0;
        _iterator.open(_currentDynamicContext);
        this.setNextResult();
    }

    @Override
    public Item nextLocal() {
        if (this._hasNext) {
            _currentResult = _nextResult;
            setNextResult();
            return _currentResult;
        } else
            throw new IteratorFlowException(RuntimeIterator.FLOW_EXCEPTION_MESSAGE, getMetadata());
    }

    private void setNextResult() {
        _nextResult = null;
        if (_iterator.hasNext()) {
            if (_iterator.isRDD()) {
                if (_currentResult == null) {
                    JavaRDD<Item> childRDD = _iterator.getRDD(_currentDynamicContext);
                    int size = childRDD.take(2).size();
                    checkMoreThanOneItemSequence(size);
                    _nextResult = childRDD.first();
                } else {
                    _nextResult = null;
                }
            } else {
                _nextResult = _iterator.next();
            }
            if (_nextResult != null)
                _childIndex++;
        } else {
            _iterator.close();
            checkEmptySequence(_childIndex);
        }

        this._hasNext = _nextResult != null;
        if (!hasNext())
            return;

        checkTreatAsEmptySequence(_childIndex);
        checkMoreThanOneItemSequence(_childIndex);
        if (!_nextResult.isTypeOf(itemType)) {
            throw new TreatException(
                    " "
                        + ItemTypes.getItemTypeName(_nextResult.getClass().getSimpleName())
                        + " cannot be treated as type "
                        + sequenceTypeName
                        + _sequenceType.getArity().getSymbol(),
                    getMetadata()
            );
        }
    }

    @Override
    public JavaRDD<Item> getRDDAux(DynamicContext dynamicContext) {
        _currentDynamicContext = dynamicContext;
        JavaRDD<Item> childRDD = _iterator.getRDD(dynamicContext);

        if (_sequenceType.getArity() != SequenceType.Arity.ZeroOrMore)
            checkEmptySequence(childRDD.take(2).size());

        Function<Item, Boolean> transformation = new TreatAsClosure(_sequenceType, getMetadata());
        return childRDD.filter(transformation);
    }

    private void checkEmptySequence(int size) {
        if (
            size == 0
                && (_sequenceType.getArity() == SequenceType.Arity.One
                    ||
                    _sequenceType.getArity() == SequenceType.Arity.OneOrMore)
        ) {
            String message = "Empty sequence cannot be treated as type "
                + sequenceTypeName
                + _sequenceType.getArity().getSymbol();
            throw _shouldThrowTreatException
                ? new TreatException(message, getMetadata())
                : new UnexpectedTypeException(message, getMetadata());
        }
    }

    private void checkTreatAsEmptySequence(int size) {
        if (size > 0 && _sequenceType.isEmptySequence())
            throw new TreatException(
                    ItemTypes.getItemTypeName(_nextResult.getClass().getSimpleName())
                        + " cannot be treated as type empty-sequence()",
                    getMetadata()
            );
    }

    private void checkMoreThanOneItemSequence(int size) {
        if (
            size > 1
                && (_sequenceType.getArity() == SequenceType.Arity.One
                    ||
                    _sequenceType.getArity() == SequenceType.Arity.OneOrZero)
        ) {
            throw new TreatException(
                    "Sequences of more than one item cannot be treated as type "
                        + sequenceTypeName
                        + _sequenceType.getArity().getSymbol(),
                    getMetadata()
            );
        }
    }

    @Override
    public boolean initIsRDD() {
        return _sequenceType.getArity() != SequenceType.Arity.One
            && _sequenceType.getArity() != SequenceType.Arity.OneOrZero
            && _iterator.isRDD();
    }
}

