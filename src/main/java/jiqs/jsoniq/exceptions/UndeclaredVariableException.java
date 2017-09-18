package jiqs.jsoniq.exceptions;

public class UndeclaredVariableException extends SparksoniqRuntimeException {
    public UndeclaredVariableException(String message) {
        super(message, ErrorCodes.UndeclaredVariableErrorCode);
    }

}
