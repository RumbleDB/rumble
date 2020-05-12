package org.rumbledb.exceptions;

import org.rumbledb.errorcodes.ErrorCode;

public class CastableException extends RumbleException {


    private static final long serialVersionUID = 1L;

    public CastableException(String message, ExceptionMetadata metadata) {
        super(message, ErrorCode.CastableErrorCode, metadata);
    }
}
