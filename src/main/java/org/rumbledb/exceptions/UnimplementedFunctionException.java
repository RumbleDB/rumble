package org.rumbledb.exceptions;

import java.io.Serial;

import org.rumbledb.context.Name;
import org.rumbledb.errorcodes.ErrorCode;

public class UnimplementedFunctionException extends RumbleException {

    @Serial private static final long serialVersionUID = 1L;

    public UnimplementedFunctionException(String functionName, ExceptionMetadata metadata) {
        super(
                "Unimplemented function: " + functionName,
                new ErrorCode(new Name(null, null, functionName + "_unimplemented")),
                metadata);
    }
}
