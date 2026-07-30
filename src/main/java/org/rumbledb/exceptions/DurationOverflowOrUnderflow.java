package org.rumbledb.exceptions;

import org.rumbledb.errorcodes.ErrorCode;

public class DurationOverflowOrUnderflow extends RumbleException {

    private static final long serialVersionUID = 1L;

    public DurationOverflowOrUnderflow(String message, ExceptionMetadata metadata) {
        super(message, ErrorCode.DurationOverflowOrUnderflow, metadata);
    }
}
