package org.rumbledb.exceptions;

import org.rumbledb.errorcodes.ErrorCodes;

import org.rumbledb.exceptions.ExceptionMetadata;

public class StringOfJSONiqItemException extends SparksoniqRuntimeException {

    private static final long serialVersionUID = 1L;

    public StringOfJSONiqItemException(String message, ExceptionMetadata metadata) {
        super(
            message,
            ErrorCodes.StringOfJSONiqItemsErrorCode,
            metadata.getExceptionMetadata()
        );
    }
}
