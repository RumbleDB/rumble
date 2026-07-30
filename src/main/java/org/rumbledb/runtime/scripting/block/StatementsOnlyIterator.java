package org.rumbledb.runtime.scripting.block;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.plan.RuntimePlan;

import java.io.Serial;
import java.util.List;

public class StatementsOnlyIterator extends AtMostOneItemLocalRuntimeIterator {
    @Serial
    private static final long serialVersionUID = 1L;

    public StatementsOnlyIterator(List<RuntimeIterator> children, RuntimeStaticContext staticContext) {
        super(
            children,
            staticContext.toBuilder().isSequential(children.stream().anyMatch(RuntimeIterator::isSequential)).build()
        );
    }

    @Override
    public Item evaluateAtMostOne(DynamicContext dynamicContext) {
        for (RuntimePlan<Item> statement : this.getChildren()) {
            statement.materialize(dynamicContext);
        }
        return null;
    }
}
