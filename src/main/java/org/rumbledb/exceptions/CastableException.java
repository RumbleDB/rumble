package org.rumbledb.exceptions;

import java.io.Serial;

import org.rumbledb.errorcodes.ErrorCode;

public class CastableException extends RumbleException {

    @Serial private static final long serialVersionUID = 1L;

    public CastableException(String message, ExceptionMetadata metadata) {
        super(message, ErrorCode.CastableErrorCode, metadata);
    }
}
