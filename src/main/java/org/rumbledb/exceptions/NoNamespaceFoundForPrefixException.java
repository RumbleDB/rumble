package org.rumbledb.exceptions;

import java.io.Serial;

import org.rumbledb.errorcodes.ErrorCode;

public class NoNamespaceFoundForPrefixException extends RumbleException {

    @Serial
    private static final long serialVersionUID = 1L;

    public NoNamespaceFoundForPrefixException(String message, ExceptionMetadata metadata) {
        super(message, ErrorCode.NoNamespaceFoundForPrefixErrorCode, metadata);
    }
}
