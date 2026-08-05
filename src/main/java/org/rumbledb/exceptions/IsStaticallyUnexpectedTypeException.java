package org.rumbledb.exceptions;

import java.io.Serial;

import org.rumbledb.errorcodes.ErrorCode;

public class IsStaticallyUnexpectedTypeException extends RumbleException {
    @Serial private static final long serialVersionUID = 1L;

    public IsStaticallyUnexpectedTypeException(String message, ExceptionMetadata metadata) {
        super(message, ErrorCode.UnexpectedStaticType, metadata);
    }
}
