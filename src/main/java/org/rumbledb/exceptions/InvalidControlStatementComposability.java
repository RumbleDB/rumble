package org.rumbledb.exceptions;

import org.rumbledb.errorcodes.ErrorCode;

import java.io.Serial;

public class InvalidControlStatementComposability extends SemanticException {
    @Serial
    private static final long serialVersionUID = 1L;

    public InvalidControlStatementComposability(String message, ExceptionMetadata metadata) {
        super(message, ErrorCode.InvalidControlStatementComposability, metadata);
    }
}
