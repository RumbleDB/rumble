package org.rumbledb.exceptions;

import org.rumbledb.errorcodes.ErrorCode;

import java.io.Serial;

public class DecimalFormatPropertyConflictException extends SemanticException {
    @Serial
    private static final long serialVersionUID = 1L;

    public DecimalFormatPropertyConflictException(String message, ExceptionMetadata metadata) {
        super(message, ErrorCode.DecimalFormatPropertyConflictErrorCode, metadata);
    }
}
