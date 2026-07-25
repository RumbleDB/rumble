package org.rumbledb.exceptions;

import org.rumbledb.errorcodes.ErrorCode;

import java.io.Serial;

public class DuplicateModuleTargetNamespaceException extends RumbleException {


    @Serial
    private static final long serialVersionUID = 1L;

    public DuplicateModuleTargetNamespaceException(String message, ExceptionMetadata metadata) {
        super(message, ErrorCode.DuplicateModuleTargetNamespace, metadata);
    }
}
