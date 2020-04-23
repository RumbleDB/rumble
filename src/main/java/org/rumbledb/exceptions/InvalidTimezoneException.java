package org.rumbledb.exceptions;

import org.rumbledb.errorcodes.ErrorCode;

public class InvalidTimezoneException extends SparksoniqRuntimeException {

    private static final long serialVersionUID = 1L;

    public InvalidTimezoneException(String message, ExceptionMetadata metadata) {
        super(message, ErrorCode.InvalidTimezoneValue, metadata);
    }
}
