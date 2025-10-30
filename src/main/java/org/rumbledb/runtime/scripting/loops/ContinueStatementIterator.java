package org.rumbledb.runtime.scripting.loops;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.ContinueStatementException;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;

public class ContinueStatementIterator extends AtMostOneItemLocalRuntimeIterator {
    private static final long serialVersionUID = 1L;
    public ContinueStatementIterator(RuntimeStaticContext staticContext) {
        super(null, staticContext);
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext context) {
        throw new ContinueStatementException();
    }
}
