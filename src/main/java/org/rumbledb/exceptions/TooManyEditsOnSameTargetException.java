package org.rumbledb.exceptions;

import org.rumbledb.errorcodes.ErrorCode;

import java.io.Serial;

public class TooManyEditsOnSameTargetException extends RumbleException {

    @Serial
    private static final long serialVersionUID = 1L;

    public TooManyEditsOnSameTargetException(String collection, double rowID, ExceptionMetadata metadata) {
        super(
            "Dynamic Updating error; Too many edit requested for collection: " + collection + " ; rowID:" + rowID + ".",
            ErrorCode.TooManyEditsOnSameTargetException,
            metadata
        );
    }

}
