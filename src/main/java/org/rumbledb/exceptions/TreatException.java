package org.rumbledb.exceptions;

import java.io.Serial;

import org.rumbledb.errorcodes.ErrorCode;

public class TreatException extends RumbleException {

    @Serial private static final long serialVersionUID = 1L;

    public TreatException(String message, ExceptionMetadata metadata) {
        super(message, ErrorCode.DynamicTypeTreatErrorCode, metadata);
    }
}
