package org.rumbledb.exceptions;

import org.rumbledb.errorcodes.ErrorCode;

public class URIResolutionException extends RumbleException {

    private static final long serialVersionUID = 1L;

    public URIResolutionException(String message, ExceptionMetadata metadata) {
        super(message, ErrorCode.URIResolutionErrorCode, metadata);
    }
}
