package org.rumbledb.runtime.scripting.declaration;

import java.io.Serial;
import java.util.List;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.runtime.AbstractAtMostOneItemRuntimePlan;
import org.rumbledb.runtime.plan.ItemRuntimePlan;

/*
 * It is expected that no results are returned for this iterator.
 */
public class CommaVariableDeclStatementIterator extends AbstractAtMostOneItemRuntimePlan {
    @Serial
    private static final long serialVersionUID = 1L;

    public CommaVariableDeclStatementIterator(
            List<? extends ItemRuntimePlan> children, RuntimeStaticContext staticContext) {
        super(children, staticContext);
    }

    @Override
    public Item evaluateAtMostOne(DynamicContext context) {
        for (ItemRuntimePlan child : this.getChildren()) {
            child.materialize(context);
        }
        return null;
    }
}
