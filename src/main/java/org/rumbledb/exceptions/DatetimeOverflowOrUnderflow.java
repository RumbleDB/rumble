package org.rumbledb.exceptions;

import org.rumbledb.errorcodes.ErrorCode;

public class DatetimeOverflowOrUnderflow extends RumbleException {

    private static final long serialVersionUID = 1L;

    public DatetimeOverflowOrUnderflow(String message, ExceptionMetadata metadata) {
        super(message, ErrorCode.DatetimeOverflowOrUnderflow, metadata);
    }
}
