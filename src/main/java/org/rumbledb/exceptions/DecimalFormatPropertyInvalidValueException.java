package org.rumbledb.exceptions;

import org.rumbledb.errorcodes.ErrorCode;

public class DecimalFormatPropertyInvalidValueException extends RumbleException {
    public DecimalFormatPropertyInvalidValueException(String message, ExceptionMetadata metadata) {
        super(message, ErrorCode.DecimalFormatPropertyInvalidValueErrorCode, metadata);
    }
}
