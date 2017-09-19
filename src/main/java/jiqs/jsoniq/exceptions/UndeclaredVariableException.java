package jiqs.jsoniq.exceptions;

public class UndeclaredVariableException extends SemanticException {
    public UndeclaredVariableException(String message) {
        super(message, ErrorCodes.UndeclaredVariableErrorCode);
    }

}
