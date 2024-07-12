package org.rumbledb.exceptions;

import org.rumbledb.errorcodes.ErrorCode;

public class InvalidExpressionClassification extends RumbleException {
    private static final long serialVersionUID = 1L;

    public InvalidExpressionClassification(String message, ExceptionMetadata metadata) {
        super(message, ErrorCode.InvalidExpressionClassification, metadata);
    }
}
