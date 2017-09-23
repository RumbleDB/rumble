package sparksoniq.exceptions;

public class CliException extends SparksoniqRuntimeException{
    public CliException(String message) {
        super(message, ErrorCodes.CliErrorCode);
    }
}
