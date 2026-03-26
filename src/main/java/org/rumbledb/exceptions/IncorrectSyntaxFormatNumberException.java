package org.rumbledb.exceptions;

import org.rumbledb.errorcodes.ErrorCode;

public class IncorrectSyntaxFormatNumberException extends RumbleException {

    private static final long serialVersionUID = 1L;

    public IncorrectSyntaxFormatNumberException(String message, ExceptionMetadata metadata) {
        super(message, ErrorCode.IncorrectSyntaxFormatNumberErrorCode, metadata);
    }
}
