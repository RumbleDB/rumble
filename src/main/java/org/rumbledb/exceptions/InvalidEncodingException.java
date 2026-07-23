package org.rumbledb.exceptions;

import org.rumbledb.errorcodes.ErrorCode;

import java.io.Serial;

public class InvalidEncodingException extends RumbleException {

    @Serial
    private static final long serialVersionUID = 1L;

    public InvalidEncodingException(String message, ExceptionMetadata metadata) {
        super(message, ErrorCode.InvalidEncodingErrorCode, metadata);
    }
}
