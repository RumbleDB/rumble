package org.rumbledb.runtime.functions.strings;

import org.rumbledb.runtime.plan.EvaluationArguments;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.InvalidArgumentValueException;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;

import java.io.Serial;
import java.net.URI;
import java.util.List;

public class ResolveURIFunctionIterator extends AtMostOneItemLocalRuntimeIterator {

    @Serial
    private static final long serialVersionUID = 1L;

    public ResolveURIFunctionIterator(
            List<RuntimeIterator> children,
            RuntimeStaticContext staticContext
    ) {
        super(children, staticContext);
    }

    @Override
    public Item evaluateAtMostOne(DynamicContext context) {
        return evaluate(
            EvaluationArguments.lazy(
                this.getChildren().size(),
                index -> this.getChild(index).materializeFirstItemOrNull(context)
            )
        );
    }

    private Item evaluate(EvaluationArguments<Item> arguments) {
        Item relative = arguments.get(0);
        if (relative == null) {
            return null;
        }
        Item base;
        if (arguments.size() == 2) {
            base = arguments.get(1);
        } else {
            base = ItemFactory.getInstance().createAnyURIItem(this.staticContext.getStaticURI().toString());
        }
        if (base == null) {
            return null;
        }
        String stringRelative = relative.getStringValue();
        URI relativeURI = parseURI(stringRelative);
        if (relativeURI.isAbsolute()) {
            return ItemFactory.getInstance().createAnyURIItem(stringRelative);
        }

        URI uri = parseURI(base.getStringValue());
        String stringURI = uri.resolve(relativeURI).toString();

        return ItemFactory.getInstance().createAnyURIItem(stringURI);
    }

    private URI parseURI(String uri) {
        try {
            return URI.create(uri);
        } catch (IllegalArgumentException e) {
            throw new InvalidArgumentValueException(
                    "Malformed URI: " + uri + " Cause: " + e.getMessage(),
                    getMetadata()
            );
        }
    }
}
