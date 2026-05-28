package org.rumbledb.runtime.functions.typing;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.items.FunctionItem;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.types.SequenceType;

import java.util.List;

public class FunctionNameFunctionIterator extends AtMostOneItemLocalRuntimeIterator {
    private static final long serialVersionUID = 1L;

    public FunctionNameFunctionIterator(
            List<RuntimeIterator> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext context) {
        RuntimeIterator functionIterator = this.children.get(0);
        /*
         * TODO remove...
         * Currently used for debugging, this guard fails when given an if statement
         */
        if (!functionIterator.getStaticType().isSubtypeOf(SequenceType.createSequenceType("function"))) {
            throw new UnexpectedTypeException(
                    "fn:function-name expects a function item, found " + functionIterator.getStaticType(),
                    getMetadata()
            );
        }
        System.err.println("Item is of type function");
        Item functionItem = functionIterator.materializeFirstItemOrNull(context);
        if (functionItem == null || !(functionItem instanceof FunctionItem)) {
            throw new OurBadException("Expected argument to be of type function and not be null");
        }
        FunctionItem function = (FunctionItem) functionItem;
        if (function.getIdentifier() == null || function.getIdentifier().getName() == null) {
            return null;
        }
        Name name = function.getIdentifier().getName();
        return ItemFactory.getInstance().createQNameItem(name);
    }
}
