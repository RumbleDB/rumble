package org.rumbledb.exceptions;

import org.rumbledb.errorcodes.ErrorCode;

public class PrefixCannotBeExpandedException extends RumbleException {

    private static final long serialVersionUID = 1L;

    public PrefixCannotBeExpandedException(String message, ExceptionMetadata metadata) {
        super(message, ErrorCode.PrefixCannotBeExpandedErrorCode, metadata);
    }
}
