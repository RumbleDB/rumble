package org.rumbledb.exceptions;

import org.rumbledb.errorcodes.ErrorCode;

public class DecimalFormatPropertyConflictException extends SemanticException {
    private static final long serialVersionUID = 1L;

    public DecimalFormatPropertyConflictException(String message, ExceptionMetadata metadata) {
        super(message, ErrorCode.DecimalFormatPropertyConflictErrorCode, metadata);
    }
}
