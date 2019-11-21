package sparksoniq.jsoniq.runtime.iterator.operational;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.rumbledb.api.Item;
import sparksoniq.exceptions.TreatException;
import sparksoniq.jsoniq.item.AtomicItem;
import sparksoniq.jsoniq.runtime.iterator.HybridRuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.functions.sequences.general.TypePromotionClosure;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.semantics.DynamicContext;
import sparksoniq.semantics.types.SequenceType;

import java.util.Collections;

public class TypePromotionIterator extends HybridRuntimeIterator {

    private static final long serialVersionUID = 1L;
    private SequenceType _sequenceType;
    private TreatIterator _treatIterator;
    private RuntimeIterator _child;

    public TypePromotionIterator(RuntimeIterator iterator, IteratorMetadata iteratorMetadata) {
        super(Collections.singletonList(iterator), iteratorMetadata);
        this._child = iterator;
        this._treatIterator = new TreatIterator(_child, iteratorMetadata);
    }

    public TypePromotionIterator(RuntimeIterator iterator, SequenceType sequenceType, IteratorMetadata iteratorMetadata) {
        this(iterator, iteratorMetadata);
        this.setSequenceType(sequenceType);
    }

    @Override
    public void openLocal() {
        try {
            _treatIterator.open(_currentDynamicContext);
        } catch (TreatException e) {
            this.checkTypePromotion(e.getJSONiqErrorMessage());
        }
    }

    @Override
    public void closeLocal() {
        _treatIterator.close();
    }

    @Override
    public void resetLocal(DynamicContext context) {
        _treatIterator.reset(context);
    }

    @Override
    public boolean hasNextLocal() {
        return _treatIterator.hasNext();
    }

    @Override
    public Item nextLocal() {
        Item result;
        try {
            result = _treatIterator.next();
        } catch (TreatException e) {
            this.checkTypePromotion(e.getJSONiqErrorMessage());
            result = _treatIterator._currentResult;
        }
        return result;
    }

    @Override
    public boolean initIsRDD() {
        return _treatIterator.isRDD();
    }

    @Override
    public JavaRDD<Item> getRDDAux(DynamicContext context) {
        _currentDynamicContext = context;
        JavaRDD<Item> childRDD = _child.getRDD(context);

        int count = childRDD.take(2).size();
        _treatIterator.checkEmptySequence(count);
        _treatIterator.checkItemsSize(count);
        Function<Item, Item> transformation = new TypePromotionClosure(_sequenceType, getMetadata());
        return childRDD.map(transformation);
    }

    private boolean resultCanBePromoted() {
        return _treatIterator._shouldCheckForTypePromotion
            && _treatIterator._nextResult != null
            && _treatIterator._nextResult.canBePromotedTo(_sequenceType.getItemType());
    }

    private void checkTypePromotion(String treatExceptionMessage) {
        if (_treatIterator._nextResult != null && _treatIterator._nextResult.isFunction()) return;
        if (!this.resultCanBePromoted())
            throw new TreatException(treatExceptionMessage.replaceFirst("treated as", "promoted to"), getMetadata());
        _treatIterator._nextResult = _treatIterator._nextResult.promoteTo(_sequenceType.getItemType());
    }

    public void setSequenceType(SequenceType _sequenceType) {
        this._sequenceType = _sequenceType;
        this._treatIterator.setSequenceType(_sequenceType);
    }
}
