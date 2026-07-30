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

import java.io.Serial;
import java.util.List;

public class FunctionNameFunctionIterator extends AtMostOneItemLocalRuntimeIterator {
    @Serial
    private static final long serialVersionUID = 1L;

    public FunctionNameFunctionIterator(
            List<RuntimeIterator> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
    }

    @Override
    public Item evaluateAtMostOne(DynamicContext context) {
        validateStaticType();
        return evaluate(this.getChild(0).materializeFirstOrNull(context));
    }

    private void validateStaticType() {
        if (!this.getChild(0).getStaticType().isSubtypeOf(SequenceType.createSequenceType("function"))) {
            throw new UnexpectedTypeException(
                    "fn:function-name expects a function item, found " + this.getChild(0).getStaticType(),
                    getMetadata()
            );
        }
    }

    private Item evaluate(Item functionItem) {
        if (!(functionItem instanceof FunctionItem function)) {
            throw new OurBadException("Expected argument to be of type function and not be null");
        }
        if (function.getIdentifier() == null || function.getIdentifier().getName() == null) {
            return null;
        }
        Name name = function.getIdentifier().getName();
        return ItemFactory.getInstance().createQNameItem(name);
    }
}
