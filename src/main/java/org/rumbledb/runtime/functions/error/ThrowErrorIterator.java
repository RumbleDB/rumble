package org.rumbledb.runtime.functions.error;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.errorcodes.ErrorCode;
import org.rumbledb.exceptions.RumbleException;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.cursor.ComputedLocalCursor;
import org.rumbledb.runtime.cursor.LocalCursor;

import java.io.Serial;
import java.util.List;

public class ThrowErrorIterator extends AtMostOneItemLocalRuntimeIterator {
    @Serial
    private static final long serialVersionUID = 1L;

    public ThrowErrorIterator(List<RuntimeIterator> children, RuntimeStaticContext staticContext) {
        super(children, staticContext);
    }

    @Override
    public LocalCursor<Item> createLocalCursor(DynamicContext context) {
        return new ComputedLocalCursor<>(
                () -> raiseError(
                    ComputedLocalCursor.arguments(
                        this.getChildren().size(),
                        index -> index == 2
                            ? null
                            : this.getChild(index).materializeFirstOrNull(context)
                    ),
                    this.getChildren().size() == 3
                        ? () -> this.getChild(2).materialize(context)
                        : List::of
                ),
                getMetadata()
        );
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext context) {
        return raiseError(
            ComputedLocalCursor.arguments(
                this.getChildren().size(),
                index -> this.getChild(index).materializeFirstItemOrNull(context)
            ),
            this.getChildren().size() == 3
                ? () -> this.getChild(2).materialize(context)
                : List::of
        );
    }

    private Item raiseError(
            ComputedLocalCursor.Arguments<Item> arguments,
            java.util.function.Supplier<List<Item>> errorValue
    ) {
        if (arguments.size() == 0 || arguments.get(0) == null) {
            // No argument case.
            throw new RumbleException(
                    "An error has been raised without an error description or code.",
                    ErrorCode.UnidentifiedErrorExceptionCode,
                    this.getMetadata()
            );
        }

        Name errorCode = arguments.get(0).getQNameValue();

        if (arguments.size() == 1) {
            // Error code argument case.
            throw new RumbleException(
                    "An error has been raised without an error description.",
                    new ErrorCode(errorCode),
                    this.getMetadata()
            );
        } else if (arguments.size() == 2) {
            // Error code and description arguments case.
            String description = arguments.get(1).getStringValue();
            throw new RumbleException(description, new ErrorCode(errorCode), this.getMetadata());
        } else {
            // Error code, description, and object case.
            String description = arguments.get(1).getStringValue();
            throw new RumbleException(
                    description,
                    new ErrorCode(errorCode),
                    this.getMetadata(),
                    errorValue.get()
            );
        }
    }
}
