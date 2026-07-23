package org.rumbledb.exceptions;

import org.rumbledb.errorcodes.ErrorCode;

import java.io.Serial;

public class InvalidAnnotationException extends SemanticException {
    @Serial
    private static final long serialVersionUID = 1L;

    public InvalidAnnotationException(String message, ExceptionMetadata metadata) {
        super(message, ErrorCode.InvalidAnnotation, metadata);
    }
}
