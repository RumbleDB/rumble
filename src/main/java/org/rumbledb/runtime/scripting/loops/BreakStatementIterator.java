package org.rumbledb.runtime.scripting.loops;

import java.io.Serial;
import java.util.List;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.BreakStatementException;
import org.rumbledb.runtime.AbstractAtMostOneItemRuntimePlan;

public class BreakStatementIterator extends AbstractAtMostOneItemRuntimePlan {
    @Serial
    private static final long serialVersionUID = 1L;

    public BreakStatementIterator(RuntimeStaticContext staticContext) {
        super(List.of(), staticContext);
    }

    @Override
    public Item evaluateAtMostOne(DynamicContext context) {
        throw new BreakStatementException();
    }
}
