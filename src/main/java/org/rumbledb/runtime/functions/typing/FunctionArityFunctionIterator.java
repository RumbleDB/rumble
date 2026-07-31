package org.rumbledb.runtime.functions.typing;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.items.FunctionItem;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AbstractAtMostOneItemRuntimePlan;
import org.rumbledb.runtime.plan.RuntimePlan;

import java.io.Serial;
import java.math.BigInteger;
import java.util.List;

public class FunctionArityFunctionIterator extends AbstractAtMostOneItemRuntimePlan {
    @Serial
    private static final long serialVersionUID = 1L;

    public FunctionArityFunctionIterator(
            List<RuntimePlan<Item>> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
    }

    @Override
    public Item evaluateAtMostOne(DynamicContext context) {
        return evaluate(this.getChild(0).materializeFirstOrNull(context));
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
