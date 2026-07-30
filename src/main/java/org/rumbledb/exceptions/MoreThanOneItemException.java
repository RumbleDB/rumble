package org.rumbledb.exceptions;

import java.io.Serial;

public class MoreThanOneItemException extends Exception {

    @Serial
    private static final long serialVersionUID = 1L;

    public MoreThanOneItemException() {
        super("More than one item found although one was expected.");
    }
}
