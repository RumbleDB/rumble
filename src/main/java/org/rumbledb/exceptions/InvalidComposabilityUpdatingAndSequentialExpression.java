package org.rumbledb.exceptions;

import org.rumbledb.errorcodes.ErrorCode;

public class InvalidComposabilityUpdatingAndSequentialExpression extends SemanticException {
    private static final long serialVersionUID = 1L;

    public InvalidComposabilityUpdatingAndSequentialExpression(String message, ExceptionMetadata metadata) {
        super(message, ErrorCode.InvalidComposabilityUpdatingAndSequentialExpression, metadata);
    }
}
