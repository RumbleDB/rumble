package org.rumbledb.exceptions;

import org.rumbledb.errorcodes.ErrorCode;

public class InvalidSequentialChildInNonSequentialParent extends SemanticException {
    private static final long serialVersionUID = 1L;

    public InvalidSequentialChildInNonSequentialParent(String message, ExceptionMetadata metadata) {
        super(message, ErrorCode.InvalidComposabilityUpdatingAndSequentialExpression, metadata);
    }
}
