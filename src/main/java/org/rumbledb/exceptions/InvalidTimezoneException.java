package org.rumbledb.exceptions;

import org.rumbledb.errorcodes.ErrorCodes;

import org.rumbledb.exceptions.ExceptionMetadata;

public class InvalidTimezoneException extends SparksoniqRuntimeException {

    private static final long serialVersionUID = 1L;

    public InvalidTimezoneException(String message, ExceptionMetadata metadata) {
        super(message, ErrorCodes.InvalidTimezoneValue, metadata);
    }
}
