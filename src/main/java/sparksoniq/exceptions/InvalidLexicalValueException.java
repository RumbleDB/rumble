package sparksoniq.exceptions;

import sparksoniq.exceptions.codes.ErrorCodes;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;

public class InvalidLexicalValueException extends SparksoniqRuntimeException {

    private static final long serialVersionUID = 1L;

    public InvalidLexicalValueException(String message, IteratorMetadata metadata) {
        super(
            message,
            ErrorCodes.InvalidLexicalValueErrorCode,
            metadata.getExpressionMetadata()
        );
    }
}
