package org.rumbledb.exceptions;

import org.rumbledb.errorcodes.ErrorCode;

public class InvalidEncodingException extends RumbleException {

    private static final long serialVersionUID = 1L;

    public InvalidEncodingException(String message, ExceptionMetadata metadata) {
        super(message, ErrorCode.InvalidEncodingErrorCode, metadata);
    }
}
