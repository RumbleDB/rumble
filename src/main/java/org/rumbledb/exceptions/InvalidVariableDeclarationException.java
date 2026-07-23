package org.rumbledb.exceptions;

import org.rumbledb.errorcodes.ErrorCode;

import java.io.Serial;

public class InvalidVariableDeclarationException extends SemanticException {
    @Serial
    private static final long serialVersionUID = 1L;

    public InvalidVariableDeclarationException(String message, ExceptionMetadata metadata) {
        super(message, ErrorCode.InvalidVariableDeclaration, metadata);
    }
}
