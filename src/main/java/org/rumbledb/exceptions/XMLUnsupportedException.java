package org.rumbledb.exceptions;

import org.rumbledb.errorcodes.ErrorCode;

public class XMLUnsupportedException extends RumbleException{

    public XMLUnsupportedException(String message, ExceptionMetadata metadata) {
        super(message, metadata);
    }
}
