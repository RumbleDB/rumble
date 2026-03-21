package org.rumbledb.runtime.functions.arrays;

import org.apache.spark.api.java.JavaRDD;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.ArrayIndexOutOfBoundsException;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.exceptions.MoreThanOneItemException;
import org.rumbledb.exceptions.NoItemException;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.items.structured.JSoundDataFrame;
import org.rumbledb.runtime.HybridRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class ArrayInsertBeforeFunctionIterator extends HybridRuntimeIterator {

    private static final long serialVersionUID = 1L;

    private final RuntimeIterator arrayIterator;
    private final RuntimeIterator positionIterator;
    private final RuntimeIterator memberIterator;
    private Item resultItem;
    private boolean hasProducedResult;

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
        this.resultItem = null;
        this.hasProducedResult = false;
    }

    @Override
    protected void openLocal() {
        this.arrayIterator.open(this.currentDynamicContextForLocalExecution);
        this.positionIterator.open(this.currentDynamicContextForLocalExecution);
        this.memberIterator.open(this.currentDynamicContextForLocalExecution);
        initializeResult(this.currentDynamicContextForLocalExecution);
        this.hasNext = this.resultItem != null;
        this.hasProducedResult = false;
    }

    private void initializeResult(DynamicContext context) {
        Item arrayItem;
        try {
            arrayItem = this.arrayIterator.materializeExactlyOneItem(context);
        } catch (NoItemException e) {
            this.resultItem = null;
            return;
        } catch (MoreThanOneItemException e) {
            throw new UnexpectedTypeException(
                    "array:insert-before expects exactly one array argument.",
                    getMetadata()
            );
        }

        if (!arrayItem.isArray()) {
            throw new UnexpectedTypeException(
                    "Type error; first argument to array:insert-before must be an array.",
                    getMetadata()
            );
        }

        Item positionItem;
        try {
            positionItem = this.positionIterator.materializeExactlyOneItem(context);
        } catch (NoItemException e) {
            throw new UnexpectedTypeException(
                    "array:insert-before expects exactly one position argument.",
                    getMetadata()
            );
        } catch (MoreThanOneItemException e) {
            throw new UnexpectedTypeException(
                    "array:insert-before expects exactly one position argument.",
                    getMetadata()
            );
        }

        if (!positionItem.isNumeric()) {
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

        List<List<Item>> originalMemberSequences = arrayItem.getSequences();
        List<List<Item>> newMemberSequences = new ArrayList<>(size + 1);

        for (int i = 0; i < insertIndex && i < originalMemberSequences.size(); i++) {
            newMemberSequences.add(originalMemberSequences.get(i));
        }

        List<Item> memberSequence = this.memberIterator.materialize(context);
        newMemberSequences.add(memberSequence);

        for (int i = insertIndex; i < originalMemberSequences.size(); i++) {
            newMemberSequences.add(originalMemberSequences.get(i));
        }

        this.resultItem = ItemFactory.getInstance()
            .createArrayFromMemberSequences(newMemberSequences, false);
    }

    @Override
    protected boolean hasNextLocal() {
        return this.hasNext;
    }

    @Override
    protected Item nextLocal() {
        if (!this.hasNext || this.hasProducedResult) {
            throw new IteratorFlowException(RuntimeIterator.FLOW_EXCEPTION_MESSAGE, getMetadata());
        }
        this.hasProducedResult = true;
        this.hasNext = false;
        return this.resultItem;
    }

    @Override
    protected void resetLocal() {
        this.arrayIterator.reset(this.currentDynamicContextForLocalExecution);
        this.positionIterator.reset(this.currentDynamicContextForLocalExecution);
        this.memberIterator.reset(this.currentDynamicContextForLocalExecution);
        initializeResult(this.currentDynamicContextForLocalExecution);
        this.hasNext = this.resultItem != null;
        this.hasProducedResult = false;
    }

    @Override
    protected void closeLocal() {
        if (this.arrayIterator.isOpen()) {
            this.arrayIterator.close();
        }
        if (this.positionIterator.isOpen()) {
            this.positionIterator.close();
        }
        if (this.memberIterator.isOpen()) {
            this.memberIterator.close();
        }
        this.resultItem = null;
        this.hasProducedResult = false;
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

