package org.rumbledb.exceptions;

import org.rumbledb.errorcodes.ErrorCode;

import java.io.Serial;

public class DuplicateKeyOnUpdateApplyException extends RumbleException {

    @Serial
    private static final long serialVersionUID = 1L;

    public DuplicateKeyOnUpdateApplyException(String keyInfo, ExceptionMetadata metadata) {
        super(
            "Dynamic Updating error; Duplicate keys inserted into target object during update application: "
                + keyInfo
                + ".",
            ErrorCode.DuplicateKeyOnUpdateApplyErrorCode,
            metadata
        );
    }
}
