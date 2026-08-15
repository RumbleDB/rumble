package org.rumbledb.exceptions;

import java.io.Serial;

import org.rumbledb.errorcodes.ErrorCode;

public class DuplicateObjectInsertSourceException extends RumbleException {

    @Serial
    private static final long serialVersionUID = 1L;

    public DuplicateObjectInsertSourceException(String keyInfo, ExceptionMetadata metadata) {
        super(
                "Dynamic Updating error; Duplicate keys to insert into object: " + keyInfo + ".",
                ErrorCode.DuplicateObjectInsertSourceErrorCode,
                metadata);
    }
}
