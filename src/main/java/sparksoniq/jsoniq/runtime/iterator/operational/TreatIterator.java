package sparksoniq.jsoniq.runtime.iterator.operational;

import org.rumbledb.api.Item;
import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.exceptions.TreatException;
import sparksoniq.jsoniq.compiler.translator.expr.operational.base.OperationalExpressionBase;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.semantics.DynamicContext;
import sparksoniq.semantics.types.ItemType;
import sparksoniq.semantics.types.ItemTypes;
import sparksoniq.semantics.types.SequenceType;


public class TreatIterator extends UnaryOperationIterator {

    private static final long serialVersionUID = 1L;
    private final SequenceType _sequenceType;
    private Item _nextResult;
    private int _childIndex;

    public TreatIterator(RuntimeIterator child, SequenceType sequenceType, IteratorMetadata iteratorMetadata) {
        super(child, OperationalExpressionBase.Operator.TREAT, iteratorMetadata);
        this._sequenceType = sequenceType;
    }

    @Override
    public Item next() {
        if (this._hasNext) {
            Item result = _nextResult;
            setNextResult();
            return result;
        } else
            throw new IteratorFlowException(RuntimeIterator.FLOW_EXCEPTION_MESSAGE, getMetadata());
    }

    private void setNextResult() {
        ItemType itemType = _sequenceType.getItemType();
        String sequenceTypeName = ItemTypes.getItemTypeName(itemType.getType().toString());

        _nextResult = null;
        if (_child.hasNext()) {
            _nextResult = _child.next();
        } else {
            _child.close();
            if (
                _childIndex == 0
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

        // ... treat as ()
        if (_nextResult != null && _sequenceType.isEmptySequence())
            throw new TreatException(
                    " "
                        + ItemTypes.getItemTypeName(_nextResult.getClass().getSimpleName())
                        + " cannot be treated as type empty-sequence()",
                    getMetadata()
            );

        // More items
        if (
            _childIndex > 1
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

        if (_nextResult != null && !_nextResult.isTypeOf(itemType)) {
            throw new TreatException(
                    " "
                        + ItemTypes.getItemTypeName(_nextResult.getClass().getSimpleName())
                        + " cannot be treated as type "
                        + sequenceTypeName
                        + _sequenceType.getArity().getSymbol(),
                    getMetadata()
            );
        }

        this._hasNext = _nextResult != null;
        _childIndex++;
    }

    @Override
    public void open(DynamicContext context) {
        super.open(context);
        this._childIndex = 0;
        _child.open(_currentDynamicContext);
        this.setNextResult();
    }
}
