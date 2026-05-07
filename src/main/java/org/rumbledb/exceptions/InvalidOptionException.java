package org.rumbledb.exceptions;

import org.rumbledb.errorcodes.ErrorCode;

public class InvalidOptionException extends RumbleException {

    private static final long serialVersionUID = 1L;

    public InvalidOptionException(String message, ExceptionMetadata metadata) {
        super(message, ErrorCode.InvalidOptionErrorCode, metadata);
    }
}
