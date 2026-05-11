package org.rumbledb.exceptions;

import org.rumbledb.errorcodes.ErrorCode;

public class IsStaticallyUnexpectedType extends RumbleException {
    private static final long serialVersionUID = 1L;

    public IsStaticallyUnexpectedType(String message) {
        super(message, ErrorCode.UnexpectedStaticType);
    }
}
