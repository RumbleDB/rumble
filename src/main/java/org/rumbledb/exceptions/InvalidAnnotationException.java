package org.rumbledb.exceptions;

import java.io.Serial;

import org.rumbledb.errorcodes.ErrorCode;

public class InvalidAnnotationException extends SemanticException {
    @Serial private static final long serialVersionUID = 1L;

    public InvalidAnnotationException(String message, ExceptionMetadata metadata) {
        super(message, ErrorCode.InvalidAnnotation, metadata);
    }
}
