package org.rumbledb.exceptions;

import org.rumbledb.errorcodes.ErrorCode;

public class UnimplementedFunctionException extends RumbleException {

    private static final long serialVersionUID = 1L;

    public UnimplementedFunctionException(String functionName, ExceptionMetadata metadata) {
        super(
            "Unimplemented function: " + functionName,
            ErrorCode.UnimplementedFunctionErrorCode,
            metadata
        );
    }
}
