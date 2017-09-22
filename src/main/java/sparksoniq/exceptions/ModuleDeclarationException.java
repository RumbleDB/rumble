package sparksoniq.exceptions;

public class ModuleDeclarationException extends ParsingException {
    public ModuleDeclarationException(String message) {
        super(message, ErrorCodes.ModuleDeclarationErrorCode);
    }
}
