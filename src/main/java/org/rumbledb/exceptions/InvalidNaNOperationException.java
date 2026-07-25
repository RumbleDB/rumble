package org.rumbledb.exceptions;

import org.rumbledb.errorcodes.ErrorCode;

import java.io.Serial;

public class InvalidNaNOperationException extends RumbleException {

    @Serial
    private static final long serialVersionUID = 1L;

    public InvalidNaNOperationException(String message, ExceptionMetadata metadata) {
        super(
            message,
            ErrorCode.InvalidNaNOperation,
            metadata
        );
    }
}
