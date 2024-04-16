package org.rumbledb.exceptions;

import org.rumbledb.errorcodes.ErrorCode;

public class InvalidControlStatementComposability extends SemanticException {
    private static final long serialVersionUID = 1L;

    public InvalidControlStatementComposability(String message, ExceptionMetadata metadata) {
        super(message, ErrorCode.InvalidComposabilityUpdatingAndSequentialExpression, metadata);
    }
}
