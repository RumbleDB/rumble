package org.rumbledb.exceptions;

import org.rumbledb.errorcodes.ErrorCode;

public class DuplicateModuleTargetNamespaceException extends RumbleException {


    private static final long serialVersionUID = 1L;

    public DuplicateModuleTargetNamespaceException(String message, ExceptionMetadata metadata) {
        super(message, ErrorCode.DuplicateModuleTargetNamespace, metadata);
    }
}
