package org.rumbledb.exceptions;

import org.rumbledb.errorcodes.ErrorCode;

public class ArithmeticOverflowOrUnderflow extends RumbleException {

    private static final long serialVersionUID = 1L;

    public ArithmeticOverflowOrUnderflow(String message, ExceptionMetadata metadata) {
        super(message, ErrorCode.InvalidTimezoneValue, metadata);
    }
}
