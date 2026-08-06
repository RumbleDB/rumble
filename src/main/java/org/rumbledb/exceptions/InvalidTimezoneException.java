package org.rumbledb.exceptions;

import org.rumbledb.errorcodes.ErrorCode;

import java.io.Serial;

public class InvalidTimezoneException extends RumbleException {

    @Serial
    private static final long serialVersionUID = 1L;

    public InvalidTimezoneException(String message, ExceptionMetadata metadata) {
        super(message, ErrorCode.InvalidTimezoneValue, metadata);
    }
}
