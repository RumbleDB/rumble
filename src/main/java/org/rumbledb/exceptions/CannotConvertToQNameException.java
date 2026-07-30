package org.rumbledb.exceptions;

import org.rumbledb.errorcodes.ErrorCode;

import java.io.Serial;

public class CannotConvertToQNameException extends RumbleException {

    @Serial
    private static final long serialVersionUID = 1L;

    public CannotConvertToQNameException(String message, ExceptionMetadata metadata) {
        super(message, ErrorCode.CannotConvertToQNameErrorCode, metadata);
    }
}
