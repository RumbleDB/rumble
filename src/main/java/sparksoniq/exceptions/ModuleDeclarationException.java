package sparksoniq.exceptions;

import sparksoniq.exceptions.codes.ErrorCodes;

public class ModuleDeclarationException extends ParsingException {
    public ModuleDeclarationException(String message) {
        super(message, ErrorCodes.ModuleDeclarationErrorCode);
    }
}
