package org.rumbledb.exceptions;

import org.rumbledb.errorcodes.ErrorCode;

public class DuplicateDecimalFormatPropertyException extends RumbleException {
    private static final long serialVersionUID = 1L;

    public DuplicateDecimalFormatPropertyException(String message, ExceptionMetadata metadata) {
        super(message, ErrorCode.DuplicateDecimalFormatPropertyErrorCode, metadata);
    }
}
