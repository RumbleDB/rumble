package org.rumbledb.exceptions;

import org.rumbledb.errorcodes.ErrorCodes;

public class ComponentSpecifierNotAvailableException extends SparksoniqRuntimeException {

    private static final long serialVersionUID = 1L;

    public ComponentSpecifierNotAvailableException(String message, ExceptionMetadata metadata) {
        super(message, ErrorCodes.ComponentSpecifierNotAvailableErrorCode, metadata);
    }
}
