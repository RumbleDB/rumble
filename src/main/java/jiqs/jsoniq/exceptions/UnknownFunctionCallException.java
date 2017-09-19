package jiqs.jsoniq.exceptions;

public class UnknownFunctionCallException extends SparksoniqRuntimeException {
    public UnknownFunctionCallException(String message) {
        super(message, ErrorCodes.InvalidFunctionCallErrorCode);
    }

    public UnknownFunctionCallException() {
        super("Unknown/Unsupported function call", ErrorCodes.InvalidFunctionCallErrorCode);
    }
}
