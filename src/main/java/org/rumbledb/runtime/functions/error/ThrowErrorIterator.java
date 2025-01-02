package org.rumbledb.runtime.functions.error;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.errorcodes.ErrorCode;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.RumbleException;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;

import java.util.List;

public class ThrowErrorIterator extends AtMostOneItemLocalRuntimeIterator {
    public ThrowErrorIterator(List<RuntimeIterator> children, RuntimeStaticContext staticContext) {
        super(children, staticContext);
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext context) {
        if (this.children.isEmpty()) {
            // No argument case.
            throw new RumbleException(
                    "An error has been raised without an error description or code.",
                    ErrorCode.UnidentifiedErrorExceptionCode
            );
        } else if (this.children.size() == 1) {
            // Error code argument case.
            Item errorCode = this.children.get(0).materializeFirstItemOrNull(context);
            throw new RumbleException(
                    "An error has been raised without an error description.",
                    ErrorCode.valueOf(errorCode.getStringValue())
            );
        } else {
            // Error code and description arguments case.
            Item errorCode = this.children.get(0).materializeFirstItemOrNull(context);
            Item description = this.children.get(1).materializeFirstItemOrNull(context);
            throw new RumbleException(description.getStringValue(), ErrorCode.valueOf(errorCode.getStringValue()));
        }
    }
}
