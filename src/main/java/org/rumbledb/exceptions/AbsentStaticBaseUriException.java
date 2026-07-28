package org.rumbledb.exceptions;

import org.rumbledb.errorcodes.ErrorCode;

public class AbsentStaticBaseUriException extends RumbleException {

    private static final long serialVersionUID = 1L;

    public AbsentStaticBaseUriException(String message, ExceptionMetadata metadata) {
        super(message, ErrorCode.StaticBaseUriAbsentErrorCode, metadata);
    }
}
