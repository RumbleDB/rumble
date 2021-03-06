package org.rumbledb.runtime.typing;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.MoreThanOneItemException;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.types.ItemType;
import org.rumbledb.types.SequenceType;
import org.rumbledb.types.SequenceType.Arity;

import java.util.Collections;

public class AtMostOneItemTypePromotionIterator extends AtMostOneItemLocalRuntimeIterator {

    private static final long serialVersionUID = 1L;
    private final String exceptionMessage;
    private RuntimeIterator iterator;
    private SequenceType sequenceType;

    private ItemType itemType;

    public AtMostOneItemTypePromotionIterator(
            RuntimeIterator iterator,
            SequenceType sequenceType,
            String exceptionMessage,
            ExecutionMode executionMode,
            ExceptionMetadata iteratorMetadata
    ) {
        super(Collections.singletonList(iterator), executionMode, iteratorMetadata);
        this.exceptionMessage = exceptionMessage;
        this.iterator = iterator;
        this.sequenceType = sequenceType;
        this.itemType = this.sequenceType.getItemType();
        if (!executionMode.equals(ExecutionMode.LOCAL)) {
            throw new OurBadException(
                    "A promotion iterator should never be executed in parallel if the sequence type arity is 0, 1 or ?."
            );
        }
        if (
            !sequenceType.isEmptySequence()
                && !sequenceType.getArity().equals(Arity.One)
                && !sequenceType.getArity().equals(Arity.OneOrZero)
        ) {
            throw new OurBadException(
                    "A type promotion iterator was instantiated that can only output at most one item, but the sequence type is + or *."
            );
        }
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext context) {
        Item item = null;
        try {
            item = this.iterator.materializeAtMostOneItemOrNull(context);
        } catch (MoreThanOneItemException e) {
            throw new UnexpectedTypeException(
                    this.exceptionMessage
                        + "Expecting at most one item, but the value provided has at least two items.",
                    getMetadata()
            );

        }
        if (
            item == null && (this.sequenceType.getArity() == SequenceType.Arity.One)
        ) {
            throw new UnexpectedTypeException(
                    this.exceptionMessage
                        + "Expecting one item, but the value provided is the empty sequence.",
                    getMetadata()
            );
        }
        if (item == null) {
            return null;
        }

        if (!InstanceOfIterator.doesItemTypeMatchItem(this.itemType, item)) {
            item = checkTypePromotion(item);
        }
        return item;
    }

    private Item checkTypePromotion(Item item) {
        if (item.isFunction()) {
            return item;
        }
        if (!item.getDynamicType().canBePromotedTo(this.sequenceType.getItemType())) {
            throw new UnexpectedTypeException(
                    this.exceptionMessage
                        + item.getDynamicType().toString()
                        + " cannot be promoted to type "
                        + this.sequenceType
                        + ".",
                    getMetadata()
            );
        }
        Item result = CastIterator.castItemToType(item, this.sequenceType.getItemType(), getMetadata());
        if (result == null) {
            throw new OurBadException(
                    "We were not able to promote "
                        + item.getDynamicType()
                        + " to type "
                        + this.sequenceType.getItemType()
            );
        }
        return result;
    }

}
