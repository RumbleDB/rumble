package org.rumbledb.exceptions;

import java.io.Serial;

import org.rumbledb.errorcodes.ErrorCode;

public class InvalidExpressionClassification extends RumbleException {
    @Serial
    private static final long serialVersionUID = 1L;

    public InvalidExpressionClassification(String message, ExceptionMetadata metadata) {
        super(message, ErrorCode.InvalidExpressionClassification, metadata);
    }
}
