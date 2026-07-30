package org.rumbledb.runtime.functions.typing;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.items.FunctionItem;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.cursor.ContextOrArgumentLocalCursor;
import org.rumbledb.runtime.cursor.Cursor;

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
    public Cursor<Item> createNativeCursor(DynamicContext context) {
        return ContextOrArgumentLocalCursor.mapArgument(
            this.getChild(0),
            context,
            this::evaluate,
            getMetadata()
        );
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext context) {
        return evaluate(this.getChild(0).materializeFirstItemOrNull(context));
    }

    private Item evaluate(Item function) {
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
