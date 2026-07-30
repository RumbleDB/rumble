package org.rumbledb.exceptions;

import org.rumbledb.errorcodes.ErrorCode;

import java.io.Serial;

public class EmptyModuleURIException extends RumbleException {


    @Serial
    private static final long serialVersionUID = 1L;

    public EmptyModuleURIException(String message, ExceptionMetadata metadata) {
        super(message, ErrorCode.EmptyModuleURIErrorCode, metadata);
    }
}
