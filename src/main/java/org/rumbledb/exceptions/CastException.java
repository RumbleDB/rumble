package org.rumbledb.exceptions;

import java.io.Serial;

import org.rumbledb.errorcodes.ErrorCode;

public class CastException extends RumbleException {

    @Serial private static final long serialVersionUID = 1L;

    public CastException(String message, ExceptionMetadata metadata) {
        super(message, ErrorCode.CastErrorCode, metadata);
    }
}
