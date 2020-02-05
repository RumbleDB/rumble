package org.rumbledb.exceptions;

import org.rumbledb.errorcodes.ErrorCodes;

import org.rumbledb.exceptions.ExceptionMetadata;

public class CastException extends SparksoniqRuntimeException {


    private static final long serialVersionUID = 1L;

    public CastException(String message, ExceptionMetadata metadata) {
        super(message, ErrorCodes.CastErrorCode, metadata);
    }
}
