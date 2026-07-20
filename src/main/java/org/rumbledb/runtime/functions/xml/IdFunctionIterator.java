package org.rumbledb.runtime.functions.xml;

import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.runtime.RuntimeIterator;

import java.util.List;

public class IdFunctionIterator extends NodeIdentifierFunctionIterator {
    private static final long serialVersionUID = 1L;

    public IdFunctionIterator(
            List<RuntimeIterator> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext, Lookup.ID);
    }
}
