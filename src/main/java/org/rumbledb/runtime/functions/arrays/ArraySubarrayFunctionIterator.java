package org.rumbledb.runtime.functions.arrays;

import org.apache.spark.api.java.JavaRDD;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.ArrayIndexOutOfBoundsException;
import org.rumbledb.exceptions.ArrayInvalidSubarrayLengthException;
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

public class ArraySubarrayFunctionIterator extends HybridRuntimeIterator {

    private static final long serialVersionUID = 1L;

    private final RuntimeIterator arrayIterator;
    private final RuntimeIterator startIterator;
    private final RuntimeIterator lengthIterator;

    private Item resultItem;
    private boolean hasProducedResult;

    public ArraySubarrayFunctionIterator(
            List<RuntimeIterator> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
        if (arguments.size() != 2 && arguments.size() != 3) {
            throw new OurBadException("array:subarray must have either two or three arguments.");
        }
        this.arrayIterator = arguments.get(0);
        this.startIterator = arguments.get(1);
        this.lengthIterator = arguments.size() == 3 ? arguments.get(2) : null;
        this.resultItem = null;
        this.hasProducedResult = false;
    }

    @Override
    protected void openLocal() {
        this.arrayIterator.open(this.currentDynamicContextForLocalExecution);
        this.startIterator.open(this.currentDynamicContextForLocalExecution);
        if (this.lengthIterator != null) {
            this.lengthIterator.open(this.currentDynamicContextForLocalExecution);
        }
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
                    "array:subarray expects exactly one array argument.",
                    getMetadata()
            );
        }

        if (!arrayItem.isArray()) {
            throw new UnexpectedTypeException(
                    "Type error; first argument to array:subarray must be an array.",
                    getMetadata()
            );
        }

        BigInteger start = materializeIntegerArgument(context, this.startIterator, "start");

        int size = arrayItem.getSize();
        BigInteger min = BigInteger.ONE;
        BigInteger max = BigInteger.valueOf((long) size).add(BigInteger.ONE);
        if (start.compareTo(min) < 0 || start.compareTo(max) > 0) {
            throw new ArrayIndexOutOfBoundsException(
                    "Tried to access array index: " + start + ", of array with length: " + size,
                    getMetadata()
            );
        }

        BigInteger length;
        if (this.lengthIterator == null) {
            length = BigInteger.valueOf((long) size).subtract(start).add(BigInteger.ONE);
        } else {
            length = materializeIntegerArgument(context, this.lengthIterator, "length");
            if (length.compareTo(BigInteger.ZERO) < 0) {
                throw new ArrayInvalidSubarrayLengthException(
                        "array:subarray length must be non-negative. Found: " + length,
                        getMetadata()
                );
            }
        }

        if (start.add(length).compareTo(BigInteger.valueOf((long) size).add(BigInteger.ONE)) > 0) {
            throw new ArrayIndexOutOfBoundsException(
                    "array:subarray start + length is out of bounds: start="
                        + start
                        + ", length="
                        + length
                        + ", array length="
                        + size,
                    getMetadata()
            );
        }

        int fromIndex = start.intValue() - 1;
        int toIndex = fromIndex + length.intValue();

        List<List<Item>> memberSequences = arrayItem.getMemberSequences();
        List<List<Item>> slicedMemberSequences = new ArrayList<>(Math.max(0, toIndex - fromIndex));
        for (int i = fromIndex; i < toIndex; i++) {
            slicedMemberSequences.add(memberSequences.get(i));
        }

        this.resultItem = ItemFactory.getInstance()
            .createArrayFromMemberSequences(slicedMemberSequences, false);
    }

    private BigInteger materializeIntegerArgument(DynamicContext context, RuntimeIterator iterator, String label) {
        Item item;
        try {
            item = iterator.materializeExactlyOneItem(context);
        } catch (NoItemException | MoreThanOneItemException e) {
            throw new UnexpectedTypeException(
                    "array:subarray expects exactly one " + label + " argument.",
                    getMetadata()
            );
        }

        if (!item.isNumeric()) {
            throw new UnexpectedTypeException(
                    "Type error; " + label + " argument to array:subarray must be numeric.",
                    getMetadata()
            );
        }

        if (item.isInteger()) {
            return item.castToIntegerValue();
        }
        return BigInteger.valueOf(item.castToIntValue());
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
        this.startIterator.reset(this.currentDynamicContextForLocalExecution);
        if (this.lengthIterator != null) {
            this.lengthIterator.reset(this.currentDynamicContextForLocalExecution);
        }
        initializeResult(this.currentDynamicContextForLocalExecution);
        this.hasNext = this.resultItem != null;
        this.hasProducedResult = false;
    }

    @Override
    protected void closeLocal() {
        if (this.arrayIterator.isOpen()) {
            this.arrayIterator.close();
        }
        if (this.startIterator.isOpen()) {
            this.startIterator.close();
        }
        if (this.lengthIterator != null && this.lengthIterator.isOpen()) {
            this.lengthIterator.close();
        }
        this.resultItem = null;
        this.hasProducedResult = false;
    }

    @Override
    public JavaRDD<Item> getRDDAux(DynamicContext dynamicContext) {
        throw new OurBadException(
                "array:subarray is currently supported only in local execution mode."
        );
    }

    @Override
    protected boolean implementsDataFrames() {
        return false;
    }

    @Override
    public JSoundDataFrame getDataFrame(DynamicContext dynamicContext) {
        throw new OurBadException(
                "array:subarray is currently supported only in local execution mode."
        );
    }
}

