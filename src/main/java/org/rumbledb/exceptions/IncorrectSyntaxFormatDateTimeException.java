package org.rumbledb.exceptions;

import java.io.Serial;

import org.rumbledb.errorcodes.ErrorCode;

public class IncorrectSyntaxFormatDateTimeException extends RumbleException {

    @Serial private static final long serialVersionUID = 1L;

    public IncorrectSyntaxFormatDateTimeException(String message, ExceptionMetadata metadata) {
        super(message, ErrorCode.IncorrectSyntaxFormatDateTimeErrorCode, metadata);
    }
}
