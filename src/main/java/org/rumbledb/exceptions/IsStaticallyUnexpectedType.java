package org.rumbledb.exceptions;

import org.rumbledb.errorcodes.ErrorCode;

import java.io.Serial;

public class IsStaticallyUnexpectedType extends RumbleException {
    @Serial
    private static final long serialVersionUID = 1L;

    public IsStaticallyUnexpectedType(String message) {
        super(message, ErrorCode.UnexpectedStaticType);
    }
}
