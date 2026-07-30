package org.rumbledb.exceptions;

import org.rumbledb.errorcodes.ErrorCode;

import java.io.Serial;

public class InvalidUpdatingExpressionOperand extends SemanticException {
    @Serial
    private static final long serialVersionUID = 1L;

    public InvalidUpdatingExpressionOperand(String message, ExceptionMetadata metadata) {
        super(message, ErrorCode.InvalidUpdatingExpressionOperand, metadata);
    }
}
