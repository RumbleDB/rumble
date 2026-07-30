package org.rumbledb.runtime.xml;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.errorcodes.ErrorCode;
import org.rumbledb.exceptions.UnexpectedStaticTypeException;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;

import java.io.Serial;
import java.util.Collections;

public class PathRootRuntimeIterator extends AtMostOneItemLocalRuntimeIterator {
    @Serial
    private static final long serialVersionUID = 1L;

    public PathRootRuntimeIterator(RuntimeStaticContext staticContext) {
        super(Collections.emptyList(), staticContext);
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext dynamicContext) {
        Item node = dynamicContext.getVariableValues()
            .getLocalVariableValue(Name.CONTEXT_ITEM, getMetadata())
            .get(0);
        if (!node.isNode()) {
            throw new UnexpectedStaticTypeException(
                    "Leading slash path expressions require the context item to be a node [err:XPDY0050].",
                    ErrorCode.DynamicTypeTreatErrorCode,
                    getMetadata()
            );
        }
        Item current = node;
        while (current.parent() != null) {
            current = current.parent();
        }
        if (!current.isDocumentNode()) {
            throw new UnexpectedStaticTypeException(
                    "Leading slash path expressions require the root of the context item to be a document node [err:XPDY0050].",
                    ErrorCode.DynamicTypeTreatErrorCode,
                    getMetadata()
            );
        }
        return current;
    }
}
