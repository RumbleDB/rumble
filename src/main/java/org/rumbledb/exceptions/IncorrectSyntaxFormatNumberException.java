package org.rumbledb.exceptions;

import java.io.Serial;

import org.rumbledb.errorcodes.ErrorCode;

public class IncorrectSyntaxFormatNumberException extends RumbleException {

    @Serial private static final long serialVersionUID = 1L;

    public IncorrectSyntaxFormatNumberException(String message, ExceptionMetadata metadata) {
        super(message, ErrorCode.IncorrectSyntaxFormatNumberErrorCode, metadata);
    }
}
