package org.rumbledb.runtime.functions.io;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;

import java.util.List;

public class DebugFunctionIterator extends AtMostOneItemLocalRuntimeIterator {
    private final Name localVariable;

    public DebugFunctionIterator(
            List<RuntimeIterator> arguments,
            RuntimeStaticContext staticContext,
            Name localVariable
    ) {
        super(arguments, staticContext);
        this.localVariable = localVariable;
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext context) {
        System.out.println(context.getVariableValues().getLocalVariableValue(this.localVariable, this.getMetadata()));
        return null;
    }
}
