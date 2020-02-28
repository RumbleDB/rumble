package org.rumbledb.runtime.functions.numerics;

import org.rumbledb.api.Item;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.items.AtomicItem;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.functions.base.LocalFunctionCallIterator;
import sparksoniq.jsoniq.ExecutionMode;
import sparksoniq.semantics.types.AtomicTypes;

import java.util.List;

public class NumberFunctionIterator extends LocalFunctionCallIterator {

    private static final long serialVersionUID = 1L;

    public NumberFunctionIterator(
            List<RuntimeIterator> parameters,
            ExecutionMode executionMode,
            ExceptionMetadata iteratorMetadata
    ) {
        super(parameters, executionMode, iteratorMetadata);
    }

    @Override
    public Item next() {
        if (this.hasNext) {
            this.hasNext = false;
            Item anyItem = this.children.get(0).materializeFirstItemOrNull(this.currentDynamicContextForLocalExecution);
            if (anyItem == null) {
                return ItemFactory.getInstance().createDoubleItem(Double.NaN);
            }

            AtomicItem atomicItem = (AtomicItem) anyItem;
            if (atomicItem.isCastableAs(AtomicTypes.DoubleItem)) {
                try {
                    return atomicItem.castAs(AtomicTypes.DoubleItem);
                } catch (ClassCastException e) {
                    return ItemFactory.getInstance().createDoubleItem(Double.NaN);
                }
            }
            return ItemFactory.getInstance().createDoubleItem(Double.NaN);
        } else
            throw new IteratorFlowException(
                    RuntimeIterator.FLOW_EXCEPTION_MESSAGE + " number function",
                    getMetadata()
            );
    }
}
