package org.rumbledb.exceptions;

import java.io.Serial;

import org.rumbledb.errorcodes.ErrorCode;

public class InvalidUpdatingExpressionPositionException extends RumbleException {

    @Serial private static final long serialVersionUID = 1L;

    public InvalidUpdatingExpressionPositionException(String message, ExceptionMetadata metadata) {
        super(message, ErrorCode.InvalidUpdatingExpressionPositionErrorCode, metadata);
    }
}
