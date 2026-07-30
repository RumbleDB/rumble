package org.rumbledb.runtime.functions.strings;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.functions.input.FileSystemUtil;

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
        if (URI.create(stringRelative).isAbsolute()) {
            return ItemFactory.getInstance().createAnyURIItem(stringRelative);
        }

        URI uri = URI.create(base.getStringValue());
        String stringURI = FileSystemUtil.resolveURI(uri, stringRelative, getMetadata()).toString();

        return ItemFactory.getInstance().createAnyURIItem(stringURI);
    }
}
