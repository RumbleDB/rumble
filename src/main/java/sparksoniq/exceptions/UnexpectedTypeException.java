package sparksoniq.exceptions;

import sparksoniq.exceptions.codes.ErrorCodes;

public class UnexpectedTypeException extends SparksoniqRuntimeException{
    public UnexpectedTypeException(String message) {
        super(message, ErrorCodes.UnexpectedTypeErrorCode);
    }
}
