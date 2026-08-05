package org.rumbledb.exceptions;

import java.io.Serial;

import org.rumbledb.errorcodes.ErrorCode;

public class InvalidOptionException extends RumbleException {

    @Serial private static final long serialVersionUID = 1L;

    public InvalidOptionException(String message, ExceptionMetadata metadata) {
        super(message, ErrorCode.InvalidOptionErrorCode, metadata);
    }
}
