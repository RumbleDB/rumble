package org.rumbledb.exceptions;

import java.io.Serial;

import org.rumbledb.errorcodes.ErrorCode;

public class DatetimeOverflowOrUnderflow extends RumbleException {

    @Serial
    private static final long serialVersionUID = 1L;

    public DatetimeOverflowOrUnderflow(String message, ExceptionMetadata metadata) {
        super(message, ErrorCode.DatetimeOverflowOrUnderflow, metadata);
    }
}
