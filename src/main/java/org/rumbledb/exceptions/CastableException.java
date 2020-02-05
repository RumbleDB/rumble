package org.rumbledb.exceptions;

import org.rumbledb.errorcodes.ErrorCodes;

import org.rumbledb.exceptions.ExceptionMetadata;

public class CastableException extends SparksoniqRuntimeException {


    private static final long serialVersionUID = 1L;

    public CastableException(String message, ExceptionMetadata metadata) {
        super(message, ErrorCodes.CastableErrorCode, metadata.getExceptionMetadata());
    }
}
