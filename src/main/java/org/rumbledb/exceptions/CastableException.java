package org.rumbledb.exceptions;

import org.rumbledb.errorcodes.ErrorCodes;

import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;

public class CastableException extends SparksoniqRuntimeException {


    private static final long serialVersionUID = 1L;

    public CastableException(String message, IteratorMetadata metadata) {
        super(message, ErrorCodes.CastableErrorCode, metadata.getExpressionMetadata());
    }
}
