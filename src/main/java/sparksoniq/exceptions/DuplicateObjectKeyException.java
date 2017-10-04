package sparksoniq.exceptions;

import sparksoniq.exceptions.codes.ErrorCodes;

public class DuplicateObjectKeyException extends SparksoniqRuntimeException{
    public DuplicateObjectKeyException(String keyName) {
        super("Dynamic error; Two pairs in an object have the same key name: " + keyName + ".",
                ErrorCodes.DuplicatePairNameErrorCode);
    }
}
