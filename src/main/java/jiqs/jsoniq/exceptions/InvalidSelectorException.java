package jiqs.jsoniq.exceptions;

public class InvalidSelectorException extends SparksoniqRuntimeException{
    public InvalidSelectorException(String message) {
        super(message, ErrorCodes.InvalidSelectorErrorCode);
    }
}
