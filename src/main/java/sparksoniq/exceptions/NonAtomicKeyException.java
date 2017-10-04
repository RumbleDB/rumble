package sparksoniq.exceptions;

import sparksoniq.exceptions.codes.ErrorCodes;

public class NonAtomicKeyException extends SparksoniqRuntimeException{
    public NonAtomicKeyException(String message) {
        super(message, ErrorCodes.NonAtomicElementErrorCode);
    }
}
