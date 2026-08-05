package org.rumbledb.exceptions;

import java.io.Serial;

import org.rumbledb.errorcodes.ErrorCode;

public class DivisionByZeroException extends RumbleException {

    @Serial private static final long serialVersionUID = 1L;

    public DivisionByZeroException(ExceptionMetadata metadata) {
        super("Division by zero!", ErrorCode.DivisionByZero, metadata);
    }
}
