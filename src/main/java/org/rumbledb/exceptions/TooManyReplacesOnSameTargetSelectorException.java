package org.rumbledb.exceptions;

import org.rumbledb.errorcodes.ErrorCode;

public class TooManyReplacesOnSameTargetSelectorException extends RumbleException {

    private static final long serialVersionUID = 1L;

    public TooManyReplacesOnSameTargetSelectorException(
            String targetInfo,
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
