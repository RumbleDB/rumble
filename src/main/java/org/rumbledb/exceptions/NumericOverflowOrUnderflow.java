package org.rumbledb.exceptions;

import org.rumbledb.errorcodes.ErrorCode;

import java.io.Serial;


public class NumericOverflowOrUnderflow extends RumbleException {

    @Serial
    private static final long serialVersionUID = 1L;

    public NumericOverflowOrUnderflow(String message, ExceptionMetadata metadata) {
        super(
            message,
            ErrorCode.NumericOverflowOrUnderflow,
            metadata
        );
    }
}
