package sparksoniq.jsoniq.runtime.iterator.operational;

import org.rumbledb.api.Item;
import org.rumbledb.exceptions.CastableException;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.exceptions.NonAtomicKeyException;
import org.rumbledb.expressions.operational.base.OperationalExpressionBase;
import sparksoniq.jsoniq.ExecutionMode;
import sparksoniq.jsoniq.item.AtomicItem;
import sparksoniq.jsoniq.item.ItemFactory;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.iterator.operational.base.UnaryOperationBaseIterator;
import sparksoniq.semantics.types.AtomicTypes;
import sparksoniq.semantics.types.ItemTypes;
import sparksoniq.semantics.types.SingleType;

import java.util.ArrayList;
import java.util.List;


public class CastableIterator extends UnaryOperationBaseIterator {
    private static final long serialVersionUID = 1L;
    private final SingleType _singleType;

    public CastableIterator(
            RuntimeIterator child,
            SingleType singleType,
            ExecutionMode executionMode,
            ExceptionMetadata iteratorMetadata
    ) {
        super(child, OperationalExpressionBase.Operator.CASTABLE, executionMode, iteratorMetadata);
        this._singleType = singleType;
    }

    @Override
    public Item next() {
        if (this._hasNext) {
            List<Item> items = new ArrayList<>();
            this._child.open(this._currentDynamicContextForLocalExecution);
            while (this._child.hasNext()) {
                items.add(this._child.next());
                if (items.size() > 1) {
                    this._child.close();
                    this._hasNext = false;
                    return ItemFactory.getInstance().createBooleanItem(false);
                }
            }
            this._child.close();
            this._hasNext = false;

            if (items.isEmpty())
                return ItemFactory.getInstance().createBooleanItem(this._singleType.getZeroOrOne());

            if (items.size() != 1 || items.get(0) == null)
                return ItemFactory.getInstance().createBooleanItem(false);

            AtomicItem atomicItem = checkInvalidCastable(items.get(0), getMetadata(), this._singleType);

            return ItemFactory.getInstance().createBooleanItem(atomicItem.isCastableAs(this._singleType.getType()));
        } else
            throw new IteratorFlowException(RuntimeIterator.FLOW_EXCEPTION_MESSAGE, getMetadata());
    }

    static AtomicItem checkInvalidCastable(Item item, ExceptionMetadata metadata, SingleType singleType) {
        if (singleType.getType() == AtomicTypes.AtomicItem) {
            throw new CastableException("\"atomic\": invalid type for \"cast\" or \"castable\" expression", metadata);
        }
        AtomicItem atomicItem;

        if (item.isAtomic()) {
            atomicItem = (AtomicItem) item;
        } else {
            String message = String.format(
                "Can not atomize an %1$s item: an %1$s has probably been passed where "
                    +
                    "an atomic value is expected (e.g., as a key, or to a function expecting an atomic item)",
                ItemTypes.getItemTypeName(item.getClass().getSimpleName())
            );
            throw new NonAtomicKeyException(message, metadata);
        }
        return atomicItem;
    }
}
