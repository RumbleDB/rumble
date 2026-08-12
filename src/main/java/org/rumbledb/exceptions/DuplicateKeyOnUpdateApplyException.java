package org.rumbledb.exceptions;

import java.io.Serial;

import org.rumbledb.errorcodes.ErrorCode;

public class DuplicateKeyOnUpdateApplyException extends RumbleException {

    @Serial
    private static final long serialVersionUID = 1L;

    public DuplicateKeyOnUpdateApplyException(String keyInfo, ExceptionMetadata metadata) {
        super(
                "Dynamic Updating error; Duplicate keys inserted into target object during update application: "
                        + keyInfo
                        + ".",
                ErrorCode.DuplicateKeyOnUpdateApplyErrorCode,
                metadata);
    }
}
