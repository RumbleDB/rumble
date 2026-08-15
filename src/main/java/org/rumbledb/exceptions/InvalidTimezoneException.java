package org.rumbledb.exceptions;

import java.io.Serial;

import org.rumbledb.errorcodes.ErrorCode;

public class InvalidTimezoneException extends RumbleException {

    @Serial
    private static final long serialVersionUID = 1L;

    public InvalidTimezoneException(String message, ExceptionMetadata metadata) {
        super(message, ErrorCode.InvalidTimezoneValue, metadata);
    }
}
