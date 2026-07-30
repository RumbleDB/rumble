package org.rumbledb.exceptions;

import org.rumbledb.errorcodes.ErrorCode;

import java.io.Serial;

public class InvalidXmlDocumentException extends RumbleException {
    @Serial
    private static final long serialVersionUID = 1L;

    public InvalidXmlDocumentException(String message, ExceptionMetadata metadata) {
        super(message, ErrorCode.InvalidXmlDocumentErrorCode, metadata);
    }
}
