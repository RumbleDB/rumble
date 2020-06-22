package org.rumbledb.exceptions;

import org.rumbledb.errorcodes.ErrorCode;

public class EmptyModuleURIException extends RumbleException {


    private static final long serialVersionUID = 1L;

    public EmptyModuleURIException(String message, ExceptionMetadata metadata) {
        super(message, ErrorCode.EmptyModuleURIErrorCode, metadata);
    }
}
