package org.rumbledb.exceptions;

import org.rumbledb.errorcodes.ErrorCode;


public class DivisionByZeroException extends RumbleException {

    private static final long serialVersionUID = 1L;

    public DivisionByZeroException(ExceptionMetadata metadata) {
        super(
            "Division by zero!",
            ErrorCode.DivisionByZero,
            metadata
        );
    }
}
