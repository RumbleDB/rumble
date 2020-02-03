package sparksoniq.jsoniq.runtime.iterator.operational;

import org.rumbledb.api.Item;
import sparksoniq.exceptions.CastException;
import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.exceptions.UnexpectedTypeException;
import sparksoniq.jsoniq.ExecutionMode;
import sparksoniq.jsoniq.compiler.translator.expr.operational.base.OperationalExpressionBase;
import sparksoniq.jsoniq.item.AtomicItem;
import sparksoniq.jsoniq.runtime.iterator.RuntimeIterator;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
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
            IteratorMetadata iteratorMetadata
    ) {
        super(child, OperationalExpressionBase.Operator.CAST, executionMode, iteratorMetadata);
        this._singleType = singleType;
    }

    @Override
    public Item next() {
        if (this._hasNext) {
            String targetType = ItemTypes.getItemTypeName(_singleType.getType().toString());

            List<Item> items = new ArrayList<>();
            _child.open(_currentDynamicContextForLocalExecution);
            while (_child.hasNext()) {
                items.add(_child.next());
                if (items.size() > 1) {
                    _child.close();
                    this._hasNext = false;
                    throw new UnexpectedTypeException(
                            " Sequence of more than one item can not be treated as type "
                                + targetType,
                            getMetadata()
                    );
                }
            }
            _child.close();
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

            AtomicItem atomicItem = CastableIterator.checkInvalidCastable(item, getMetadata(), _singleType);

            if (atomicItem.isCastableAs(_singleType.getType())) {
                try {
                    return atomicItem.castAs(_singleType.getType());
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
        if (!_child.hasNext() && !_singleType.getZeroOrOne())
            throw new UnexpectedTypeException(
                    " Empty sequence can not be cast to type with quantifier '1'",
                    getMetadata()
            );
    }
}
