package org.rumbledb.exceptions;

import java.io.Serial;

import org.rumbledb.errorcodes.ErrorCode;

public class SimpleExpressionMustBeVacuousException extends RumbleException {

    @Serial
    private static final long serialVersionUID = 1L;

    public SimpleExpressionMustBeVacuousException(String message, ExceptionMetadata metadata) {
        super(message, ErrorCode.SimpleExpressionMustBeVacuousErrorCode, metadata);
    }
}
