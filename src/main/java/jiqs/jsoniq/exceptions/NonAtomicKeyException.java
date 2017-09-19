package jiqs.jsoniq.exceptions;

public class NonAtomicKeyException extends SparksoniqRuntimeException{
    public NonAtomicKeyException(String message) {
        super(message, ErrorCodes.NonAtomicElementErrorCode);
    }
}
