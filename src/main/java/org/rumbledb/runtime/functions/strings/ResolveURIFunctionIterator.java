package org.rumbledb.runtime.functions.strings;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.InvalidArgumentValueException;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;

import java.net.URI;
import java.util.List;

public class ResolveURIFunctionIterator extends AtMostOneItemLocalRuntimeIterator {

    private static final long serialVersionUID = 1L;

    public ResolveURIFunctionIterator(
            List<RuntimeIterator> children,
            RuntimeStaticContext staticContext
    ) {
        super(children, staticContext);
    }


    @Override
    public Item materializeFirstItemOrNull(DynamicContext context) {
        Item relative = this.children.get(0).materializeFirstItemOrNull(context);
        if (relative == null) {
            return null;
        }
        Item base;
        if (this.children.size() == 2) {
            base = this.children.get(1).materializeFirstItemOrNull(context);
        } else {
            base = ItemFactory.getInstance().createAnyURIItem(this.staticURI.toString());
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
            throw new InvalidArgumentValueException("Malformed URI: " + uri + " Cause: " + e.getMessage(), getMetadata());
        }
    }
}
