package org.rumbledb.exceptions;

import org.rumbledb.errorcodes.ErrorCode;

import java.io.Serial;

public class NoNamespaceFoundForPrefixException extends RumbleException {

    @Serial
    private static final long serialVersionUID = 1L;

    public NoNamespaceFoundForPrefixException(String message, ExceptionMetadata metadata) {
        super(message, ErrorCode.NoNamespaceFoundForPrefixErrorCode, metadata);
    }
}
