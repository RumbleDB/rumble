package org.rumbledb.exceptions;

import org.rumbledb.errorcodes.ErrorCodes;

import org.rumbledb.exceptions.ExceptionMetadata;

public class InvalidLexicalValueException extends SparksoniqRuntimeException {

    private static final long serialVersionUID = 1L;

    public InvalidLexicalValueException(String message, ExceptionMetadata metadata) {
        super(
            message,
            ErrorCodes.InvalidLexicalValueErrorCode,
            metadata.getExceptionMetadata()
        );
    }
}
