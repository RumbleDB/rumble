package org.rumbledb.exceptions;

import org.rumbledb.errorcodes.ErrorCode;

import java.io.Serial;

public class NodeAndNonNodeException extends RumbleException {
    @Serial
    private static final long serialVersionUID = 1L;

    public NodeAndNonNodeException(String message, ExceptionMetadata metadata) {
        super(message, ErrorCode.NodeAndNonNode, metadata);
    }
}
