package org.rumbledb.exceptions;

import org.rumbledb.errorcodes.ErrorCode;

public class InvalidAnnotationException extends SemanticException {
    private static final long serialVersionUID = 1L;

    public InvalidAnnotationException(String message, ExceptionMetadata metadata) {
        super(message, ErrorCode.InvalidAnnotation, metadata);
    }
}
