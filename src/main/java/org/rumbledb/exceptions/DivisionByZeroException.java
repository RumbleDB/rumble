package org.rumbledb.exceptions;

import org.rumbledb.errorcodes.ErrorCode;

import java.io.Serial;


public class DivisionByZeroException extends RumbleException {

    @Serial
    private static final long serialVersionUID = 1L;

    public DivisionByZeroException(ExceptionMetadata metadata) {
        super(
            "Division by zero!",
            ErrorCode.DivisionByZero,
            metadata
        );
    }
}
