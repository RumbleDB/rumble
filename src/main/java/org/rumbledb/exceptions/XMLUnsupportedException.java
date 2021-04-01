package org.rumbledb.exceptions;


public class XMLUnsupportedException extends RumbleException {

    private static final long serialVersionUID = 1L;

    public XMLUnsupportedException(String message, ExceptionMetadata metadata) {
        super(message, metadata);
    }
}
