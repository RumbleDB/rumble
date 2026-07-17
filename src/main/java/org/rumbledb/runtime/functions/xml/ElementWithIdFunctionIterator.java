package org.rumbledb.runtime.functions.xml;

import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.runtime.RuntimeIterator;

import java.util.List;

public class ElementWithIdFunctionIterator extends NodeIdentifierFunctionIterator {
    private static final long serialVersionUID = 1L;

    public ElementWithIdFunctionIterator(
            List<RuntimeIterator> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext, Lookup.ELEMENT_WITH_ID);
    }
}
