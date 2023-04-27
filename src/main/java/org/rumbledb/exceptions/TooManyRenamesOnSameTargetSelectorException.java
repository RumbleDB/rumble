package org.rumbledb.exceptions;

import org.rumbledb.errorcodes.ErrorCode;

public class TooManyRenamesOnSameTargetSelectorException extends RumbleException {

    private static final long serialVersionUID = 1L;

    public TooManyRenamesOnSameTargetSelectorException(String keyInfo, ExceptionMetadata metadata) {
        super(
                "Dynamic Updating error; Too many renames on same object at key: " + keyInfo + ".",
                ErrorCode.TooManyRenamesOnSameTargetSelectorErrorCode,
                metadata
        );
    }

}
