package jiqs.jsoniq.exceptions;

public class UnexpectedTypeException extends SparksoniqRuntimeException{
    public UnexpectedTypeException(String message) {
        super(message, ErrorCodes.UnexpectedTypeErrorCode);
    }
}
