package sparksoniq.exceptions;

import sparksoniq.exceptions.codes.ErrorCodes;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;

public class StringOfJSONiqItemException extends SparksoniqRuntimeException {

    private static final long serialVersionUID = 1L;

    public StringOfJSONiqItemException(String message, IteratorMetadata metadata) {
        super(
                message,
                ErrorCodes.StringOfJSONiqItemsErrorCode,
                metadata.getExpressionMetadata()
        );
    }
}
