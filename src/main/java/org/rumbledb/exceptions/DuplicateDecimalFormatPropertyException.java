package org.rumbledb.exceptions;

import org.rumbledb.errorcodes.ErrorCode;

import java.io.Serial;

public class DuplicateDecimalFormatPropertyException extends RumbleException {
    @Serial
    private static final long serialVersionUID = 1L;

    public DuplicateDecimalFormatPropertyException(String message, ExceptionMetadata metadata) {
        super(message, ErrorCode.DuplicateDecimalFormatPropertyErrorCode, metadata);
    }
}
