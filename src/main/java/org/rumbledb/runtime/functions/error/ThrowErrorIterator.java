package org.rumbledb.runtime.functions.error;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.errorcodes.ErrorCode;
import org.rumbledb.exceptions.RumbleException;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;

import java.util.List;

public class ThrowErrorIterator extends AtMostOneItemLocalRuntimeIterator {
    private static final long serialVersionUID = 1L;

    public ThrowErrorIterator(List<RuntimeIterator> children, RuntimeStaticContext staticContext) {
        super(children, staticContext);
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext context) {
        if (this.children.isEmpty() || this.children.get(0).materializeFirstItemOrNull(context) == null) {
            // No argument case.
            throw new RumbleException(
                    "An error has been raised without an error description or code.",
                    ErrorCode.UnidentifiedErrorExceptionCode
            );
        }

        Name errorCode = this.children.get(0).materializeFirstItemOrNull(context).getQNameValue();

        if (this.children.size() == 1) {
            // Error code argument case.
            throw new RumbleException(
                    "An error has been raised without an error description.",
                    new ErrorCode(errorCode)
            );
        } else if (this.children.size() == 2) {
            // Error code and description arguments case.
            String description = this.children.get(1).materializeFirstItemOrNull(context).getStringValue();
            throw new RumbleException(description, new ErrorCode(errorCode));
        } else {
            // Error code, description, and object case.
            String description = this.children.get(1).materializeFirstItemOrNull(context).getStringValue();
            Item object = this.children.get(2).materializeFirstItemOrNull(context);
            String message = description + ". Object: " + object.getStringValue();

            throw new RumbleException(message, new ErrorCode(errorCode));
        }
    }
}
