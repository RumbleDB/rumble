package org.rumbledb.exceptions;

import java.io.Serial;

import org.rumbledb.errorcodes.ErrorCode;

public class InvalidControlStatementComposability extends SemanticException {
    @Serial private static final long serialVersionUID = 1L;

    public InvalidControlStatementComposability(String message, ExceptionMetadata metadata) {
        super(message, ErrorCode.InvalidControlStatementComposability, metadata);
    }
}
