package org.rumbledb.exceptions;

import org.rumbledb.errorcodes.ErrorCodes;

import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;

public class CastException extends SparksoniqRuntimeException {


    private static final long serialVersionUID = 1L;

    public CastException(String message, IteratorMetadata metadata) {
        super(message, ErrorCodes.CastErrorCode, metadata.getExpressionMetadata());
    }
}
