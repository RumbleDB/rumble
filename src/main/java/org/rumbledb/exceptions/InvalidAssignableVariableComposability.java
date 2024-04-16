package org.rumbledb.exceptions;

import org.rumbledb.errorcodes.ErrorCode;

public class InvalidAssignableVariableComposability extends SemanticException {
    private static final long serialVersionUID = 1L;

    public InvalidAssignableVariableComposability(String message, ExceptionMetadata metadata) {
        super(message, ErrorCode.InvalidComposabilityUpdatingAndSequentialExpression, metadata);
    }
}
