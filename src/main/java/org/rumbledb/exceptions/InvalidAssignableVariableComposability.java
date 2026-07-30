package org.rumbledb.exceptions;

import org.rumbledb.errorcodes.ErrorCode;

import java.io.Serial;

public class InvalidAssignableVariableComposability extends SemanticException {
    @Serial
    private static final long serialVersionUID = 1L;

    public InvalidAssignableVariableComposability(String message, ExceptionMetadata metadata) {
        super(message, ErrorCode.InvalidAssignableVariableComposability, metadata);
    }
}
