package org.rumbledb.exceptions;

import org.rumbledb.errorcodes.ErrorCode;

public class InvalidVariableDeclarationException extends SemanticException {
    private static final long serialVersionUID = 1L;

    public InvalidVariableDeclarationException(String message, ExceptionMetadata metadata) {
        super(message, ErrorCode.InvalidVariableDeclaration, metadata);
    }
}
