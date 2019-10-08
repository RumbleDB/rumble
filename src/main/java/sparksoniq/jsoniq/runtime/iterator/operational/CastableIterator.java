package sparksoniq.jsoniq.runtime.iterator.operational;

import org.rumbledb.api.Item;
import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.exceptions.NonAtomicKeyException;
import sparksoniq.exceptions.CastableException;
import sparksoniq.jsoniq.compiler.translator.expr.operational.base.OperationalExpressionBase;
import sparksoniq.jsoniq.item.AtomicItem;
import sparksoniq.jsoniq.item.ItemFactory;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.operational.base.UnaryOperationBaseIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.semantics.types.AtomicType;
import sparksoniq.semantics.types.AtomicTypes;
import sparksoniq.semantics.types.ItemTypes;

import java.util.ArrayList;
import java.util.List;


public class CastableIterator extends UnaryOperationBaseIterator {
    private static final long serialVersionUID = 1L;
    private final AtomicType _atomicType;

    public CastableIterator(RuntimeIterator child, AtomicType atomicType, IteratorMetadata iteratorMetadata) {
        super(child, OperationalExpressionBase.Operator.CASTABLE, iteratorMetadata);
        this._atomicType = atomicType;
    }

    @Override
    public Item next() {
        if (this._hasNext) {
            List<Item> items = new ArrayList<>();
            _child.open(_currentDynamicContext);
            while (_child.hasNext())
                items.add(_child.next());
            _child.close();
            this._hasNext = false;

            if (items.isEmpty())
                return ItemFactory.getInstance().createBooleanItem(_atomicType.getZeroOrOne());

            if (items.size() != 1 || items.get(0) == null) return ItemFactory.getInstance().createBooleanItem(false);

            AtomicItem atomicItem = checkInvalidCastable(items.get(0), getMetadata(), _atomicType);

            return ItemFactory.getInstance().createBooleanItem(atomicItem.isCastableAs(_atomicType));
        } else
            throw new IteratorFlowException(RuntimeIterator.FLOW_EXCEPTION_MESSAGE, getMetadata());
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
