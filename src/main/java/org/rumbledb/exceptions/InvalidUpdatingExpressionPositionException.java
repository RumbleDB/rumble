package org.rumbledb.exceptions;

import org.rumbledb.errorcodes.ErrorCode;

import java.io.Serial;

public class InvalidUpdatingExpressionPositionException extends RumbleException {

    @Serial
    private static final long serialVersionUID = 1L;

    public InvalidUpdatingExpressionPositionException(String message, ExceptionMetadata metadata) {
        super(message, ErrorCode.InvalidUpdatingExpressionPositionErrorCode, metadata);
    }

}
