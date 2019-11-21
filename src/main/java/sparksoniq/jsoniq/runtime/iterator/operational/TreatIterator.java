package sparksoniq.jsoniq.runtime.iterator.operational;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.rumbledb.api.Item;
import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.exceptions.TreatException;
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
    Item _nextResult;
    Item _currentResult;
    private SequenceType _sequenceType;
    private int _childIndex;
    private ItemType itemType;
    private String sequenceTypeName;
    boolean _shouldCheckForTypePromotion = true;

    TreatIterator(RuntimeIterator iterator, IteratorMetadata iteratorMetadata) {
        super(Collections.singletonList(iterator), iteratorMetadata);
        _iterator = iterator;
    }

    public TreatIterator(RuntimeIterator iterator, SequenceType sequenceType, IteratorMetadata iteratorMetadata) {
        this(iterator, iteratorMetadata);
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
            _nextResult = _iterator.next();
            if (_nextResult != null)
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
            throw new TreatException(
                    ItemTypes.getItemTypeName(_nextResult.getClass().getSimpleName())
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

        int count = childRDD.take(2).size();
        checkEmptySequence(count);
        checkItemsSize(count);
        Function<Item, Boolean> transformation = new TreatAsClosure(_sequenceType, getMetadata());
        return childRDD.filter(transformation);
    }

    void checkEmptySequence(long size) {
        if (
            size == 0
                && (_sequenceType.getArity() == SequenceType.Arity.One
                    ||
                    _sequenceType.getArity() == SequenceType.Arity.OneOrMore)
        ) {
            _shouldCheckForTypePromotion = false;
            throw new TreatException(
                    "Empty sequence cannot be treated as type "
                        + sequenceTypeName
                        + _sequenceType.getArity().getSymbol(),
                    getMetadata()
            );
        }
    }

    void checkItemsSize(long size) {
        if (size > 0 && _sequenceType.isEmptySequence()) {
            _shouldCheckForTypePromotion = false;
            throw new TreatException(
                    ItemTypes.getItemTypeName(_nextResult.getClass().getSimpleName())
                        + " cannot be treated as type empty-sequence()",
                    getMetadata()
            );
        }


        if (
            size > 1
                && (_sequenceType.getArity() == SequenceType.Arity.One
                    ||
                    _sequenceType.getArity() == SequenceType.Arity.OneOrZero)
        ) {
            _shouldCheckForTypePromotion = false;
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
        return _iterator.isRDD();
    }

    public void setSequenceType(SequenceType _sequenceType) {
        this._sequenceType = _sequenceType;
        this.itemType = _sequenceType.getItemType();
        this.sequenceTypeName = ItemTypes.getItemTypeName(itemType.getType().toString());
    }
}

