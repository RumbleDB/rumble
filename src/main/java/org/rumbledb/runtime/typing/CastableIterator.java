package org.rumbledb.runtime.typing;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.CastableException;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.MoreThanOneItemException;
import org.rumbledb.exceptions.NonAtomicKeyException;
import org.rumbledb.exceptions.UnknownCastTypeException;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.cursor.AtMostOneLocalCursor;
import org.rumbledb.runtime.cursor.LocalCursor;
import org.rumbledb.runtime.cursor.LocalCursorUtils;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.ItemType;
import org.rumbledb.types.SequenceType;
import org.rumbledb.types.SequenceType.Arity;

import java.io.Serial;
import java.util.Collections;


public class CastableIterator extends AtMostOneItemLocalRuntimeIterator {
    @Serial
    private static final long serialVersionUID = 1L;
    private final RuntimeIterator child;
    private final SequenceType sequenceType;

    public CastableIterator(
            RuntimeIterator child,
            SequenceType sequenceType,
            RuntimeStaticContext staticContext
    ) {
        super(Collections.singletonList(child), staticContext);
        this.child = child;
        this.sequenceType = sequenceType;
    }

    @Override
    public Item materializeFirstItemOrNull(
            DynamicContext dynamicContext
    ) {
        return evaluate(this.child, this.sequenceType, this.staticContext, getMetadata(), dynamicContext);
    }

    @Override
    public LocalCursor<Item> createLocalCursor(DynamicContext context) {
        return new Cursor(this.child, this.sequenceType, this.staticContext, getMetadata(), context);
    }

    private static Item evaluate(
            RuntimeIterator child,
            SequenceType sequenceType,
            RuntimeStaticContext staticContext,
            ExceptionMetadata metadata,
            DynamicContext dynamicContext
    ) {
        if (!sequenceType.isResolved()) {
            sequenceType.resolve(dynamicContext, metadata);
        }
        ItemType targetItemType = sequenceType.getItemType();
        boolean validCastTarget =
            targetItemType.isAtomicItemType()
                || (targetItemType.isUnionType()
                    && targetItemType.getTypes().stream().allMatch(ItemType::isAtomicItemType));
        if (!validCastTarget) {
            throw new UnknownCastTypeException(
                    "The type "
                        + targetItemType.getIdentifierString()
                        + " is not atomic. Castable can only be used with atomic types.",
                    metadata
            );
        }
        Item item;
        try {
            item = LocalCursorUtils.materializeAtMostOne(child, dynamicContext);
            if (item != null && !item.getDynamicType().isResolved()) {
                item.getDynamicType().resolve(dynamicContext, metadata);
            }
        } catch (MoreThanOneItemException e) {
            return ItemFactory.getInstance().createBooleanItem(false);
        }

        if (item == null) {
            return ItemFactory.getInstance()
                .createBooleanItem(sequenceType.getArity().equals(Arity.OneOrZero));
        }

        checkInvalidCastable(item, metadata, sequenceType.getItemType());
        try {
            Item res = CastIterator.castItemToType(
                item,
                sequenceType.getItemType(),
                metadata,
                staticContext
            );
            return ItemFactory.getInstance()
                .createBooleanItem(res != null);
        } catch (Exception e) {
            return ItemFactory.getInstance()
                .createBooleanItem(false);
        }

    }

    private static final class Cursor extends AtMostOneLocalCursor<Item> {

        private final RuntimeIterator childPlan;
        private final SequenceType sequenceType;
        private final RuntimeStaticContext staticContext;
        private final ExceptionMetadata metadata;
        private final DynamicContext context;

        private Cursor(
                RuntimeIterator childPlan,
                SequenceType sequenceType,
                RuntimeStaticContext staticContext,
                ExceptionMetadata metadata,
                DynamicContext context
        ) {
            this.childPlan = childPlan;
            this.sequenceType = sequenceType;
            this.staticContext = staticContext;
            this.metadata = metadata;
            this.context = context;
        }

        @Override
        protected Item materializeFirstItemOrNull() {
            return evaluate(this.childPlan, this.sequenceType, this.staticContext, this.metadata, this.context);
        }
    }

    static void checkInvalidCastable(Item item, ExceptionMetadata metadata, ItemType type) {
        // the target type cannot be xs:NOTATION, xs:anySimpleType, or xs:anyAtomicType
        // TODO: add support for xs:anySimpleType
        if (type.equals(BuiltinTypesCatalogue.NOTATIONItem)) {
            throw new CastableException("Invalid target type for castable expression: xs:NOTATION", metadata);
        }
        if (type.equals(BuiltinTypesCatalogue.atomicItem)) {
            throw new CastableException("Invalid target type for castable expression: xs:anyAtomicType", metadata);
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
