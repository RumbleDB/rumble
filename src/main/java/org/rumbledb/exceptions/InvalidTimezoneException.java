package org.rumbledb.exceptions;

import org.rumbledb.errorcodes.ErrorCodes;

import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;

public class InvalidTimezoneException extends SparksoniqRuntimeException {

    private static final long serialVersionUID = 1L;

    public InvalidTimezoneException(String message, IteratorMetadata metadata) {
        super(message, ErrorCodes.InvalidTimezoneValue, metadata.getExpressionMetadata());
    }
}
