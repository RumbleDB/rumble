package org.rumbledb.exceptions;

import org.rumbledb.errorcodes.ErrorCodes;

import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;

public class TreatException extends SparksoniqRuntimeException {

    private static final long serialVersionUID = 1L;

    public TreatException(String message, IteratorMetadata metadata) {
        super(message, ErrorCodes.DynamicTypeTreatErrorCode, metadata.getExpressionMetadata());
    }
}
