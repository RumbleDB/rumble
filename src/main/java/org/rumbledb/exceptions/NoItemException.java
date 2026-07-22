package org.rumbledb.exceptions;

import java.io.Serial;

public class NoItemException extends Exception {

    @Serial
    private static final long serialVersionUID = 1L;

    public NoItemException() {
        super("No item found although one was expected.");
    }
}
