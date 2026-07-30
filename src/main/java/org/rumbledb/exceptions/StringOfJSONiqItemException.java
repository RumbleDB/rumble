package org.rumbledb.exceptions;

import org.rumbledb.errorcodes.ErrorCode;

import java.io.Serial;

public class StringOfJSONiqItemException extends RumbleException {

    @Serial
    private static final long serialVersionUID = 1L;

    public StringOfJSONiqItemException(String message, ExceptionMetadata metadata) {
        super(
            message,
            ErrorCode.StringOfJSONiqItemsErrorCode,
            metadata
        );
    }
}
