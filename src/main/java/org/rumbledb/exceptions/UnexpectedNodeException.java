package org.rumbledb.exceptions;

import org.rumbledb.errorcodes.ErrorCode;

import java.io.Serial;

public class UnexpectedNodeException extends RumbleException {
    @Serial
    private static final long serialVersionUID = 1L;

    public UnexpectedNodeException(String message, ExceptionMetadata metadata) {
        super(message, ErrorCode.UnexpectedNode, metadata);
    }
}
