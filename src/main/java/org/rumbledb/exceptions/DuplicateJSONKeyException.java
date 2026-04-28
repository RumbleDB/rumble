package org.rumbledb.exceptions;

import org.rumbledb.errorcodes.ErrorCode;

public class DuplicateJSONKeyException extends RumbleException {

    private static final long serialVersionUID = 1L;

    public DuplicateJSONKeyException(String message, ExceptionMetadata metadata) {
        super(message, ErrorCode.DuplicateJSONKeyErrorCode, metadata);
    }
}

