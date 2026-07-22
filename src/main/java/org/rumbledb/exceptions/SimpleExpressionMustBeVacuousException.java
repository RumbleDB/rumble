package org.rumbledb.exceptions;

import org.rumbledb.errorcodes.ErrorCode;

import java.io.Serial;

public class SimpleExpressionMustBeVacuousException extends RumbleException {

    @Serial
    private static final long serialVersionUID = 1L;

    public SimpleExpressionMustBeVacuousException(String message, ExceptionMetadata metadata) {
        super(message, ErrorCode.SimpleExpressionMustBeVacuousErrorCode, metadata);
    }
}
