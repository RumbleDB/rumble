package org.rumbledb.exceptions;

import org.rumbledb.errorcodes.ErrorCode;

public class ComponentSpecifierNotAvailableException extends RumbleException {

    private static final long serialVersionUID = 1L;

    public ComponentSpecifierNotAvailableException(String message, ExceptionMetadata metadata) {
        super(message, ErrorCode.ComponentSpecifierNotAvailableErrorCode, metadata);
    }
}
