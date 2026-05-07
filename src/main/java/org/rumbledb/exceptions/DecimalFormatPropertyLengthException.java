package org.rumbledb.exceptions;

import org.rumbledb.errorcodes.ErrorCode;

public class DecimalFormatPropertyLengthException extends RumbleException {
    public DecimalFormatPropertyLengthException(String message, ExceptionMetadata metadata) {
        super(message, ErrorCode.DecimalFormatPropertyLengthErrorCode, metadata);
    }
}
