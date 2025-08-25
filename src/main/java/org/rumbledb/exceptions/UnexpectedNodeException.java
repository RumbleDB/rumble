package org.rumbledb.exceptions;

import org.rumbledb.errorcodes.ErrorCode;

public class UnexpectedNodeException extends RumbleException {
    public UnexpectedNodeException(String message, ExceptionMetadata metadata) {
        super(message, ErrorCode.UnexpectedNode, metadata);
    }
}
