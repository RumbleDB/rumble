package org.rumbledb.exceptions;

import org.rumbledb.errorcodes.ErrorCode;

import java.io.Serial;

public class NamespacePrefixBoundTwiceException extends RumbleException {


    @Serial
    private static final long serialVersionUID = 1L;

    public NamespacePrefixBoundTwiceException(String message, ExceptionMetadata metadata) {
        super(message, ErrorCode.NamespacePrefixBoundTwiceCode, metadata);
    }
}
