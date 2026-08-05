package org.rumbledb.exceptions;

import java.io.Serial;

import org.rumbledb.errorcodes.ErrorCode;

public class UnavailableResourceException extends RumbleException {

    @Serial private static final long serialVersionUID = 1L;

    public UnavailableResourceException(String message, ExceptionMetadata metadata) {
        super(message, ErrorCode.UnavailableResourceErrorCode, metadata);
    }
}
