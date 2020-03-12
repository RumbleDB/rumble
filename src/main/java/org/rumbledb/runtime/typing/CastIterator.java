package org.rumbledb.runtime.typing;

import org.rumbledb.api.Item;
import org.rumbledb.exceptions.CastException;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.items.AtomicItem;
import org.rumbledb.runtime.LocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.types.SequenceType;
import org.rumbledb.types.SequenceType.Arity;

import sparksoniq.jsoniq.ExecutionMode;
import sparksoniq.semantics.DynamicContext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class CastIterator extends LocalRuntimeIterator {
    private static final long serialVersionUID = 1L;
    private final RuntimeIterator child;
    private final SequenceType sequenceType;

    public CastIterator(
            RuntimeIterator child,
            SequenceType sequenceType,
            ExecutionMode executionMode,
            ExceptionMetadata iteratorMetadata
    ) {
        super(Collections.singletonList(child), executionMode, iteratorMetadata);
        this.child = child;
        this.sequenceType = sequenceType;
    }

    @Override
    public Item next() {
        if (this.hasNext) {
            List<Item> items = new ArrayList<>();
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
            String itemType = item.getDynamicType().toString();

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
        this.child.open(context);
        this.hasNext = this.child.hasNext();
        if (!this.hasNext && !this.sequenceType.isEmptySequence() && this.sequenceType.getArity() != Arity.OneOrZero) {
            throw new UnexpectedTypeException(
                    " Empty sequence can not be cast to type with quantifier '1'",
                    getMetadata()
            );
        }
    }
}
