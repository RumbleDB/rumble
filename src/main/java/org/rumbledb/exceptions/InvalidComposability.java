package org.rumbledb.exceptions;

import org.rumbledb.errorcodes.ErrorCode;

public class InvalidComposability extends SemanticException {
    private static final long serialVersionUID = 1L;

    public InvalidComposability(String message, ExceptionMetadata metadata) {
        super(message, ErrorCode.InvalidComposability, metadata);
    }
}
