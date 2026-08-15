package org.rumbledb.exceptions;

import java.io.Serial;

import org.rumbledb.errorcodes.ErrorCode;

public class NamespaceDoesNotMatchModuleException extends RumbleException {

    @Serial
    private static final long serialVersionUID = 1L;

    public NamespaceDoesNotMatchModuleException(String message, ExceptionMetadata metadata) {
        super(message, ErrorCode.NamespaceDoesNotMatchModule, metadata);
    }
}
