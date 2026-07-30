package org.rumbledb.exceptions;

public class MoreThanOneItemException extends Exception {

    private static final long serialVersionUID = 1L;

    public MoreThanOneItemException() {
        super("More than one item found although one was expected.");
    }
}
