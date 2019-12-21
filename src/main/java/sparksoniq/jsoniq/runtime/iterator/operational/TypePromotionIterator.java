package sparksoniq.jsoniq.runtime.iterator.operational;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.rumbledb.api.Item;
import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.exceptions.UnexpectedTypeException;
import sparksoniq.jsoniq.runtime.iterator.HybridRuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.sequences.general.TypePromotionClosure;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.semantics.DynamicContext;
import sparksoniq.semantics.types.ItemType;
import sparksoniq.semantics.types.ItemTypes;
import sparksoniq.semantics.types.SequenceType;

import java.util.Collections;

public class TypePromotionIterator extends HybridRuntimeIterator {

    private static final long serialVersionUID = 1L;
    private final String _exceptionMessage;
    private RuntimeIterator _iterator;
    private SequenceType _sequenceType;

    private ItemType itemType;
    private String sequenceTypeName;

    private Item _nextResult;
    private int _childIndex;

    public TypePromotionIterator(
            RuntimeIterator iterator,
            SequenceType sequenceType,
            String exceptionMessage,
            IteratorMetadata iteratorMetadata
    ) {
        super(Collections.singletonList(iterator), iteratorMetadata);
        this._exceptionMessage = exceptionMessage;
        this._iterator = iterator;
        this._sequenceType = sequenceType;
        itemType = _sequenceType.getItemType();
        sequenceTypeName = ItemTypes.getItemTypeName(itemType.getType().toString());
    }

    @Override
    public boolean hasNextLocal() {
        return _hasNext;
    }

    @Override
    public void resetLocal(DynamicContext context) {
        _iterator.reset(_currentDynamicContextForLocalExecution);
        this._childIndex = 0;
        setNextResult();
    }

    @Override
    public void closeLocal() {
        _iterator.close();
    }

    @Override
    public void openLocal() {
        this._childIndex = 0;
        _iterator.open(_currentDynamicContextForLocalExecution);
        this.setNextResult();
    }


    @Override
    public Item nextLocal() {
        if (this._hasNext) {
            Item _currentResult = _nextResult;
            setNextResult();
            return _currentResult;
        } else
            throw new IteratorFlowException(RuntimeIterator.FLOW_EXCEPTION_MESSAGE, getMetadata());
    }

    private void setNextResult() {
        _nextResult = null;
        if (_iterator.hasNext()) {
            _nextResult = _iterator.next();
            _childIndex++;
        } else {
            _iterator.close();
            checkEmptySequence(_childIndex);
        }

        this._hasNext = _nextResult != null;
        if (!hasNext())
            return;

        checkItemsSize(_childIndex);
        if (!_nextResult.isTypeOf(itemType)) {
            checkTypePromotion();
        }
    }

    private void checkEmptySequence(long size) {
        if (
            size == 0
                && (_sequenceType.getArity() == SequenceType.Arity.One
                    ||
                    _sequenceType.getArity() == SequenceType.Arity.OneOrMore)
        ) {
            throw new UnexpectedTypeException(
                    _exceptionMessage
                        + "Expecting"
                        + ((_sequenceType.getArity() == SequenceType.Arity.OneOrMore) ? " at least" : "")
                        + " one item, but the value provided is the empty sequence.",
                    getMetadata()
            );
        }
    }

    private void checkItemsSize(long size) {
        if (size > 0 && _sequenceType.isEmptySequence()) {
            throw new UnexpectedTypeException(
                    _exceptionMessage
                        + "Expecting empty sequence, but the value provided has at least one item.",
                    getMetadata()
            );
        }


        if (
            size > 1
                && (_sequenceType.getArity() == SequenceType.Arity.One
                    ||
                    _sequenceType.getArity() == SequenceType.Arity.OneOrZero)
        ) {
            throw new UnexpectedTypeException(
                    _exceptionMessage
                        + "Expecting"
                        + ((_sequenceType.getArity() == SequenceType.Arity.OneOrZero) ? " at most" : "")
                        + " one item, but the value provided has at least two items.",
                    getMetadata()
            );
        }
    }

    @Override
    public JavaRDD<Item> getRDDAux(DynamicContext context) {
        JavaRDD<Item> childRDD = _iterator.getRDD(context);

        int count = childRDD.take(2).size();
        checkEmptySequence(count);
        checkItemsSize(count);
        Function<Item, Item> transformation = new TypePromotionClosure(_exceptionMessage, _sequenceType, getMetadata());
        return childRDD.map(transformation);
    }

    private void checkTypePromotion() {
        if (_nextResult.isFunction())
            return;
        if (!_nextResult.canBePromotedTo(_sequenceType.getItemType()))
            throw new UnexpectedTypeException(
                    _exceptionMessage
                        + ItemTypes.getItemTypeName(_nextResult.getClass().getSimpleName())
                        + " cannot be promoted to type "
                        + sequenceTypeName
                        + _sequenceType.getArity().getSymbol()
                        + ".",
                    getMetadata()
            );
        _nextResult = _nextResult.promoteTo(_sequenceType.getItemType());
    }
}
