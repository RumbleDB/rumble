package org.rumbledb.exceptions;

import org.rumbledb.errorcodes.ErrorCode;

import java.io.Serial;

public class DurationOverflowOrUnderflow extends RumbleException {

    @Serial
    private static final long serialVersionUID = 1L;

    public DurationOverflowOrUnderflow(String message, ExceptionMetadata metadata) {
        super(message, ErrorCode.DurationOverflowOrUnderflow, metadata);
    }
}
