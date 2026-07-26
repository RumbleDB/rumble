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
import org.rumbledb.runtime.cursor.IteratorLocalCursor;
import org.rumbledb.runtime.cursor.LocalCursor;
import org.rumbledb.runtime.cursor.LocalCursorUtils;

import java.io.Serial;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class ArrayHeadFunctionIterator extends HybridRuntimeIterator {

    @Serial
    private static final long serialVersionUID = 1L;

    private final RuntimeIterator arrayIterator;
    private Queue<Item> pendingResults;

    public ArrayHeadFunctionIterator(
            List<RuntimeIterator> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
        if (arguments.size() != 1) {
            throw new OurBadException("array:head must have exactly one argument.");
        }
        this.arrayIterator = arguments.get(0);
        this.pendingResults = new LinkedList<>();
    }

    @Override
    public LocalCursor<Item> createLocalCursor(DynamicContext context) {
        return new IteratorLocalCursor<>(
                () -> headArgument(LocalCursorUtils.materialize(this.arrayIterator, context)).iterator(),
                getMetadata()
        );
    }

    @Override
    protected void openLocal() {
        this.arrayIterator.open(this.currentDynamicContextForLocalExecution);
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
                    "array:head expects exactly one array argument.",
                    getMetadata()
            );
        }

        this.pendingResults.addAll(head(arrayItem));
    }

    private List<Item> head(Item arrayItem) {
        if (arrayItem == null) {
            return List.of();
        }
        if (!arrayItem.isArray()) {
            throw new UnexpectedTypeException(
                    "Type error; argument to array:head must be an array.",
                    getMetadata()
            );
        }

        int size = arrayItem.getSize();
        if (size == 0) {
            throw new ArrayIndexOutOfBoundsException(
                    "array:head called on an empty array.",
                    getMetadata()
            );
        }

        if (arrayItem.isArrayOfItems()) {
            return List.of(arrayItem.getItemAt(0));
        }
        return arrayItem.getSequenceAt(0);
    }

    private List<Item> headArgument(List<Item> items) {
        if (items.size() > 1) {
            throw new UnexpectedTypeException(
                    "array:head expects exactly one array argument.",
                    getMetadata()
            );
        }
        return head(items.isEmpty() ? null : items.get(0));
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
        this.hasNext = !this.pendingResults.isEmpty();
    }

    @Override
    protected void closeLocal() {
        if (this.arrayIterator.isOpen()) {
            this.arrayIterator.close();
        }
        this.pendingResults.clear();
    }

    @Override
    public JavaRDD<Item> getRDDAux(DynamicContext dynamicContext) {
        throw new OurBadException(
                "array:head is currently supported only in local execution mode."
        );
    }

    @Override
    protected boolean implementsDataFrames() {
        return false;
    }

    @Override
    public JSoundDataFrame getDataFrame(DynamicContext dynamicContext) {
        throw new OurBadException(
                "array:head is currently supported only in local execution mode."
        );
    }
}
