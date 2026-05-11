package org.rumbledb.exceptions;

import org.rumbledb.errorcodes.ErrorCode;

public class InvalidNaNOperationException extends RumbleException {

    private static final long serialVersionUID = 1L;

    public InvalidNaNOperationException(String message, ExceptionMetadata metadata) {
        super(
            message,
            ErrorCode.InvalidNaNOperation,
            metadata
        );
    }
}
