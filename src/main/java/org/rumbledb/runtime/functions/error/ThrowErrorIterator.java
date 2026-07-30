package org.rumbledb.runtime.functions.error;


import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.errorcodes.ErrorCode;
import org.rumbledb.exceptions.RumbleException;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;

import java.io.Serial;
import java.util.List;

public class ThrowErrorIterator extends AtMostOneItemLocalRuntimeIterator {
    @Serial
    private static final long serialVersionUID = 1L;

    public ThrowErrorIterator(List<RuntimeIterator> children, RuntimeStaticContext staticContext) {
        super(children, staticContext);
    }

    @Override
    public Item evaluateAtMostOne(DynamicContext context) {
        return raiseError(
            context,
            this.getChildren().size() == 3
                ? () -> this.getChild(2).materialize(context)
                : List::of
        );
    }

    private Item raiseError(
            DynamicContext context,
            java.util.function.Supplier<List<Item>> errorValue
    ) {
        if (this.getChildren().isEmpty()) {
            // No argument case.
            throw new RumbleException(
                    "An error has been raised without an error description or code.",
                    ErrorCode.UnidentifiedErrorExceptionCode,
                    this.getMetadata()
            );
        }

        Item errorCodeItem = this.getChild(0).materializeFirstItemOrNull(context);
        if (errorCodeItem == null) {
            throw new RumbleException(
                    "An error has been raised without an error description or code.",
                    ErrorCode.UnidentifiedErrorExceptionCode,
                    this.getMetadata()
            );
        }
        Name errorCode = errorCodeItem.getQNameValue();

        if (this.getChildren().size() == 1) {
            // Error code argument case.
            throw new RumbleException(
                    "An error has been raised without an error description.",
                    new ErrorCode(errorCode),
                    this.getMetadata()
            );
        }

        String description = this.getChild(1).materializeFirstItemOrNull(context).getStringValue();
        if (this.getChildren().size() == 2) {
            // Error code and description arguments case.
            throw new RumbleException(description, new ErrorCode(errorCode), this.getMetadata());
        } else {
            // Error code, description, and object case.
            throw new RumbleException(
                    description,
                    new ErrorCode(errorCode),
                    this.getMetadata(),
                    errorValue.get()
            );
        }
    }
}
