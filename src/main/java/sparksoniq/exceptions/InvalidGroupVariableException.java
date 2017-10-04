package sparksoniq.exceptions;

import sparksoniq.exceptions.codes.ErrorCodes;

public class InvalidGroupVariableException extends SparksoniqRuntimeException{
    public InvalidGroupVariableException(String message) {
        super(message, ErrorCodes.InvalidGroupVariableErrorCode);
    }
}
