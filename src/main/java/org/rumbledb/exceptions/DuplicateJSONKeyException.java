package org.rumbledb.exceptions;

import org.rumbledb.errorcodes.ErrorCode;

import java.io.Serial;

public class DuplicateJSONKeyException extends RumbleException {

    @Serial
    private static final long serialVersionUID = 1L;

    public DuplicateJSONKeyException(String message, ExceptionMetadata metadata) {
        super(message, ErrorCode.DuplicateJSONKeyErrorCode, metadata);
    }
}

