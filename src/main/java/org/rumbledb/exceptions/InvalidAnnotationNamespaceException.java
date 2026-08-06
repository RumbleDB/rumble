package org.rumbledb.exceptions;

import org.rumbledb.errorcodes.ErrorCode;

import java.io.Serial;

public class InvalidAnnotationNamespaceException extends SemanticException {

    @Serial
    private static final long serialVersionUID = 1L;

    public InvalidAnnotationNamespaceException(String message, ExceptionMetadata metadata) {
        super(message, ErrorCode.AnnotationInReservedNamespaceErrorCode, metadata);
    }
}
