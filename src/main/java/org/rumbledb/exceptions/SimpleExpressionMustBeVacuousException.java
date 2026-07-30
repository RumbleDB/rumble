package org.rumbledb.exceptions;

import org.rumbledb.errorcodes.ErrorCode;

public class SimpleExpressionMustBeVacuousException extends RumbleException {

    private static final long serialVersionUID = 1L;

    public SimpleExpressionMustBeVacuousException(String message, ExceptionMetadata metadata) {
        super(message, ErrorCode.SimpleExpressionMustBeVacuousErrorCode, metadata);
    }
}
