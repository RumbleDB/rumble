package org.rumbledb.runtime.functions.strings;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.InvalidArgumentValueException;
import org.rumbledb.exceptions.StaticBaseURINotSetException;
import org.rumbledb.exceptions.URIResolutionException;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.functions.URIUtils;

import java.net.URI;
import java.net.URISyntaxException;
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
        String stringRelative = relative.getStringValue();
        URI relativeURI = parseURI(stringRelative);
        if (relativeURI.isAbsolute()) {
            return ItemFactory.getInstance().createAnyURIItem(stringRelative);
        }

        URI baseURI;
        if (this.children.size() == 2) {
            Item base = this.children.get(1).materializeFirstItemOrNull(context);
            baseURI = parseBaseURI(base.getStringValue());
        } else {
            if (this.staticURI == null) {
                throw new StaticBaseURINotSetException(
                        "The static base URI is absent in fn:resolve-uri().",
                        getMetadata()
                );
            }
            baseURI = parseBaseURI(this.staticURI.toString());
        }

        String stringURI;
        try {
            stringURI = URIUtils.resolveURIReference(baseURI, relativeURI).toString();
        } catch (URISyntaxException e) {
            URIResolutionException ex = new URIResolutionException(
                    "Unable to resolve URI " + stringRelative + " against " + baseURI + ".",
                    getMetadata()
            );
            ex.initCause(e);
            throw ex;
        }

        return ItemFactory.getInstance().createAnyURIItem(stringURI);
    }

    private URI parseURI(String uri) {
        try {
            return URIUtils.parseURIReference(uri);
        } catch (URISyntaxException e) {
            InvalidArgumentValueException ex = new InvalidArgumentValueException(
                    "Malformed URI: " + uri + " Cause: " + e.getMessage(),
                    getMetadata()
            );
            ex.initCause(e);
            throw ex;
        }
    }

    private URI parseBaseURI(String uri) {
        URI result = parseURI(uri);
        if (!result.isAbsolute() || result.isOpaque() || result.getFragment() != null) {
            throw new InvalidArgumentValueException(
                    "The base URI supplied to fn:resolve-uri() must be absolute, hierarchical, and fragment-free: "
                        + uri,
                    getMetadata()
            );
        }
        return result;
    }
}
