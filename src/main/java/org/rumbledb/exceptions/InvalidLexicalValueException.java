package org.rumbledb.exceptions;

import org.rumbledb.errorcodes.ErrorCode;

import java.io.Serial;

public class InvalidLexicalValueException extends RumbleException {

    @Serial
    private static final long serialVersionUID = 1L;

    public InvalidLexicalValueException(String message, ExceptionMetadata metadata) {
        super(
            message,
            ErrorCode.InvalidLexicalValueErrorCode,
            metadata
        );
    }
}
