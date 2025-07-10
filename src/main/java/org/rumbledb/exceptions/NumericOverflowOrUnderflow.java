package org.rumbledb.exceptions;

import org.rumbledb.errorcodes.ErrorCode;


public class NumericOverflowOrUnderflow extends RumbleException {

    private static final long serialVersionUID = 1L;

    public NumericOverflowOrUnderflow(String message, ExceptionMetadata metadata) {
        super(
            message,
            ErrorCode.NumericOverflowOrUnderflow,
            metadata
        );
    }
}
