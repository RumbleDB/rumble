package org.rumbledb.exceptions;

import org.rumbledb.errorcodes.ErrorCode;

public class TooManyEditsOnSameTargetException extends RumbleException {

    private static final long serialVersionUID = 1L;

    public TooManyEditsOnSameTargetException(String collection, double rowID, ExceptionMetadata metadata) {
        super(
            "Dynamic Updating error; Too many edit requested for collection: " + collection + " ; rowID:" + rowID + ".",
            ErrorCode.TooManyEditsOnSameTargetException,
            metadata
        );
    }

}
