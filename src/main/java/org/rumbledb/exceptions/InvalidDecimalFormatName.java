package org.rumbledb.exceptions;

import org.rumbledb.errorcodes.ErrorCode;

public class InvalidDecimalFormatName extends SemanticException {
    private static final long serialVersionUID = 1L;

    public InvalidDecimalFormatName(String message, ExceptionMetadata metadata) {
        super(message, ErrorCode.InvalidDecimalFormatName, metadata);
    }
}
