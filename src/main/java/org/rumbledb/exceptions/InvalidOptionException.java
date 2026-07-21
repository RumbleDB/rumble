package org.rumbledb.exceptions;

import org.rumbledb.errorcodes.ErrorCode;

import java.io.Serial;

public class InvalidOptionException extends RumbleException {

    @Serial
    private static final long serialVersionUID = 1L;

    public InvalidOptionException(String message, ExceptionMetadata metadata) {
        super(message, ErrorCode.InvalidOptionErrorCode, metadata);
    }
}
