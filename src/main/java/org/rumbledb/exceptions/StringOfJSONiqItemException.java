package org.rumbledb.exceptions;

import org.rumbledb.errorcodes.ErrorCode;

public class StringOfJSONiqItemException extends RumbleException {

    private static final long serialVersionUID = 1L;

    public StringOfJSONiqItemException(String message, ExceptionMetadata metadata) {
        super(
            message,
            ErrorCode.StringOfJSONiqItemsErrorCode,
            metadata
        );
    }
}
