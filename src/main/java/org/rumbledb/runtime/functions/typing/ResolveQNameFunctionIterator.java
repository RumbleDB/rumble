package org.rumbledb.runtime.functions.typing;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.UnimplementedFunctionException;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.cursor.ComputedLocalCursor;
import org.rumbledb.runtime.cursor.LocalCursor;

import java.io.Serial;
import java.util.List;

public class ResolveQNameFunctionIterator extends AtMostOneItemLocalRuntimeIterator {
    @Serial
    private static final long serialVersionUID = 1L;

    public ResolveQNameFunctionIterator(
            List<RuntimeIterator> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
    }

    @Override
    public LocalCursor<Item> createLocalCursor(DynamicContext context) {
        return new ComputedLocalCursor<>(
                () -> {
                    throw new UnimplementedFunctionException("fn:resolve-QName", getMetadata());
                },
                getMetadata()
        );
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext context) {
        throw new UnimplementedFunctionException("fn:resolve-QName", getMetadata());
    }
}
