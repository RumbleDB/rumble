package org.rumbledb.exceptions;

import java.io.Serial;

import org.rumbledb.errorcodes.ErrorCode;

public class DuplicateJSONKeyException extends RumbleException {

    @Serial
    private static final long serialVersionUID = 1L;

    public DuplicateJSONKeyException(String message, ExceptionMetadata metadata) {
        super(message, ErrorCode.DuplicateJSONKeyErrorCode, metadata);
    }
}
