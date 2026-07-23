package org.rumbledb.exceptions;

import org.rumbledb.errorcodes.ErrorCode;

import java.io.Serial;

public class InvalidSequentialChildInNonSequentialParent extends SemanticException {
    @Serial
    private static final long serialVersionUID = 1L;

    public InvalidSequentialChildInNonSequentialParent(String message, ExceptionMetadata metadata) {
        super(message, ErrorCode.InvalidSequentialChildInNonSequentialParent, metadata);
    }
}
