package sparksoniq.jsoniq.runtime.iterator.operational;

import org.rumbledb.api.Item;
import org.rumbledb.exceptions.CastException;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.expressions.operational.base.OperationalExpressionBase;
import sparksoniq.jsoniq.ExecutionMode;
import sparksoniq.jsoniq.item.AtomicItem;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.semantics.DynamicContext;
import sparksoniq.semantics.types.ItemTypes;
import sparksoniq.semantics.types.SingleType;

import java.util.ArrayList;
import java.util.List;


public class CastIterator extends UnaryOperationIterator {
    private static final long serialVersionUID = 1L;
    private final SingleType _singleType;

    public CastIterator(
            RuntimeIterator child,
            SingleType singleType,
            ExecutionMode executionMode,
            ExceptionMetadata iteratorMetadata
    ) {
        super(child, OperationalExpressionBase.Operator.CAST, executionMode, iteratorMetadata);
        this._singleType = singleType;
    }

    @Override
    public Item next() {
        if (this._hasNext) {
            String targetType = ItemTypes.getItemTypeName(this._singleType.getType().toString());

            List<Item> items = new ArrayList<>();
            this._child.open(this._currentDynamicContextForLocalExecution);
            while (this._child.hasNext()) {
                items.add(this._child.next());
                if (items.size() > 1) {
                    this._child.close();
                    this._hasNext = false;
                    throw new UnexpectedTypeException(
                            " Sequence of more than one item can not be treated as type "
                                + targetType,
                            getMetadata()
                    );
                }
            }
            this._child.close();
            this._hasNext = false;

            Item item = items.get(0);
            String itemType = ItemTypes.getItemTypeName(item.getClass().getSimpleName());

            if (itemType.equals(targetType))
                return item;

            String message = String.format(
                "\"%s\": value of type %s is not castable to type %s",
                item.serialize(),
                itemType,
                targetType
            );

            AtomicItem atomicItem = CastableIterator.checkInvalidCastable(item, getMetadata(), this._singleType);

            if (atomicItem.isCastableAs(this._singleType.getType())) {
                try {
                    return atomicItem.castAs(this._singleType.getType());
                } catch (ClassCastException e) {
                    throw new CastException(message, getMetadata());
                }

            }
            throw new CastException(message, getMetadata());
        } else
            throw new IteratorFlowException(RuntimeIterator.FLOW_EXCEPTION_MESSAGE, getMetadata());
    }

    @Override
    public void open(DynamicContext context) {
        super.open(context);
        if (!this._child.hasNext() && !this._singleType.getZeroOrOne())
            throw new UnexpectedTypeException(
                    " Empty sequence can not be cast to type with quantifier '1'",
                    getMetadata()
            );
    }
}
