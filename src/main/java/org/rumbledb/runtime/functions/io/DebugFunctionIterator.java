package org.rumbledb.runtime.functions.io;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.primary.VariableReferenceIterator;

import java.util.List;

public class DebugFunctionIterator extends AtMostOneItemLocalRuntimeIterator {

    public DebugFunctionIterator(
            List<RuntimeIterator> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext context) {
        VariableReferenceIterator varReference = (VariableReferenceIterator) this.children.get(0);
        System.out.println(
            context.getVariableValues().getLocalVariableValue(varReference.getVariableName(), this.getMetadata())
        );
        return null;
    }
}
