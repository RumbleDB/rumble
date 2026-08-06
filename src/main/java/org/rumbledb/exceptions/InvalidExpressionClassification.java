package org.rumbledb.exceptions;

import org.rumbledb.errorcodes.ErrorCode;

import java.io.Serial;

public class InvalidExpressionClassification extends RumbleException {
    @Serial
    private static final long serialVersionUID = 1L;

    public InvalidExpressionClassification(String message, ExceptionMetadata metadata) {
        super(message, ErrorCode.InvalidExpressionClassification, metadata);
    }
}
