package org.rumbledb.runtime.functions.arrays;

import org.apache.spark.api.java.JavaRDD;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.*;
import org.rumbledb.items.structured.JSoundDataFrame;
import org.rumbledb.exceptions.ArrayIndexOutOfBoundsException;
import org.rumbledb.runtime.HybridRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;

import java.util.LinkedList;
import java.util.Queue;

public class ArrayFunctionCallIterator extends HybridRuntimeIterator {

    private static final long serialVersionUID = 1L;

    private final Item arrayItem;
    private final RuntimeIterator indexIterator;
    private Queue<Item> pendingResults;

    public ArrayFunctionCallIterator(
            Item arrayItem,
            RuntimeIterator indexIterator,
            RuntimeStaticContext staticContext
    ) {
        super(indexIterator == null ? null : java.util.Collections.singletonList(indexIterator), staticContext);
        this.arrayItem = arrayItem;
        this.indexIterator = indexIterator;
        this.pendingResults = new LinkedList<>();
    }

    @Override
    protected void openLocal() {
        if (this.indexIterator == null) {
            throw new UnexpectedTypeException(
                    "Array function calls must have exactly one argument.",
                    getMetadata()
            );
        }
        this.indexIterator.open(this.currentDynamicContextForLocalExecution);
        initializeResults(this.currentDynamicContextForLocalExecution);
        setNextResult();
    }

    private void initializeResults(DynamicContext context) {
        this.pendingResults.clear();
        Item lookupExpression;
        try {
            lookupExpression = this.indexIterator.materializeExactlyOneItem(context);
        } catch (NoItemException e) {
            throw new InvalidSelectorException(
                    "Invalid array function call; array lookup can't be performed with no key.",
                    getMetadata()
            );
        } catch (MoreThanOneItemException e) {
            throw new InvalidSelectorException(
                    "Invalid array function call; array lookup can't be performed with multiple keys.",
                    getMetadata()
            );
        }
        if (!lookupExpression.isNumeric()) {
            throw new UnexpectedTypeException(
                    "Type error; non numeric array lookup for : " + lookupExpression.serialize(),
                    getMetadata()
            );
        }
        int lookup = lookupExpression.castToIntValue();
        if (!this.arrayItem.isArray()) {
            throw new UnexpectedTypeException(
                    "Array function calls can only be performed on arrays.",
                    getMetadata()
            );
        }
        if (lookup <= 0) {
            // 1-based positions: anything < 1 is out of bounds and should raise FOAY0001
            throw new ArrayIndexOutOfBoundsException(
                    "Tried to access array index: "
                        + lookup
                        + ", of array with length: "
                        + this.arrayItem.getSize(),
                    getMetadata()
            );
        }
        if (this.arrayItem.isArrayOfItems()) {
            Item member = this.arrayItem.getItemAt(lookup - 1);
            this.pendingResults.add(member);
        } else {
            java.util.List<Item> memberSeq = this.arrayItem.getSequenceAt(lookup - 1);
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
        if (this.indexIterator != null) {
            this.indexIterator.reset(this.currentDynamicContextForLocalExecution);
        }
        initializeResults(this.currentDynamicContextForLocalExecution);
        setNextResult();
    }

    @Override
    protected void closeLocal() {
        if (this.indexIterator != null && this.indexIterator.isOpen()) {
            this.indexIterator.close();
        }
        this.pendingResults.clear();
    }

    @Override
    public JavaRDD<Item> getRDDAux(DynamicContext dynamicContext) {
        throw new OurBadException(
                "Array function calls are currently supported only in local execution mode."
        );
    }

    @Override
    protected boolean implementsDataFrames() {
        return false;
    }

    @Override
    public JSoundDataFrame getDataFrame(DynamicContext dynamicContext) {
        throw new OurBadException(
                "Array function calls are currently supported only in local execution mode."
        );
    }
}

