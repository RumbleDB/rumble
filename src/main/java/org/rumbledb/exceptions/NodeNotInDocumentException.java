package org.rumbledb.exceptions;

import java.io.Serial;

import org.rumbledb.errorcodes.ErrorCode;

public class NodeNotInDocumentException extends RumbleException {
    @Serial private static final long serialVersionUID = 1L;

    public NodeNotInDocumentException(String message, ExceptionMetadata metadata) {
        super(message, ErrorCode.NodeNotInDocumentErrorCode, metadata);
    }
}
