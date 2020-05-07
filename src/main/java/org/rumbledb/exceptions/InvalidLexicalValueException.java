package org.rumbledb.exceptions;

import org.rumbledb.errorcodes.ErrorCode;

public class InvalidLexicalValueException extends RumbleException {

    private static final long serialVersionUID = 1L;

    public InvalidLexicalValueException(String message, ExceptionMetadata metadata) {
        super(
            message,
            ErrorCode.InvalidLexicalValueErrorCode,
            metadata
        );
    }
}
