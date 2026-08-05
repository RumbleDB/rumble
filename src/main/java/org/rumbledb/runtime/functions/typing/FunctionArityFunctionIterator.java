package org.rumbledb.runtime.functions.typing;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.items.FunctionItem;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;

import java.io.Serial;
import java.math.BigInteger;
import java.util.List;

public class FunctionArityFunctionIterator extends AtMostOneItemLocalRuntimeIterator {
    @Serial
    private static final long serialVersionUID = 1L;

    public FunctionArityFunctionIterator(
            List<RuntimeIterator> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext context) {
        Item function = this.getChild(0).materializeFirstItemOrNull(context);
        if (function == null || !function.isFunction()) {
            throw new UnexpectedTypeException(
                    "The argument of fn:function-arity must be a single function item [err:XPTY0004].",
                    getMetadata()
            );
        }
        FunctionItem functionItem = (FunctionItem) function;

        return ItemFactory.getInstance().createIntegerItem(BigInteger.valueOf(functionItem.getParameterNames().size()));
    }
}
