package org.rumbledb.exceptions;

import org.rumbledb.errorcodes.ErrorCode;

public class NamespaceDoesNotMatchModuleException extends RumbleException {

    private static final long serialVersionUID = 1L;

    public NamespaceDoesNotMatchModuleException(String message, ExceptionMetadata metadata) {
        super(message, ErrorCode.NamespaceDoesNotMatchModule, metadata);
    }
}
