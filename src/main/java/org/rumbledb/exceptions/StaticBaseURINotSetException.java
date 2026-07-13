package org.rumbledb.exceptions;

import org.rumbledb.errorcodes.ErrorCode;

public class StaticBaseURINotSetException extends RumbleException {

    private static final long serialVersionUID = 1L;

    public StaticBaseURINotSetException(String message, ExceptionMetadata metadata) {
        super(message, ErrorCode.StaticBaseURINotSetErrorCode, metadata);
    }
}
