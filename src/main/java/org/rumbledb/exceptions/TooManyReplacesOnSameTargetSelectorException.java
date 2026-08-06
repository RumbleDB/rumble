package org.rumbledb.exceptions;

import org.rumbledb.context.Name;
import org.rumbledb.errorcodes.ErrorCode;

import java.io.Serial;

public class TooManyReplacesOnSameTargetSelectorException extends RumbleException {

    @Serial
    private static final long serialVersionUID = 1L;

    public TooManyReplacesOnSameTargetSelectorException(
            Name targetInfo,
            String selectorInfo,
            ExceptionMetadata metadata
    ) {
        super(
            "Dynamic Updating error; Too many replaces on " + targetInfo + " at: " + selectorInfo + ".",
            ErrorCode.TooManyReplacesOnSameTargetSelectorErrorCode,
            metadata
        );
    }
}
