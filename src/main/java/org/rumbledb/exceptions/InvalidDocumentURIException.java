package org.rumbledb.exceptions;

import org.rumbledb.errorcodes.ErrorCode;

public class InvalidDocumentURIException extends RumbleException {

    private static final long serialVersionUID = 1L;

    public InvalidDocumentURIException(String message, ExceptionMetadata metadata) {
        super(message, ErrorCode.InvalidDocumentURIErrorCode, metadata);
    }
}
