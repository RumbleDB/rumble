package org.rumbledb.runtime.functions.context;

import org.rumbledb.runtime.plan.ItemRuntimePlan;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.cursor.IteratorLocalCursor;
import org.rumbledb.runtime.cursor.Cursor;
import org.rumbledb.runtime.functions.base.LocalFunctionCallIterator;

import java.io.Serial;
import java.util.List;

public class AvailableEnvironmentVariablesFunctionIterator extends LocalFunctionCallIterator {
    @Serial
    private static final long serialVersionUID = 1L;

    public AvailableEnvironmentVariablesFunctionIterator(
            List<ItemRuntimePlan> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
    }

    @Override
    public Cursor<Item> createNativeCursor(DynamicContext context) {
        return new IteratorLocalCursor<>(
                () -> System.getenv()
                    .keySet()
                    .stream()
                    .map(ItemFactory.getInstance()::createStringItem)
                    .iterator(),
                getMetadata()
        );
    }

}
