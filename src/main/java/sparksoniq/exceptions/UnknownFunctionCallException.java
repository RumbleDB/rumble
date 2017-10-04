package sparksoniq.exceptions;

import sparksoniq.exceptions.codes.ErrorCodes;

public class UnknownFunctionCallException extends SparksoniqRuntimeException {
    public UnknownFunctionCallException(String message) {
        super(message, ErrorCodes.InvalidFunctionCallErrorCode);
    }

    public UnknownFunctionCallException() {
        super("Static error; The expanded QName and number of arguments in a static function call do not match the name and arity of a function signature in the static context.",
                ErrorCodes.InvalidFunctionCallErrorCode);
    }
}
