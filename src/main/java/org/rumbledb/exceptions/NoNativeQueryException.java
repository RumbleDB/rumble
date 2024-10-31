package org.rumbledb.exceptions;

public class NoNativeQueryException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public NoNativeQueryException() {
        super("It was not possible to generate a native sparkSQL query for this expression");
    }
}
