package org.rumbledb.exceptions;

import org.rumbledb.errorcodes.ErrorCode;

public class InvalidAnnotationNamespaceException extends SemanticException {

    private static final long serialVersionUID = 1L;

    public InvalidAnnotationNamespaceException(String message, ExceptionMetadata metadata) {
        super(message, ErrorCode.AnnotationInReservedNamespaceErrorCode, metadata);
    }
}
