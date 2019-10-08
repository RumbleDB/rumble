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

import java.util.*;

public class TreatIterator extends UnaryOperationIterator {

    private static final long serialVersionUID = 1L;
    private List<Item> results;
    private final SequenceType _sequenceType;
    private int _currentIndex = 0;

    public TreatIterator(RuntimeIterator child, SequenceType sequenceType, IteratorMetadata iteratorMetadata) {
        super(child, OperationalExpressionBase.Operator.TREAT, iteratorMetadata);
        this._sequenceType = sequenceType;
    }

    @Override
    public Item next() {
        if (this._hasNext) {
            return getResult();
        } else
            throw new IteratorFlowException(RuntimeIterator.FLOW_EXCEPTION_MESSAGE, getMetadata());
    }

    public Item getResult() {
        if (results == null || results.size() == 0) {
            throw new IteratorFlowException("getResult called on an empty list of results", getMetadata());
        }
        if (_currentIndex == results.size() - 1)
            _hasNext = false;
        return results.get(_currentIndex++);
    }

    private void compute_treat_as() {
        List<Item> items = new ArrayList<>();
        _child.open(_currentDynamicContext);

        while (_child.hasNext())
            items.add(_child.next());
        _child.close();

        //... treat as ()
        if (!items.isEmpty() && _sequenceType.isEmptySequence())
            throw new TreatException(" " + ItemTypes.getItemTypeName(items.get(0).getClass().getSimpleName()) + " cannot be treated as type empty-sequence()", getMetadata());

        ItemType itemType = _sequenceType.getItemType();
        String sequenceTypeName = ItemTypes.getItemTypeName(itemType.getType().toString());

        //Empty sequence, more items
        if (items.isEmpty() && (_sequenceType.getArity() == SequenceType.Arity.One ||
                _sequenceType.getArity() == SequenceType.Arity.OneOrMore)) {
            throw new TreatException(" Empty sequence cannot be treated as type " + sequenceTypeName + _sequenceType.getArity().getSymbol(), getMetadata());
        }
        if (items.size() > 1 && (_sequenceType.getArity() == SequenceType.Arity.One ||
                _sequenceType.getArity() == SequenceType.Arity.OneOrZero)) {
            throw new TreatException(" Sequences of more than one item cannot be treated as type " + sequenceTypeName + _sequenceType.getArity().getSymbol(), getMetadata());
        }

        results = new ArrayList<>();

        for (Item item : items) {
            if (item.isTypeOf(itemType)) {
                results.add(item);
            } else {
                results.clear();
                throw new TreatException(" " + ItemTypes.getItemTypeName(item.getClass().getSimpleName()) + " cannot be treated as type " + sequenceTypeName + _sequenceType.getArity().getSymbol(), getMetadata());
            }
        }
    }

    @Override
    public void open(DynamicContext context) {
        super.open(context);
        this._currentIndex = 0;

        this.compute_treat_as();
        this._hasNext = results.size() != 0;
    }
}
