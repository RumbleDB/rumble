package org.rumbledb.runtime.operational;

import org.rumbledb.api.Item;
import org.rumbledb.exceptions.CastException;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.expressions.operational.base.OperationalExpressionBase;
import org.rumbledb.items.AtomicItem;
import org.rumbledb.runtime.RuntimeIterator;
import sparksoniq.jsoniq.ExecutionMode;
import sparksoniq.semantics.DynamicContext;
import sparksoniq.semantics.types.ItemTypes;
import sparksoniq.semantics.types.SingleType;

import java.util.ArrayList;
import java.util.List;


public class CastIterator extends UnaryOperationIterator {
    private static final long serialVersionUID = 1L;
    private final SingleType singleType;

    public CastIterator(
            RuntimeIterator child,
            SingleType singleType,
            ExecutionMode executionMode,
            ExceptionMetadata iteratorMetadata
    ) {
        super(child, OperationalExpressionBase.Operator.CAST, executionMode, iteratorMetadata);
        this.singleType = singleType;
    }

    @Override
    public Item next() {
        if (this.hasNext) {
            String targetType = ItemTypes.getItemTypeName(this.singleType.getType().toString());

            List<Item> items = new ArrayList<>();
            this.child.open(this.currentDynamicContextForLocalExecution);
            while (this.child.hasNext()) {
                items.add(this.child.next());
                if (items.size() > 1) {
                    this.child.close();
                    this.hasNext = false;
                    throw new UnexpectedTypeException(
                            " Sequence of more than one item can not be treated as type "
                                + targetType,
                            getMetadata()
                    );
                }
            }
            this.child.close();
            this.hasNext = false;

            Item item = items.get(0);
            String itemType = ItemTypes.getItemTypeName(item.getClass().getSimpleName());

            if (itemType.equals(targetType)) {
                return item;
            }

            String message = String.format(
                "\"%s\": value of type %s is not castable to type %s",
                item.serialize(),
                itemType,
                targetType
            );

            AtomicItem atomicItem = CastableIterator.checkInvalidCastable(item, getMetadata(), this.singleType);

            if (atomicItem.isCastableAs(this.singleType.getType())) {
                try {
                    return atomicItem.castAs(this.singleType.getType());
                } catch (ClassCastException e) {
                    throw new CastException(message, getMetadata());
                }

            }
            throw new CastException(message, getMetadata());
        } else {
            throw new IteratorFlowException(RuntimeIterator.FLOW_EXCEPTION_MESSAGE, getMetadata());
        }
    }

    @Override
    public void open(DynamicContext context) {
        super.open(context);
        if (!this.child.hasNext() && !this.singleType.getZeroOrOne()) {
            throw new UnexpectedTypeException(
                    " Empty sequence can not be cast to type with quantifier '1'",
                    getMetadata()
            );
        }
    }
}
