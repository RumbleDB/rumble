package org.rumbledb.exceptions;

import org.rumbledb.context.Name;
import org.rumbledb.errorcodes.ErrorCode;

public class UnimplementedFunctionException extends RumbleException {

    private static final long serialVersionUID = 1L;

    public UnimplementedFunctionException(String functionName, ExceptionMetadata metadata) {
        super(
            "Unimplemented function: " + functionName,
            new ErrorCode(new Name(null, null, functionName + " unimplemented")),
            metadata
        );
    }
}
