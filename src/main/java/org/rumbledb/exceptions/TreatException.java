package org.rumbledb.exceptions;

import org.rumbledb.errorcodes.ErrorCodes;

import org.rumbledb.exceptions.ExceptionMetadata;

public class TreatException extends SparksoniqRuntimeException {

    private static final long serialVersionUID = 1L;

    public TreatException(String message, ExceptionMetadata metadata) {
        super(message, ErrorCodes.DynamicTypeTreatErrorCode, metadata.getExceptionMetadata());
    }
}
