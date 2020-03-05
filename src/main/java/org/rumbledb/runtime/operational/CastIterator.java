package org.rumbledb.runtime.operational;

import org.rumbledb.api.Item;
import org.rumbledb.exceptions.CastException;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.expressions.operational.base.OperationalExpressionBase;
import org.rumbledb.items.AtomicItem;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.types.ItemType;
import org.rumbledb.types.SequenceType;
import org.rumbledb.types.SequenceType.Arity;

import sparksoniq.jsoniq.ExecutionMode;
import sparksoniq.semantics.DynamicContext;

import java.util.ArrayList;
import java.util.List;


public class CastIterator extends UnaryOperationIterator {
    private static final long serialVersionUID = 1L;
    private final SequenceType sequenceType;

    public CastIterator(
            RuntimeIterator child,
            SequenceType sequenceType,
            ExecutionMode executionMode,
            ExceptionMetadata iteratorMetadata
    ) {
        super(child, OperationalExpressionBase.Operator.CAST, executionMode, iteratorMetadata);
        this.sequenceType = sequenceType;
    }

    @Override
    public Item next() {
        if (this.hasNext) {
            List<Item> items = new ArrayList<>();
            this.child.open(this.currentDynamicContextForLocalExecution);
            while (this.child.hasNext()) {
                items.add(this.child.next());
                if (items.size() > 1) {
                    this.child.close();
                    this.hasNext = false;
                    throw new UnexpectedTypeException(
                            " Sequence of more than one item can not be treated as type "
                                + this.sequenceType.toString(),
                            getMetadata()
                    );
                }
            }
            this.child.close();
            this.hasNext = false;

            Item item = items.get(0);
            String itemType = ItemType.convertClassNameToItemTypeName(item.getClass().getSimpleName());

            if (itemType.equals(this.sequenceType.getItemType().toString())) {
                return item;
            }

            String message = String.format(
                "\"%s\": value of type %s is not castable to type %s",
                item.serialize(),
                itemType,
                this.sequenceType.toString()
            );

            AtomicItem atomicItem = CastableIterator.checkInvalidCastable(item, getMetadata(), this.sequenceType);

            if (atomicItem.isCastableAs(this.sequenceType.getItemType())) {
                try {
                    return atomicItem.castAs(this.sequenceType.getItemType());
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
        if (!this.hasNext && !this.sequenceType.isEmptySequence() && this.sequenceType.getArity() != Arity.OneOrZero) {
            throw new UnexpectedTypeException(
                    " Empty sequence can not be cast to type with quantifier '1'",
                    getMetadata()
            );
        }
    }
}
