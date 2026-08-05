package org.rumbledb.exceptions;

import java.io.Serial;

import org.rumbledb.errorcodes.ErrorCode;

public class UnexpectedNodeException extends RumbleException {
    @Serial private static final long serialVersionUID = 1L;

    public UnexpectedNodeException(String message, ExceptionMetadata metadata) {
        super(message, ErrorCode.UnexpectedNode, metadata);
    }
}
