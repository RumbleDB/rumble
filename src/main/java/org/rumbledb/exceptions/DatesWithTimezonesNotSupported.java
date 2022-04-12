package org.rumbledb.exceptions;

import org.rumbledb.errorcodes.ErrorCode;

public class DatesWithTimezonesNotSupported extends RumbleException {


    private static final long serialVersionUID = 1L;

    private static final String message =
        "By default, dates timezones cannot be converted to DataFrames. Try --dates-with-timezone yes to avoid DataFrames for dates.";

    public DatesWithTimezonesNotSupported(ExceptionMetadata metadata) {
        super(message, ErrorCode.DatesWithTimezonesNotSupported, metadata);
    }
}
