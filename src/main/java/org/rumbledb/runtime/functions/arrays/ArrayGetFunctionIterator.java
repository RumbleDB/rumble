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
import org.rumbledb.items.structured.JSoundDataFrame;
import org.rumbledb.runtime.HybridRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class ArrayGetFunctionIterator extends HybridRuntimeIterator {

    private static final long serialVersionUID = 1L;

    private final RuntimeIterator arrayIterator;
    private final RuntimeIterator positionIterator;
    private Queue<Item> pendingResults;

    public ArrayGetFunctionIterator(
            List<RuntimeIterator> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
        if (arguments.size() != 2) {
            throw new OurBadException("array:get must have exactly two arguments.");
        }
        this.arrayIterator = arguments.get(0);
        this.positionIterator = arguments.get(1);
        this.pendingResults = new LinkedList<>();
    }

    @Override
    protected void openLocal() {
        this.arrayIterator.open(this.currentDynamicContextForLocalExecution);
        this.positionIterator.open(this.currentDynamicContextForLocalExecution);
        initializeResults(this.currentDynamicContextForLocalExecution);
        setNextResult();
    }

    private void initializeResults(DynamicContext context) {
        this.pendingResults.clear();

        Item arrayItem;
        try {
            arrayItem = this.arrayIterator.materializeExactlyOneItem(context);
        } catch (NoItemException e) {
            return;
        } catch (MoreThanOneItemException e) {
            throw new UnexpectedTypeException(
                    "array:get expects exactly one array argument.",
                    getMetadata()
            );
        }

        if (!arrayItem.isArray()) {
            throw new UnexpectedTypeException(
                    "Type error; first argument to array:get must be an array.",
                    getMetadata()
            );
        }

        Item positionItem;
        try {
            positionItem = this.positionIterator.materializeExactlyOneItem(context);
        } catch (NoItemException e) {
            throw new UnexpectedTypeException(
                    "array:get expects exactly one position argument.",
                    getMetadata()
            );
        } catch (MoreThanOneItemException e) {
            throw new UnexpectedTypeException(
                    "array:get expects exactly one position argument.",
                    getMetadata()
            );
        }

        if (!positionItem.isNumeric()) {
            throw new UnexpectedTypeException(
                    "Type error; position argument to array:get must be numeric.",
                    getMetadata()
            );
        }

        java.math.BigInteger positionInteger;
        if (positionItem.isInteger()) {
            positionInteger = positionItem.castToIntegerValue();
        } else {
            positionInteger = java.math.BigInteger.valueOf(positionItem.castToIntValue());
        }

        if (
            positionInteger.compareTo(java.math.BigInteger.ONE) < 0
                || positionInteger.compareTo(java.math.BigInteger.valueOf(arrayItem.getSize())) > 0
        ) {
            throw new ArrayIndexOutOfBoundsException(
                    "Tried to access array index: "
                        + positionInteger
                        + ", of array with length: "
                        + arrayItem.getSize(),
                    getMetadata()
            );
        }

        int lookup = positionInteger.intValue();

        if (arrayItem.isJSONArray()) {
            Item member = arrayItem.getItemAt(lookup - 1);
            this.pendingResults.add(member);
        } else {
            List<Item> memberSeq = arrayItem.getSequenceAt(lookup - 1);
            this.pendingResults.addAll(memberSeq);
        }
    }

    @Override
    protected boolean hasNextLocal() {
        return this.hasNext;
    }

    @Override
    protected Item nextLocal() {
        if (!this.hasNext) {
            throw new IteratorFlowException(RuntimeIterator.FLOW_EXCEPTION_MESSAGE, getMetadata());
        }
        Item result = this.pendingResults.remove();
        setNextResult();
        return result;
    }

    private void setNextResult() {
        if (this.pendingResults.isEmpty()) {
            this.hasNext = false;
        } else {
            this.hasNext = true;
        }
    }

    @Override
    protected void resetLocal() {
        this.arrayIterator.reset(this.currentDynamicContextForLocalExecution);
        this.positionIterator.reset(this.currentDynamicContextForLocalExecution);
        initializeResults(this.currentDynamicContextForLocalExecution);
        setNextResult();
    }

    @Override
    protected void closeLocal() {
        if (this.arrayIterator.isOpen()) {
            this.arrayIterator.close();
        }
        if (this.positionIterator.isOpen()) {
            this.positionIterator.close();
        }
        this.pendingResults.clear();
    }

    @Override
    public JavaRDD<Item> getRDDAux(DynamicContext dynamicContext) {
        throw new OurBadException(
                "array:get is currently supported only in local execution mode."
        );
    }

    @Override
    protected boolean implementsDataFrames() {
        return false;
    }

    @Override
    public JSoundDataFrame getDataFrame(DynamicContext dynamicContext) {
        throw new OurBadException(
                "array:get is currently supported only in local execution mode."
        );
    }
}

