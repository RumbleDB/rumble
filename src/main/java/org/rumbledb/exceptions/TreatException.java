package org.rumbledb.exceptions;

import org.rumbledb.errorcodes.ErrorCode;

import java.io.Serial;

public class TreatException extends RumbleException {

    @Serial
    private static final long serialVersionUID = 1L;

    public TreatException(String message, ExceptionMetadata metadata) {
        super(message, ErrorCode.DynamicTypeTreatErrorCode, metadata);
    }
}
