package org.rumbledb.exceptions;

import java.io.Serial;

import org.rumbledb.errorcodes.ErrorCode;

public class NamespacePrefixBoundTwiceException extends RumbleException {

    @Serial private static final long serialVersionUID = 1L;

    public NamespacePrefixBoundTwiceException(String message, ExceptionMetadata metadata) {
        super(message, ErrorCode.NamespacePrefixBoundTwiceCode, metadata);
    }
}
