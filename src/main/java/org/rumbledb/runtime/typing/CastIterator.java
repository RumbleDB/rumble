package org.rumbledb.runtime.typing;

import org.apache.commons.codec.binary.Hex;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.exceptions.CastException;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.exceptions.MoreThanOneItemException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.LocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.types.ItemType;
import org.rumbledb.types.SequenceType;
import org.rumbledb.types.SequenceType.Arity;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collections;


public class CastIterator extends LocalRuntimeIterator {
    private static final long serialVersionUID = 1L;
    private final RuntimeIterator child;
    private final SequenceType sequenceType;
    private Item item;

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
        if (!this.hasNext) {
            throw new IteratorFlowException(RuntimeIterator.FLOW_EXCEPTION_MESSAGE, getMetadata());
        }
        this.hasNext = false;

        return castItemToType(item, this.sequenceType.getItemType(), getMetadata());
    }

    @Override
    public void open(DynamicContext context) {
        super.open(context);
        try {
            item = this.child.materializeAtMostOneItemOrNull(this.currentDynamicContextForLocalExecution);
        } catch (MoreThanOneItemException e) {
            throw new UnexpectedTypeException(
                    " Sequence of more than one item can not be treated as type "
                        + this.sequenceType.toString(),
                    getMetadata()
            );
        }
        if (item == null && !this.sequenceType.isEmptySequence() && this.sequenceType.getArity() != Arity.OneOrZero) {
            throw new UnexpectedTypeException(
                    " Empty sequence can not be cast to type with quantifier '1'",
                    getMetadata()
            );
        }
        this.hasNext = item != null;
    }

    public static Item castItemToType(Item item, ItemType targetType, ExceptionMetadata metadata) {
        String itemType = item.getDynamicType().toString();

        if (itemType.equals(targetType.toString())) {
            return item;
        }

        if (targetType.equals(ItemType.stringItem)) {
            return ItemFactory.getInstance().createStringItem(item.getStringValue());
        }

        if (targetType.equals(ItemType.booleanItem)) {
        }

        if (targetType.equals(ItemType.doubleItem)) {
            if (item.isBoolean()) {
                return ItemFactory.getInstance().createDoubleItem(item.getBooleanValue() ? 1 : 0);
            }
        }

        if (targetType.equals(ItemType.decimalItem)) {
            if (item.isBoolean()) {
                return ItemFactory.getInstance()
                    .createDecimalItem(item.getBooleanValue() ? BigDecimal.ONE : BigDecimal.ZERO);
            }
        }

        if (targetType.equals(ItemType.integerItem)) {
            if (item.isBoolean()) {
                return ItemFactory.getInstance()
                    .createIntegerItem(item.getBooleanValue() ? BigInteger.ONE : BigInteger.ZERO);
            }
        }

        if (targetType.equals(ItemType.anyURIItem)) {
            if (item.isString()) {
                return ItemFactory.getInstance().createAnyURIItem(item.getStringValue());
            }
        }

        if (targetType.equals(ItemType.base64BinaryItem)) {
            if (item.isString()) {
                return ItemFactory.getInstance().createBase64BinaryItem(item.getStringValue());
            }
        }

        if (targetType.equals(ItemType.hexBinaryItem)) {
            if (item.isString()) {
                return ItemFactory.getInstance().createHexBinaryItem(item.getStringValue());
            }
            if (item.isBase64Binary()) {
                return ItemFactory.getInstance().createHexBinaryItem(Hex.encodeHexString(item.getBinaryValue()));
            }
        }

        String message = String.format(
            "\"%s\": value of type %s is not castable to type %s",
            item.serialize(),
            itemType,
            targetType
        );

        throw new CastException(message, metadata);
    }
}
