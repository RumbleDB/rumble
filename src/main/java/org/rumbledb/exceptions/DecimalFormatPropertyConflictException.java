package org.rumbledb.exceptions;

import java.io.Serial;

import org.rumbledb.errorcodes.ErrorCode;

public class DecimalFormatPropertyConflictException extends SemanticException {
    @Serial private static final long serialVersionUID = 1L;

    public DecimalFormatPropertyConflictException(String message, ExceptionMetadata metadata) {
        super(message, ErrorCode.DecimalFormatPropertyConflictErrorCode, metadata);
    }
}
