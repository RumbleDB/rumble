package org.rumbledb.runtime.scripting.loops;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.ContinueStatementException;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;

public class ContinueStatementIterator extends AtMostOneItemLocalRuntimeIterator {
    public ContinueStatementIterator(RuntimeStaticContext staticContext) {
        super(null, staticContext);
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext context) {
        throw new ContinueStatementException();
    }
}
