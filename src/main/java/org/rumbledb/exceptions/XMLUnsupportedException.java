package org.rumbledb.exceptions;


import java.io.Serial;

public class XMLUnsupportedException extends RumbleException {

    @Serial
    private static final long serialVersionUID = 1L;

    public XMLUnsupportedException(String message, ExceptionMetadata metadata) {
        super(message, metadata);
    }
}
