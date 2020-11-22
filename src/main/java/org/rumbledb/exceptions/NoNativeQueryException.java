package org.rumbledb.exceptions;

public class NoNativeQueryException extends RuntimeException {
    public NoNativeQueryException() {
        super("It was not possible to generate a native sparkSQL query for this expression");
    }
}
