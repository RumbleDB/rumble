package org.rumbledb.runtime.typing;

import org.rumbledb.api.Item;
import org.rumbledb.exceptions.CastableException;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.exceptions.NonAtomicKeyException;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.LocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.types.ItemType;
import org.rumbledb.types.SequenceType;
import org.rumbledb.types.SequenceType.Arity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class CastableIterator extends LocalRuntimeIterator {
    private static final long serialVersionUID = 1L;
    private final RuntimeIterator child;
    private final SequenceType sequenceType;

    public CastableIterator(
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
            this.child.open(this.currentDynamicContextForLocalExecution);
            while (this.child.hasNext()) {
                items.add(this.child.next());
                if (items.size() > 1) {
                    this.child.close();
                    this.hasNext = false;
                    return ItemFactory.getInstance().createBooleanItem(false);
                }
            }
            this.child.close();
            this.hasNext = false;

            if (items.isEmpty()) {
                return ItemFactory.getInstance()
                    .createBooleanItem(this.sequenceType.getArity().equals(Arity.OneOrZero));
            }

            if (items.size() != 1 || items.get(0) == null) {
                return ItemFactory.getInstance().createBooleanItem(false);
            }

            Item item = items.get(0);
            checkInvalidCastable(item, getMetadata(), this.sequenceType);

            return ItemFactory.getInstance()
                .createBooleanItem(item.isCastableAs(this.sequenceType.getItemType()));
        } else {
            throw new IteratorFlowException(RuntimeIterator.FLOW_EXCEPTION_MESSAGE, getMetadata());
        }
    }

    static void checkInvalidCastable(Item item, ExceptionMetadata metadata, SequenceType type) {
        if (type.getItemType().equals(ItemType.atomicItem)) {
            throw new CastableException("\"atomic\": invalid type for \"cast\" or \"castable\" expression", metadata);
        }
        if (item.isAtomic()) {
            return;
        }

        String message = String.format(
            "Can not atomize an %1$s item: an %1$s has probably been passed where "
                +
                "an atomic value is expected (e.g., as a key, or to a function expecting an atomic item)",
            item.getDynamicType().toString()
        );
        throw new NonAtomicKeyException(message, metadata);
    }
}
