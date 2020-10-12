package org.rumbledb.exceptions;

public class UnexpectedStaticTypeException extends RumbleException {

    private static final long serialVersionUID = 1L;

    public UnexpectedStaticTypeException(String message) {
        // TODO: investigates errorCode and Metadata
        super(message);
    }
}
