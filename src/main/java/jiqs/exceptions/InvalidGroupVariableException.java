package jiqs.exceptions;

public class InvalidGroupVariableException extends SparksoniqRuntimeException{
    public InvalidGroupVariableException(String message) {
        super(message, ErrorCodes.InvalidGroupVariableErrorCode);
    }
}
