package org.rumbledb.exceptions;

import java.io.Serial;

import org.rumbledb.errorcodes.ErrorCode;

public class InvalidSequentialChildInNonSequentialParent extends SemanticException {
    @Serial
    private static final long serialVersionUID = 1L;

    public InvalidSequentialChildInNonSequentialParent(String message, ExceptionMetadata metadata) {
        super(message, ErrorCode.InvalidSequentialChildInNonSequentialParent, metadata);
    }
}
