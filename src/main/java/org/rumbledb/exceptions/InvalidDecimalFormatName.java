package org.rumbledb.exceptions;

import org.rumbledb.errorcodes.ErrorCode;

import java.io.Serial;

public class InvalidDecimalFormatName extends SemanticException {
    @Serial
    private static final long serialVersionUID = 1L;

    public InvalidDecimalFormatName(String message, ExceptionMetadata metadata) {
        super(message, ErrorCode.InvalidDecimalFormatName, metadata);
    }
}
