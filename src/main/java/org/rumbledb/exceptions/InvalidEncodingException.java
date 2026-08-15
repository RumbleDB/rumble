package org.rumbledb.exceptions;

import java.io.Serial;

import org.rumbledb.errorcodes.ErrorCode;

public class InvalidEncodingException extends RumbleException {

    @Serial
    private static final long serialVersionUID = 1L;

    public InvalidEncodingException(String message, ExceptionMetadata metadata) {
        super(message, ErrorCode.InvalidEncodingErrorCode, metadata);
    }
}
