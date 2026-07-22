package org.rumbledb.exceptions;

import org.rumbledb.errorcodes.ErrorCode;

import java.io.Serial;

public class CastException extends RumbleException {


    @Serial
    private static final long serialVersionUID = 1L;

    public CastException(String message, ExceptionMetadata metadata) {
        super(message, ErrorCode.CastErrorCode, metadata);
    }
}
