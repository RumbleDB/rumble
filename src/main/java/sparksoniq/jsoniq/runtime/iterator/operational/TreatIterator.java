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
import java.util.List;


public class TreatIterator extends HybridRuntimeIterator {

    private static final long serialVersionUID = 1L;
    private RuntimeIterator _iterator;
    private Item _nextResult;
    private final SequenceType _sequenceType;
    private int _childIndex;
    private final ItemType itemType;
    private final String sequenceTypeName;

    public TreatIterator(RuntimeIterator iterator, SequenceType sequenceType, IteratorMetadata iteratorMetadata) {
        super(Collections.singletonList(iterator), iteratorMetadata);
        _iterator = iterator;
        this._sequenceType = sequenceType;
        itemType = _sequenceType.getItemType();
        sequenceTypeName = ItemTypes.getItemTypeName(itemType.getType().toString());
    }

    @Override
    protected boolean hasNextLocal() {
        return _hasNext;
    }

    @Override
    protected void resetLocal(DynamicContext context) {
        _iterator.reset(_currentDynamicContext);
        setNextResult();
    }

    @Override
    protected void closeLocal() {
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
            Item result = _nextResult;
            setNextResult();
            return result;
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

        int count = childRDD.take(2).size();
        checkEmptySequence(count);
        checkItemsSize(count);
        Function<Item, Boolean> transformation = new TreatAsClosure(_sequenceType, getMetadata());
        return childRDD.filter(transformation);
    }

    private void checkEmptySequence(long size) {
        if (
            size == 0
                && (_sequenceType.getArity() == SequenceType.Arity.One
                    ||
                    _sequenceType.getArity() == SequenceType.Arity.OneOrMore)
        ) {
            throw new TreatException(
                    " Empty sequence cannot be treated as type "
                        + sequenceTypeName
                        + _sequenceType.getArity().getSymbol(),
                    getMetadata()
            );
        }
    }

    private void checkItemsSize(long size) {
        if (size > 0 && _sequenceType.isEmptySequence())
            throw new TreatException(
                    " "
                        + ItemTypes.getItemTypeName(_nextResult.getClass().getSimpleName())
                        + " cannot be treated as type empty-sequence()",
                    getMetadata()
            );


        if (
            size > 1
                && (_sequenceType.getArity() == SequenceType.Arity.One
                    ||
                    _sequenceType.getArity() == SequenceType.Arity.OneOrZero)
        ) {
            throw new TreatException(
                    " Sequences of more than one item cannot be treated as type "
                        + sequenceTypeName
                        + _sequenceType.getArity().getSymbol(),
                    getMetadata()
            );
        }
    }

    @Override
    public boolean initIsRDD(DynamicContext context) {
        return _iterator.isRDD(context);
    }
}

