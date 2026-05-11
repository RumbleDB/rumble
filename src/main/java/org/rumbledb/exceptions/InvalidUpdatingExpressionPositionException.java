package org.rumbledb.exceptions;

import org.rumbledb.errorcodes.ErrorCode;

public class InvalidUpdatingExpressionPositionException extends RumbleException {

    private static final long serialVersionUID = 1L;

    public InvalidUpdatingExpressionPositionException(String message, ExceptionMetadata metadata) {
        super(message, ErrorCode.InvalidUpdatingExpressionPositionErrorCode, metadata);
    }

}
