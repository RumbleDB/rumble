package org.rumbledb.exceptions;

import java.io.Serial;

import org.rumbledb.errorcodes.ErrorCode;

public class InvalidJSONException extends RumbleException {

    @Serial private static final long serialVersionUID = 1L;

    public InvalidJSONException(String message, ExceptionMetadata metadata) {
        super(message, ErrorCode.InvalidJSONErrorCode, metadata);
    }
}
