package org.rumbledb.runtime.typing;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.exceptions.CastableException;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.MoreThanOneItemException;
import org.rumbledb.exceptions.NonAtomicKeyException;
import org.rumbledb.exceptions.UnknownCastTypeException;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.ItemType;
import org.rumbledb.types.SequenceType;
import org.rumbledb.types.SequenceType.Arity;

import java.util.Collections;


public class CastableIterator extends AtMostOneItemLocalRuntimeIterator {
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
    public Item materializeFirstItemOrNull(
            DynamicContext dynamicContext
    ) {
        if (!this.sequenceType.isResolved()) {
            this.sequenceType.resolve(dynamicContext, getMetadata());
        }
        if(!this.sequenceType.getItemType().isAtomicItemType())
        {
            throw new UnknownCastTypeException("The type " + this.sequenceType.getItemType().getIdentifierString() + " is not atomic. Castable can only be used with atomic types.", getMetadata());
        }
        Item item;
        try {
            item = this.child.materializeAtMostOneItemOrNull(dynamicContext);
        } catch (MoreThanOneItemException e) {
            return ItemFactory.getInstance().createBooleanItem(false);
        }

        if (item == null) {
            return ItemFactory.getInstance()
                .createBooleanItem(this.sequenceType.getArity().equals(Arity.OneOrZero));
        }

        checkInvalidCastable(item, getMetadata(), this.sequenceType.getItemType());

        return ItemFactory.getInstance()
            .createBooleanItem(
                CastIterator.castItemToType(item, this.sequenceType.getItemType(), getMetadata()) != null
            );
    }

    static void checkInvalidCastable(Item item, ExceptionMetadata metadata, ItemType type) {
        if (type.equals(BuiltinTypesCatalogue.atomicItem)) {
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
