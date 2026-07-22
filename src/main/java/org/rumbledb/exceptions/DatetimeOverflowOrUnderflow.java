package org.rumbledb.exceptions;

import org.rumbledb.errorcodes.ErrorCode;

import java.io.Serial;

public class DatetimeOverflowOrUnderflow extends RumbleException {

    @Serial
    private static final long serialVersionUID = 1L;

    public DatetimeOverflowOrUnderflow(String message, ExceptionMetadata metadata) {
        super(message, ErrorCode.DatetimeOverflowOrUnderflow, metadata);
    }
}
