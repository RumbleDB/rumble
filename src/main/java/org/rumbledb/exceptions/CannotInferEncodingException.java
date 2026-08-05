package org.rumbledb.exceptions;

import java.io.Serial;

import org.rumbledb.errorcodes.ErrorCode;

public class CannotInferEncodingException extends RumbleException {

    @Serial private static final long serialVersionUID = 1L;

    public CannotInferEncodingException(String message, ExceptionMetadata metadata) {
        super(message, ErrorCode.CannotInferEncodingErrorCode, metadata);
    }
}
