package org.rumbledb.exceptions;

import org.rumbledb.errorcodes.ErrorCode;

public class IsStaticallyUnexpectedTypeException extends RumbleException {
    private static final long serialVersionUID = 1L;

    public IsStaticallyUnexpectedTypeException(String message) {
        super(message, ErrorCode.UnexpectedStaticType);
    }

    public IsStaticallyUnexpectedTypeException(String message, ExceptionMetadata metadata) {
        super(message, ErrorCode.UnexpectedStaticType, metadata);
    }
}
