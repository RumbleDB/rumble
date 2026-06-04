package org.rumbledb.exceptions;

import org.rumbledb.errorcodes.ErrorCode;

public class NoNamespaceFoundForPrefixException extends RumbleException {

    private static final long serialVersionUID = 1L;

    public NoNamespaceFoundForPrefixException(String message, ExceptionMetadata metadata) {
        super(message, ErrorCode.NoNamespaceFoundForPrefixErrorCode, metadata);
    }
}
