package sparksoniq.exceptions;

public class JsoniqVersionException extends ParsingException{
    public JsoniqVersionException() {
        super("Invalid JSONiq Version", ErrorCodes.InvalidJsoniqVersionErrorCode);
    }

}
