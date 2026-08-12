package org.rumbledb.exceptions;

import java.io.Serial;

import org.rumbledb.errorcodes.ErrorCode;

public class NodeAndNonNodeException extends RumbleException {
    @Serial
    private static final long serialVersionUID = 1L;

    public NodeAndNonNodeException(String message, ExceptionMetadata metadata) {
        super(message, ErrorCode.NodeAndNonNode, metadata);
    }
}
