package sparksoniq.exceptions;

import sparksoniq.exceptions.codes.ErrorCodes;

public class JsoniqVersionException extends ParsingException{
    public JsoniqVersionException() {
        super("Static error; The version number specified in a version declaration is not supported by the implementation.",
                ErrorCodes.InvalidJsoniqVersionErrorCode);
    }

}
