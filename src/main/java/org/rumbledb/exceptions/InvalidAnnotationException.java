package org.rumbledb.exceptions;

import org.rumbledb.errorcodes.ErrorCode;

public class InvalidAnnotationException extends SemanticException {
    public InvalidAnnotationException(String message, ExceptionMetadata metadata) {
        super(message, ErrorCode.InvalidAnnotation, metadata);
    }
}
