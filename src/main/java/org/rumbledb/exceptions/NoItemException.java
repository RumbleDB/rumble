package org.rumbledb.exceptions;

public class NoItemException extends Exception {

    private static final long serialVersionUID = 1L;

    public NoItemException() {
        super("No item found although one was expected.");
    }
}
