package org.rumbledb.exceptions;

import org.rumbledb.errorcodes.ErrorCode;

public class UnavailableResourceException extends RumbleException {

    private static final long serialVersionUID = 1L;

    public UnavailableResourceException(String message, ExceptionMetadata metadata) {
        super(message, ErrorCode.UnavailableResourceErrorCode, metadata);
    }
}
