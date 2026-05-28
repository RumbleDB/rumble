package org.rumbledb.exceptions;

import org.rumbledb.errorcodes.ErrorCode;

public class CannotConvertToQNameException extends RumbleException {

    private static final long serialVersionUID = 1L;

    public CannotConvertToQNameException(String message, ExceptionMetadata metadata) {
        super(message, ErrorCode.CannotConvertToQNameErrorCode, metadata);
    }
}
