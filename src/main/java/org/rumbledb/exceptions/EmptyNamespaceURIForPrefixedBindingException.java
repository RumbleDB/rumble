package org.rumbledb.exceptions;

import org.rumbledb.errorcodes.ErrorCode;

public class EmptyNamespaceURIForPrefixedBindingException extends RumbleException {

    private static final long serialVersionUID = 1L;

    public EmptyNamespaceURIForPrefixedBindingException(String message, ExceptionMetadata metadata) {
        super(message, ErrorCode.EmptyNamespaceURIForPrefixedBindingErrorCode, metadata);
    }

    public EmptyNamespaceURIForPrefixedBindingException(String message) {
        super(message, ErrorCode.EmptyNamespaceURIForPrefixedBindingErrorCode);
    }
}
