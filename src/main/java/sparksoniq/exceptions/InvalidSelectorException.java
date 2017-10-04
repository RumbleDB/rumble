package sparksoniq.exceptions;

import sparksoniq.exceptions.codes.ErrorCodes;

public class InvalidSelectorException extends SparksoniqRuntimeException{
    public InvalidSelectorException(String message) {
        super(message, ErrorCodes.InvalidSelectorErrorCode);
    }
}
