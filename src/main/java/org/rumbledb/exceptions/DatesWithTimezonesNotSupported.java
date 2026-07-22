package org.rumbledb.exceptions;

import org.rumbledb.errorcodes.ErrorCode;

import java.io.Serial;

public class DatesWithTimezonesNotSupported extends RumbleException {


    @Serial
    private static final long serialVersionUID = 1L;

    private static final String message =
        "By default, dates timezones cannot be converted to DataFrames. Try --dates-with-timezone yes to avoid DataFrames for dates.";

    public DatesWithTimezonesNotSupported(ExceptionMetadata metadata) {
        super(message, ErrorCode.DatesWithTimezonesNotSupported, metadata);
    }
}
