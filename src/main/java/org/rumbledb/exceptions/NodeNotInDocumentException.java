package org.rumbledb.exceptions;

import org.rumbledb.errorcodes.ErrorCode;

import java.io.Serial;

public class NodeNotInDocumentException extends RumbleException {
    @Serial
    private static final long serialVersionUID = 1L;

    public NodeNotInDocumentException(String message, ExceptionMetadata metadata) {
        super(message, ErrorCode.NodeNotInDocumentErrorCode, metadata);
    }
}
