package org.rumbledb.exceptions;

import java.io.Serial;

import org.rumbledb.errorcodes.ErrorCode;

public class InvalidAssignableVariableComposability extends SemanticException {
    @Serial
    private static final long serialVersionUID = 1L;

    public InvalidAssignableVariableComposability(String message, ExceptionMetadata metadata) {
        super(message, ErrorCode.InvalidAssignableVariableComposability, metadata);
    }
}
