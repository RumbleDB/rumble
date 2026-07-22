package org.rumbledb.exceptions;

import org.rumbledb.errorcodes.ErrorCode;

import java.io.Serial;

public class ComponentSpecifierNotAvailableException extends RumbleException {

    @Serial
    private static final long serialVersionUID = 1L;

    public ComponentSpecifierNotAvailableException(String message, ExceptionMetadata metadata) {
        super(message, ErrorCode.ComponentSpecifierNotAvailableErrorCode, metadata);
    }
}
