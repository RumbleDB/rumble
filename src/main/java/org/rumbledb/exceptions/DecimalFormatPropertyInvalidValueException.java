package org.rumbledb.exceptions;

import java.io.Serial;

import org.rumbledb.errorcodes.ErrorCode;

public class DecimalFormatPropertyInvalidValueException extends RumbleException {
    @Serial private static final long serialVersionUID = 1L;

    public DecimalFormatPropertyInvalidValueException(String message, ExceptionMetadata metadata) {
        super(message, ErrorCode.DecimalFormatPropertyInvalidValueErrorCode, metadata);
    }
}
