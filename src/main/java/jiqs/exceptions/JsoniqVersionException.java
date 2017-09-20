package jiqs.exceptions;

public class JsoniqVersionException extends SparksoniqRuntimeException{
    public JsoniqVersionException() {
        super("Invalid JSONiq Version", ErrorCodes.InvalidJsoniqVersionErrorCode);
    }

    public JsoniqVersionException(String message) {
        super(message, ErrorCodes.InvalidJsoniqVersionErrorCode);
    }
}
