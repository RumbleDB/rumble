package org.rumbledb.exceptions;

import org.rumbledb.errorcodes.ErrorCode;

public class CannotInferEncodingException extends RumbleException {

    private static final long serialVersionUID = 1L;

    public CannotInferEncodingException(String message, ExceptionMetadata metadata) {
        super(message, ErrorCode.CannotInferEncodingErrorCode, metadata);
    }
}
