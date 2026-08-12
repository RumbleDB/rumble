package org.rumbledb.exceptions;

import java.io.Serial;

import org.rumbledb.errorcodes.ErrorCode;

public class UnexpectedStaticTypeException extends RumbleException {

    @Serial
    private static final long serialVersionUID = 1L;

    public UnexpectedStaticTypeException(String message) {
        super(message, ErrorCode.UnexpectedTypeErrorCode);
    }

    public UnexpectedStaticTypeException(String message, ExceptionMetadata metadata) {
        super(message, ErrorCode.UnexpectedTypeErrorCode, metadata);
    }

    public UnexpectedStaticTypeException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }

    public UnexpectedStaticTypeException(String message, ErrorCode errorCode, ExceptionMetadata metadata) {
        super(message, errorCode, metadata);
    }
}
