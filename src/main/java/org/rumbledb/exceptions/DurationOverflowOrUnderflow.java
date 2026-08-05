package org.rumbledb.exceptions;

import java.io.Serial;

import org.rumbledb.errorcodes.ErrorCode;

public class DurationOverflowOrUnderflow extends RumbleException {

    @Serial private static final long serialVersionUID = 1L;

    public DurationOverflowOrUnderflow(String message, ExceptionMetadata metadata) {
        super(message, ErrorCode.DurationOverflowOrUnderflow, metadata);
    }
}
