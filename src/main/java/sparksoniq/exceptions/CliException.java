package sparksoniq.exceptions;

import sparksoniq.exceptions.codes.ErrorCodes;

public class CliException extends SparksoniqRuntimeException{
    public CliException(String message) {
        super(message, ErrorCodes.CliErrorCode);
    }
}
