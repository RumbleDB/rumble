package org.rumbledb.runtime.functions.strings;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;

import java.util.List;

public class StaticBaseURIFunctionIterator extends AtMostOneItemLocalRuntimeIterator {

    private static final long serialVersionUID = 1L;

    public StaticBaseURIFunctionIterator(
            List<RuntimeIterator> children,
            ExecutionMode executionMode,
            ExceptionMetadata iteratorMetadata
    ) {
        super(children, executionMode, iteratorMetadata);
    }


    @Override
    public Item materializeFirstItemOrNull(DynamicContext context) {
        return ItemFactory.getInstance().createAnyURIItem(this.staticURI.toString());
    }
}
