package org.rumbledb.exceptions;

import org.rumbledb.errorcodes.ErrorCode;

public class IncorrectSyntaxFormatDateTimeException extends SparksoniqRuntimeException {

    private static final long serialVersionUID = 1L;

    public IncorrectSyntaxFormatDateTimeException(String message, ExceptionMetadata metadata) {
        super(message, ErrorCode.IncorrectSyntaxFormatDateTimeErrorCode, metadata);
    }
}
