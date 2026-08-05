package org.rumbledb.exceptions;

import java.io.Serial;

import org.rumbledb.errorcodes.ErrorCode;

public class InvalidComposabilityUpdatingAndSequentialExpression extends SemanticException {
    @Serial private static final long serialVersionUID = 1L;

    public InvalidComposabilityUpdatingAndSequentialExpression(
            String message, ExceptionMetadata metadata) {
        super(message, ErrorCode.InvalidComposabilityUpdatingAndSequentialExpression, metadata);
    }
}
