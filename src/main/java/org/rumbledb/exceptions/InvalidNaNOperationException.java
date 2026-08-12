package org.rumbledb.exceptions;

import java.io.Serial;

import org.rumbledb.errorcodes.ErrorCode;

public class InvalidNaNOperationException extends RumbleException {

    @Serial
    private static final long serialVersionUID = 1L;

    public InvalidNaNOperationException(String message, ExceptionMetadata metadata) {
        super(message, ErrorCode.InvalidNaNOperation, metadata);
    }
}
