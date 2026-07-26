package org.rumbledb.runtime.functions.arrays;

import java.io.Serial;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.apache.spark.api.java.JavaRDD;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.ArrayIndexOutOfBoundsException;
import org.rumbledb.exceptions.MoreThanOneItemException;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.items.structured.JSoundDataFrame;
import org.rumbledb.runtime.HybridRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.cursor.ComputedLocalCursor;
import org.rumbledb.runtime.cursor.LocalCursor;

public class ArrayInsertBeforeFunctionIterator extends HybridRuntimeIterator {

    @Override
    public LocalCursor<Item> createLocalCursor(DynamicContext context) {
        return new ComputedLocalCursor<>(
                () -> computeResult(context),
                getMetadata()
        );
    }

    @Serial
    private static final long serialVersionUID = 1L;

    private final RuntimeIterator arrayIterator;
    private final RuntimeIterator positionIterator;
    private final RuntimeIterator memberIterator;

    public ArrayInsertBeforeFunctionIterator(
            List<RuntimeIterator> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
        if (arguments.size() != 3) {
            throw new OurBadException("array:insert-before must have exactly three arguments.");
        }
        this.arrayIterator = arguments.get(0);
        this.positionIterator = arguments.get(1);
        this.memberIterator = arguments.get(2);
    }

    private Item computeResult(DynamicContext context) {
        Item arrayItem = null;
        try {
            arrayItem = this.arrayIterator.materializeAtMostOne(context);
        } catch (MoreThanOneItemException e) {
            throw new UnexpectedTypeException(
                    "array:insert-before expects exactly one array argument.",
                    getMetadata()
            );
        }
        if (arrayItem == null) {
            return null;
        }

        if (!arrayItem.isArray()) {
            throw new UnexpectedTypeException(
                    "Type error; first argument to array:insert-before must be an array.",
                    getMetadata()
            );
        }

        Item positionItem = null;
        try {
            positionItem = this.positionIterator.materializeAtMostOne(context);
        } catch (MoreThanOneItemException e) {
            throw new UnexpectedTypeException(
                    "array:insert-before expects exactly one position argument.",
                    getMetadata()
            );
        }
        if (positionItem == null || !positionItem.isNumeric()) {
            throw new UnexpectedTypeException(
                    "Type error; position argument to array:insert-before must be numeric.",
                    getMetadata()
            );
        }

        BigInteger positionInteger;
        if (positionItem.isInteger()) {
            positionInteger = positionItem.castToIntegerValue();
        } else {
            positionInteger = BigInteger.valueOf(positionItem.castToIntValue());
        }

        int size = arrayItem.getSize();
        BigInteger min = BigInteger.ONE;
        BigInteger max = BigInteger.valueOf((long) size).add(BigInteger.ONE);
        if (positionInteger.compareTo(min) < 0 || positionInteger.compareTo(max) > 0) {
            throw new ArrayIndexOutOfBoundsException(
                    "Tried to insert at array index: "
                        + positionInteger
                        + ", of array with length: "
                        + size,
                    getMetadata()
            );
        }

        int insertIndex = positionInteger.intValue() - 1;
        List<Item> memberSequence = this.memberIterator.materialize(context);

        if (arrayItem.isArrayOfItems() && memberSequence.size() == 1) {
            List<Item> newItems = new ArrayList<>(size + 1);
            // add items before the insert index
            for (int i = 0; i < insertIndex; i++) {
                newItems.add(arrayItem.getItemAt(i));
            }
            // add the new item
            newItems.add(memberSequence.get(0));
            // add items after the insert index
            for (int i = insertIndex; i < size; i++) {
                newItems.add(arrayItem.getItemAt(i));
            }
            return ItemFactory.getInstance()
                .createArrayItem(newItems, this.getRuntimeStaticContext().isQuerySideEffecting());
        }
        List<List<Item>> newMemberSequences = new ArrayList<>(size + 1);
        for (int i = 0; i < insertIndex; i++) {
            newMemberSequences.add(arrayItem.getSequenceAt(i));
        }
        newMemberSequences.add(memberSequence);
        for (int i = insertIndex; i < size; i++) {
            newMemberSequences.add(arrayItem.getSequenceAt(i));
        }
        return ItemFactory.getInstance()
            .createSequenceArrayItem(newMemberSequences, this.getRuntimeStaticContext().isQuerySideEffecting());
    }

    @Override
    public JavaRDD<Item> getRDDAux(DynamicContext dynamicContext) {
        throw new OurBadException(
                "array:insert-before is currently supported only in local execution mode."
        );
    }

    @Override
    protected boolean implementsDataFrames() {
        return false;
    }

    @Override
    public JSoundDataFrame getDataFrame(DynamicContext dynamicContext) {
        throw new OurBadException(
                "array:insert-before is currently supported only in local execution mode."
        );
    }
}
