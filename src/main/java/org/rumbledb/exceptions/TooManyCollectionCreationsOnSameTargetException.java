package org.rumbledb.exceptions;

import org.rumbledb.errorcodes.ErrorCode;

public class TooManyCollectionCreationsOnSameTargetException extends RumbleException {

    private static final long serialVersionUID = 1L;

    public TooManyCollectionCreationsOnSameTargetException(String path, ExceptionMetadata metadata) {
        super(
            "Dynamic Updating error; Too many collection creations requested  at path: " + path + ".",
            ErrorCode.TooManyCollectionCreationsOnSameTargetException,
            metadata
        );
    }

}
