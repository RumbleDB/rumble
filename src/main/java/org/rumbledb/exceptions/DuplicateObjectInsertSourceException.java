package org.rumbledb.exceptions;

import org.rumbledb.errorcodes.ErrorCode;

public class DuplicateObjectInsertSourceException extends RumbleException {

    private static final long serialVersionUID = 1L;

    public DuplicateObjectInsertSourceException(String keyInfo, ExceptionMetadata metadata) {
        super(
                "Dynamic Updating error; Duplicate keys to insert into object: " + keyInfo + ".",
                ErrorCode.DuplicateObjectInsertSourceErrorCode,
                metadata
        );
    }

}
