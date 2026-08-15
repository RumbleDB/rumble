package org.rumbledb.exceptions;

import java.io.Serial;

import org.rumbledb.errorcodes.ErrorCode;

public class DuplicateModuleTargetNamespaceException extends RumbleException {

    @Serial
    private static final long serialVersionUID = 1L;

    public DuplicateModuleTargetNamespaceException(String message, ExceptionMetadata metadata) {
        super(message, ErrorCode.DuplicateModuleTargetNamespace, metadata);
    }
}
