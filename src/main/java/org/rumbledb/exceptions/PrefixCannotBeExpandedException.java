package org.rumbledb.exceptions;

import org.rumbledb.errorcodes.ErrorCode;

import java.io.Serial;

public class PrefixCannotBeExpandedException extends RumbleException {

    @Serial
    private static final long serialVersionUID = 1L;

    public PrefixCannotBeExpandedException(String message, ExceptionMetadata metadata) {
        super(message, ErrorCode.PrefixCannotBeExpandedErrorCode, metadata);
    }
}
