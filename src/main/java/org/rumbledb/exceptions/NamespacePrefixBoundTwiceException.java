package org.rumbledb.exceptions;

import org.rumbledb.errorcodes.ErrorCode;

public class NamespacePrefixBoundTwiceException extends RumbleException {


    private static final long serialVersionUID = 1L;

    public NamespacePrefixBoundTwiceException(String message, ExceptionMetadata metadata) {
        super(message, ErrorCode.NamespacePrefixBoundTwiceCode, metadata);
    }
}
