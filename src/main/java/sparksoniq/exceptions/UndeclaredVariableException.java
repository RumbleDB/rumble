package sparksoniq.exceptions;

import sparksoniq.exceptions.codes.ErrorCodes;

public class UndeclaredVariableException extends SemanticException {
    public UndeclaredVariableException(String message) {
        super(message, ErrorCodes.UndeclaredVariableErrorCode);
    }

}
