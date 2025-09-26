package org.rumbledb.exceptions;

import org.rumbledb.errorcodes.ErrorCode;

public class InvalidUpdatingExpressionCondition extends SemanticException {
    private static final long serialVersionUID = 1L;

    public InvalidUpdatingExpressionCondition(String message, ExceptionMetadata metadata) {
        super(message, ErrorCode.InvalidUpdatingExpressionCondition, metadata);
    }
}
