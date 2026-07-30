package org.rumbledb.runtime.functions.io;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.cursor.ContextOrArgumentLocalCursor;
import org.rumbledb.runtime.cursor.Cursor;
import org.rumbledb.runtime.functions.input.FileSystemUtil;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.io.Serial;
import java.net.URI;
import java.util.List;

public class DocAvailableFunctionIterator extends AtMostOneItemLocalRuntimeIterator {
    @Serial
    private static final long serialVersionUID = 1L;

    public DocAvailableFunctionIterator(
            List<RuntimeIterator> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
    }

    @Override
    public Cursor<Item> createNativeCursor(DynamicContext context) {
        return ContextOrArgumentLocalCursor.mapArgument(
            this.getChild(0),
            context,
            this::evaluate,
            getMetadata()
        );
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext context) {
        return evaluate(this.getChild(0).materializeFirstItemOrNull(context));
    }

    private Item evaluate(Item uriItem) {
        if (uriItem == null) {
            return ItemFactory.getInstance().createBooleanItem(false);
        }
        try {
            URI uri = FileSystemUtil.resolveURI(
                this.staticContext.getStaticURI(),
                uriItem.getStringValue(),
                getMetadata()
            );
            InputStream xmlFileStream = FileSystemUtil.getDataInputStream(uri, getConfiguration(), getMetadata());
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilderFactory.setNamespaceAware(true);
            documentBuilderFactory.newDocumentBuilder().parse(xmlFileStream);
            return ItemFactory.getInstance().createBooleanItem(true);
        } catch (Exception e) {
            return ItemFactory.getInstance().createBooleanItem(false);
        }
    }
}
