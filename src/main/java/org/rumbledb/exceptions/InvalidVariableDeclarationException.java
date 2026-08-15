package org.rumbledb.exceptions;

import java.io.Serial;

import org.rumbledb.errorcodes.ErrorCode;

public class InvalidVariableDeclarationException extends SemanticException {
    @Serial
    private static final long serialVersionUID = 1L;

    public InvalidVariableDeclarationException(String message, ExceptionMetadata metadata) {
        super(message, ErrorCode.InvalidVariableDeclaration, metadata);
    }
}
