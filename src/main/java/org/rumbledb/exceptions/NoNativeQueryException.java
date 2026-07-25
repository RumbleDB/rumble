package org.rumbledb.exceptions;

import java.io.Serial;

public class NoNativeQueryException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public NoNativeQueryException() {
        super("It was not possible to generate a native sparkSQL query for this expression");
    }
}
