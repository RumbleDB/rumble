package org.rumbledb.exceptions;

import org.rumbledb.errorcodes.ErrorCode;

public class DecimalFormatPropertyInvalidValueException extends RumbleException {
    private static final long serialVersionUID = 1L;

    public DecimalFormatPropertyInvalidValueException(String message, ExceptionMetadata metadata) {
        super(message, ErrorCode.DecimalFormatPropertyInvalidValueErrorCode, metadata);
    }
}
