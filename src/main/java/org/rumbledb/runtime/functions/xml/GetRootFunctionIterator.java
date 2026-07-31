package org.rumbledb.runtime.functions.xml;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.UnsupportedFeatureException;
import org.rumbledb.items.xml.AttributeItem;
import org.rumbledb.items.xml.CommentItem;
import org.rumbledb.items.xml.DocumentItem;
import org.rumbledb.items.xml.ElementItem;
import org.rumbledb.items.xml.TextItem;
import org.rumbledb.runtime.cursor.ContextOrArgumentLocalCursor;
import org.rumbledb.runtime.cursor.Cursor;
import org.rumbledb.runtime.functions.base.LocalFunctionCallIterator;
import org.rumbledb.runtime.plan.RuntimePlan;

import java.io.Serial;
import java.util.List;

public class GetRootFunctionIterator extends LocalFunctionCallIterator {
    @Serial
    private static final long serialVersionUID = 1L;

    public GetRootFunctionIterator(
            List<RuntimePlan<Item>> parameters,
            RuntimeStaticContext staticContext
    ) {
        super(parameters, staticContext);
    }

    @Override
    public Cursor<Item> createNativeCursor(DynamicContext context) {
        return ContextOrArgumentLocalCursor.mapFirstArgumentOrContext(
            this.getChildren(),
            context,
            this::evaluate,
            getMetadata()
        );
    }

    private Item evaluate(Item node) {
        if (
            node instanceof DocumentItem
                || node instanceof ElementItem
                || node instanceof AttributeItem
                || node instanceof TextItem
                || node instanceof CommentItem
        ) {
            Item current = node;
            while (current.parent() != null) {
                current = current.parent();
            }
            return current;
        }
        throw new UnsupportedFeatureException(
                "The argument must be a reference to a supported XML node type",
                getMetadata()
        );
    }

}
