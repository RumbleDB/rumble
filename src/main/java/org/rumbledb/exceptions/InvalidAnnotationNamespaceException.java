package org.rumbledb.exceptions;

import java.io.Serial;

import org.rumbledb.errorcodes.ErrorCode;

public class InvalidAnnotationNamespaceException extends SemanticException {

    @Serial
    private static final long serialVersionUID = 1L;

    public InvalidAnnotationNamespaceException(String message, ExceptionMetadata metadata) {
        super(message, ErrorCode.AnnotationInReservedNamespaceErrorCode, metadata);
    }
}
