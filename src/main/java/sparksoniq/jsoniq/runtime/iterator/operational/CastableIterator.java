package sparksoniq.jsoniq.runtime.iterator.operational;

import org.rumbledb.api.Item;
import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.exceptions.NonAtomicKeyException;
import sparksoniq.exceptions.CastableException;
import sparksoniq.jsoniq.compiler.translator.expr.operational.base.OperationalExpressionBase;
import sparksoniq.jsoniq.item.*;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.semantics.types.AtomicType;
import sparksoniq.semantics.types.AtomicTypes;
import sparksoniq.semantics.types.ItemTypes;


public class CastableIterator extends UnaryOperationIterator{
    private static final long serialVersionUID = 1L;
    private final AtomicType _atomicType;

    public CastableIterator(RuntimeIterator child, AtomicType atomicType, IteratorMetadata iteratorMetadata) {
        super(child, OperationalExpressionBase.Operator.CASTABLE, iteratorMetadata);
        this._atomicType = atomicType;
    }

    @Override
    public Item next() {
        if (this._hasNext) {
            Item item = null;

            _child.open(_currentDynamicContext);
            if (_child.hasNext()) {
                item = _child.next();
            }
            _child.close();
            this._hasNext = false;

            if (item == null) return ItemFactory.getInstance().createBooleanItem(false);

            AtomicItem atomicItem = checkInvalidCastable(item, getMetadata(), _atomicType);

            boolean result = atomicItem.isCastableAs(_atomicType);

            return ItemFactory.getInstance().createBooleanItem(result);
        } else
            throw new IteratorFlowException(RuntimeIterator.FLOW_EXCEPTION_MESSAGE, getMetadata());
    }


    @Override
    public Item getResultIfEmpty() {
        if (_atomicType.getZeroOrOne()) {
            return ItemFactory.getInstance().createBooleanItem(true);
        }
        return ItemFactory.getInstance().createBooleanItem(false);
    }


    static AtomicItem checkInvalidCastable(Item item, IteratorMetadata metadata, AtomicType atomicType) {
        if (atomicType.getType() == AtomicTypes.AtomicItem) {
            throw new CastableException("\"atomic\": invalid type for \"cast\" or \"castable\" expression", metadata);
        }
        AtomicItem atomicItem;

        if (item.isAtomic()) {
            atomicItem = (AtomicItem) item;
        }
        else {
            String message = String.format("Can not atomize an %1$s item: an %1$s has probably been passed where " +
                            "an atomic value is expected (e.g., as a key, or to a function expecting an atomic item)",
                    ItemTypes.getItemTypeName(item.getClass().getSimpleName()));
            throw new NonAtomicKeyException(message, metadata.getExpressionMetadata());
        }
        return atomicItem;
    }
}
