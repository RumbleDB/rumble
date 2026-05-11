package org.rumbledb.exceptions;

import org.rumbledb.errorcodes.ErrorCode;

public class InvalidDecimalFormatPropertyConflict extends SemanticException {
    private static final long serialVersionUID = 1L;

    public InvalidDecimalFormatPropertyConflict(String message, ExceptionMetadata metadata) {
        super(message, ErrorCode.InvalidDecimalFormatPropertyConflict, metadata);
    }
}
