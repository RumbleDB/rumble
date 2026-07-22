package org.rumbledb.exceptions;

import org.rumbledb.errorcodes.ErrorCode;

import java.io.Serial;

public class NamespaceDoesNotMatchModuleException extends RumbleException {

    @Serial
    private static final long serialVersionUID = 1L;

    public NamespaceDoesNotMatchModuleException(String message, ExceptionMetadata metadata) {
        super(message, ErrorCode.NamespaceDoesNotMatchModule, metadata);
    }
}
