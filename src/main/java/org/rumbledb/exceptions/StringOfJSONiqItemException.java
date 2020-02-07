package org.rumbledb.exceptions;

import org.rumbledb.errorcodes.ErrorCodes;

public class StringOfJSONiqItemException extends SparksoniqRuntimeException {

    private static final long serialVersionUID = 1L;

    public StringOfJSONiqItemException(String message, ExceptionMetadata metadata) {
        super(
            message,
            ErrorCodes.StringOfJSONiqItemsErrorCode,
            metadata
        );
    }
}
