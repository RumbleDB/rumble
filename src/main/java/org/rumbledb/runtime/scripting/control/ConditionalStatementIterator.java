package org.rumbledb.runtime.scripting.control;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.runtime.EffectiveBooleanValue;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.cursor.ComputedLocalCursor;
import org.rumbledb.runtime.cursor.LocalCursor;

import java.io.Serial;
import java.util.List;


public class ConditionalStatementIterator extends AtMostOneItemLocalRuntimeIterator {
    @Serial
    private static final long serialVersionUID = 1L;

    public ConditionalStatementIterator(List<RuntimeIterator> children, RuntimeStaticContext staticContext) {
        super(children, staticContext);
    }

    @Override
    public LocalCursor<Item> createLocalCursor(DynamicContext context) {
        return new ComputedLocalCursor<>(
                () -> {
                    RuntimeIterator selected = EffectiveBooleanValue.evaluate(this.getChild(0), context)
                        ? this.getChild(1)
                        : this.getChild(2);
                    selected.materialize(new DynamicContext(context));
                    return null;
                },
                getMetadata()
        );
    }

    private RuntimeIterator selectApplicableIterator(DynamicContext dynamicContext) {
        RuntimeIterator condition = this.getChild(0);
        boolean effectiveBooleanValue = condition.getEffectiveBooleanValue(dynamicContext);
        if (effectiveBooleanValue) {
            return this.getChild(1);
        } else {
            return this.getChild(2);
        }
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext dynamicContext) {
        RuntimeIterator selectedIterator = selectApplicableIterator(dynamicContext);
        DynamicContext childDynamicContext = new DynamicContext(dynamicContext);
        selectedIterator.materialize(childDynamicContext);
        return null;
    }
}
