package org.rumbledb.exceptions;

import org.rumbledb.errorcodes.ErrorCode;

public class UnexpectedStaticTypeException extends RumbleException {

    private static final long serialVersionUID = 1L;

    public UnexpectedStaticTypeException(String message) {
        // TODO: investigates errorCode and Metadata
        super(message);
    }

    public UnexpectedStaticTypeException(String message, ErrorCode errorCode){
        super(message, errorCode);
    }
}
