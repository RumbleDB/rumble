package org.rumbledb.exceptions;

import java.io.Serial;

import org.rumbledb.errorcodes.ErrorCode;

public class InvalidDecimalFormatName extends SemanticException {
    @Serial
    private static final long serialVersionUID = 1L;

    public InvalidDecimalFormatName(String message, ExceptionMetadata metadata) {
        super(message, ErrorCode.InvalidDecimalFormatName, metadata);
    }
}
