package org.rumbledb.runtime.functions.maps;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;

/**
 * W3C XPath/XQuery {@code map:entry}:
 * <ul>
 * <li>atomizes a single key into exactly one atomic value</li>
 * <li>materializes the value into a general sequence (possibly empty)</li>
 * <li>returns a map containing a single key/value binding</li>
 * </ul>
 */
public class MapEntryFunctionIterator extends AtMostOneItemLocalRuntimeIterator {

    @Serial
    private static final long serialVersionUID = 1L;

    private final RuntimeIterator keyIterator;
    private final RuntimeIterator valueIterator;

    public MapEntryFunctionIterator(
            List<RuntimeIterator> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
        this.keyIterator = arguments.get(0);
        this.valueIterator = arguments.get(1);
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext dynamicContext) {
        Item key = this.keyIterator.materializeFirstItemOrNull(dynamicContext);

        List<Item> valueSequence = new ArrayList<>();
        this.valueIterator.materialize(dynamicContext, valueSequence);

        return ItemFactory.getInstance()
            .createMapItem(
                key,
                valueSequence,
                this.getRuntimeStaticContext().isQuerySideEffecting()
            );
    }
}

