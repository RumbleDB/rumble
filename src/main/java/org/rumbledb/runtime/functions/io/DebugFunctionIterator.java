package org.rumbledb.runtime.functions.io;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.primary.VariableReferenceIterator;

import java.util.List;

public class DebugFunctionIterator extends AtMostOneItemLocalRuntimeIterator {
    private static final long serialVersionUID = 1L;

    public DebugFunctionIterator(
            List<RuntimeIterator> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext context) {
        VariableReferenceIterator varReference = (VariableReferenceIterator) this.children.get(0);
        List<Item> varValue = context.getVariableValues()
            .getLocalVariableValue(varReference.getVariableName(), this.getMetadata());
        StringBuilder sb = new StringBuilder();
        sb.append(" ");
        for (Item item : varValue) {
            sb.append(item.serialize());
        }
        System.out.println(
            "Variable: " + varReference.getVariableName() + sb
        );
        return null;
    }
}
