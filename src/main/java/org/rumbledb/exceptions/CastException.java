package org.rumbledb.exceptions;

import org.rumbledb.errorcodes.ErrorCode;

public class CastException extends RumbleException {


    private static final long serialVersionUID = 1L;

    public CastException(String message, ExceptionMetadata metadata) {
        super(message, ErrorCode.CastErrorCode, metadata);
    }
}
