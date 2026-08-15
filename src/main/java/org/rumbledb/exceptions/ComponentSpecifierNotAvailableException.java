package org.rumbledb.exceptions;

import java.io.Serial;

import org.rumbledb.errorcodes.ErrorCode;

public class ComponentSpecifierNotAvailableException extends RumbleException {

    @Serial
    private static final long serialVersionUID = 1L;

    public ComponentSpecifierNotAvailableException(String message, ExceptionMetadata metadata) {
        super(message, ErrorCode.ComponentSpecifierNotAvailableErrorCode, metadata);
    }
}
