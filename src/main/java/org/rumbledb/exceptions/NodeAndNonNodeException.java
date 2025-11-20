package org.rumbledb.exceptions;

import org.rumbledb.errorcodes.ErrorCode;

public class NodeAndNonNodeException extends RumbleException {
    private static final long serialVersionUID = 1L;

    public NodeAndNonNodeException(String message, ExceptionMetadata metadata) {
        super(message, ErrorCode.NodeAndNonNode, metadata);
    }
}
