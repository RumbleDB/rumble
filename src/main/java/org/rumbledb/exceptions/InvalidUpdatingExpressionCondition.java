package org.rumbledb.exceptions;

import org.rumbledb.errorcodes.ErrorCode;

import java.io.Serial;

public class InvalidUpdatingExpressionCondition extends SemanticException {
    @Serial
    private static final long serialVersionUID = 1L;

    public InvalidUpdatingExpressionCondition(String message, ExceptionMetadata metadata) {
        super(message, ErrorCode.InvalidUpdatingExpressionCondition, metadata);
    }
}
