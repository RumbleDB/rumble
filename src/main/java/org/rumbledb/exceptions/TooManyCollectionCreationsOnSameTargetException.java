package org.rumbledb.exceptions;

import java.io.Serial;

import org.rumbledb.errorcodes.ErrorCode;

public class TooManyCollectionCreationsOnSameTargetException extends RumbleException {

    @Serial private static final long serialVersionUID = 1L;

    public TooManyCollectionCreationsOnSameTargetException(
            String path, ExceptionMetadata metadata) {
        super(
                "Dynamic Updating error; Too many collection creations requested  at path: "
                        + path
                        + ".",
                ErrorCode.TooManyCollectionCreationsOnSameTargetException,
                metadata);
    }
}
