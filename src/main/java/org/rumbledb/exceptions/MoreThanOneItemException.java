package org.rumbledb.exceptions;

import java.io.Serial;

public class MoreThanOneItemException extends RumbleException {

    @Serial
    private static final long serialVersionUID = 1L;

    public MoreThanOneItemException(ExceptionMetadata metadata) {
        super("More than one item found although one was expected.", metadata);
    }
}
