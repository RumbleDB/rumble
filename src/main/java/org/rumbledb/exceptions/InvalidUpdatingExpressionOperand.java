package org.rumbledb.exceptions;

import org.rumbledb.errorcodes.ErrorCode;

public class InvalidUpdatingExpressionOperand extends SemanticException {
    private static final long serialVersionUID = 1L;

    public InvalidUpdatingExpressionOperand(String message, ExceptionMetadata metadata) {
        super(message, ErrorCode.InvalidUpdatingExpressionOperand, metadata);
    }
}
