package org.rumbledb.runtime.functions.typing;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.items.FunctionItem;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;

import java.math.BigInteger;
import java.util.List;

public class FunctionArityFunctionIterator extends AtMostOneItemLocalRuntimeIterator {
    private static final long serialVersionUID = 1L;

    public FunctionArityFunctionIterator(
            List<RuntimeIterator> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext context) {
        FunctionItem functionItem = (FunctionItem) this.children.get(0).materializeFirstItemOrNull(context);

        return ItemFactory.getInstance().createIntegerItem(BigInteger.valueOf(functionItem.getParameterNames().size()));
    }
}
